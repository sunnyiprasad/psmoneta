/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsc.moneta.module;

import com.rsc.moneta.Config;
import com.rsc.moneta.bean.Account;
import com.rsc.moneta.bean.PaymentOrder;
import com.rsc.moneta.dao.AccountDao;
import com.rsc.moneta.dao.Dao;
import com.rsc.moneta.dao.EMF;
import com.rsc.moneta.dao.PaymentOrderDao;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Entity;
import javax.persistence.EntityManager;

/**
 *
 * @author sulic
 */
public class MainPaymentHandler {

    public CheckResponse check(PaymentOrder paymentOrder, Double amount) {
        CheckResponse checkResponse = new CheckResponse();
        switch (paymentOrder.getStatus()) {
            case PaymentOrder.ORDER_STATUS_ACCEPTED: {
                try {
                    OutputHandler outputHandler = new Config().buildOutputHandler(paymentOrder.getMarket().getOutputHandlerType());
                    CheckResponse monetaResponse = outputHandler.check(paymentOrder);
                    if (monetaResponse != null) {
                        checkResponse.setResultCode(monetaResponse.getResultCode());
                    } else {
                        checkResponse.setResultCode(ResultCode.ERROR_TRY_AGAIN);
                    }
                } catch (Exception ex) {
                    Logger.getLogger(MainPaymentHandler.class.getName()).log(Level.SEVERE, "Ошибка при создании обработчика", ex);
                    ex.printStackTrace();
                    checkResponse.setResultCode(ResultCode.INTERNAL_ERROR);
                }
                break;
            }
            case PaymentOrder.ORDER_STATUS_NOT_PAID_AND_REJECTED_BY_EMARKETPLACE: {
                checkResponse.setResultCode(ResultCode.ORDER_NOT_ACTUAL);
                break;
            }
            case PaymentOrder.ORDER_STATUS_PAID_AND_COMPLETED: {
                checkResponse.setResultCode(ResultCode.ORDER_ALREADY_PAID);
                break;
            }
            case PaymentOrder.ORDER_STATUS_PAID_BUT_NOT_COMPLETED_AND_STILL_PROCESSING: {
                checkResponse.setResultCode(ResultCode.ORDER_ALREADY_PAID);
                break;
            }
            case PaymentOrder.ORDER_STATUS_PAID_BUT_REJECTED_BY_EMARKETPLACE: {
                checkResponse.setResultCode(ResultCode.ORDER_ALREADY_PAID);
                break;
            }
            case PaymentOrder.ORDER_STATUS_UNDEFINED: {
                checkResponse.setResultCode(ResultCode.ERROR_TRY_AGAIN);
                break;
            }
            default: {
                checkResponse.setResultCode(ResultCode.UNKNOWN_CODE_RETURNED_BY_EMARKEPLACE);
                break;
            }
        }
        return checkResponse;
    }

    public CheckResponse pay(PaymentOrder order, double amount) {
        CheckResponse checkResponse = new CheckResponse();
        switch (order.getStatus()) {
            case PaymentOrder.ORDER_STATUS_ACCEPTED: {
                try {
                    EntityManager em = EMF.getEntityManager();
                    order.setStatus(PaymentOrder.ORDER_STATUS_PAID_BUT_NOT_COMPLETED_AND_STILL_PROCESSING);
                    new Dao(em).persist(order);
                    OutputHandler outputHandler = new Config().buildOutputHandler(order.getMarket().getOutputHandlerType());
                    CheckResponse response = outputHandler.pay(order);
                    debug(" ResultCode = " + response.getResultCode());
                    debug(" Description = " + response.getDescription());
                    if (response != null) {
                        if (response.getResultCode() == ResultCode.SUCCESS_WITHOUT_AMOUNT
                                || response.getResultCode() == ResultCode.SUCCESS_WITH_AMOUNT) {
                            try {
                                if (order.getAccount() == null) {
                                    order.setStatus(PaymentOrder.ORDER_STATUS_PAID_AND_COMPLETED_BUT_NOT_FOUND_MARKET_ACCOUNT);
                                    new Dao(em).persist(order);
                                } else {
                                    if (order.getAmount() == amount) {
                                        debug("Сумма совершенно одинаковая просто производим начисление на счет");
                                        new PaymentOrderDao(em).processOrderPay(order);
                                    } else if (order.getAmount() < amount) {
                                        debug("Сумма больше чем нужно, поэтому сдачу начисляем на счет абонента в системе");
                                        new PaymentOrderDao(em).processOrderPayWithOddMoney(order, order.getAmount() - amount);
                                    } else {
                                        debug("Суммы меньше чем нужно. Проверяем есть ли деньги на счету абонента в нашей системе");
                                        if (order.getUser().getAccount(order.getCurrency()).getBalance() + amount > order.getAmount()) {
                                            debug("Денег на счету достаточно, можно проводить платеж с тех средств");
                                            new PaymentOrderDao(em).processOrderFromBalance(order, amount);
                                        } else {
                                            debug("Денег не достаточно, поэтому вся сумма остается на счету абонента в нашей системе.");
                                            new PaymentOrderDao(em).addUserAccountBalance(order.getUser().getAccount(order.getCurrency()).getId(), amount);
                                            checkResponse.setResultCode(ResultCode.SUCCESS_BUT_MONEY_LESS_THAN_MUST_BE);
                                            checkResponse.setDescription("Деньги зачислены на счет абонента в нашей системе, т.к. внесенных средств недостаточно для оплаты заказа.");
                                            checkResponse.setTransactionId(order.getTransactionId());
                                            checkResponse.setMarketId(order.getMarketId());
                                            return checkResponse;
                                        }
                                    }
                                }
                            } finally {
                                em.close();
                            }
                        } else if (response.getResultCode() == ResultCode.ORDER_NOT_ACTUAL) {
                            order.setStatus(PaymentOrder.ORDER_STATUS_PAID_BUT_REJECTED_BY_EMARKETPLACE);
                            new Dao(em).persist(order);
                        } else if (response.getResultCode() == ResultCode.ORDER_NOT_FOUND_IN_EMARKEPLACE) {
                            order.setStatus(PaymentOrder.ORDER_STATUS_PAID_BUT_ORDER_NOT_FOUND);
                            new Dao(em).persist(order);
                        } else if (response.getResultCode() == ResultCode.ERROR_TRY_AGAIN ||
                                response.getResultCode() == ResultCode.ORDER_PROCESSING){
                            
                        }else {

                            order.setStatus(PaymentOrder.ORDER_STATUS_PAID_BUT_EMARKETPLACE_CANNOT_PROCESS_IT);
                        }
                    } else {
                        checkResponse.setResultCode(ResultCode.ERROR_TRY_AGAIN);
                        checkResponse.setDescription("В ответ пришел нуль");
                    }
                } catch (Exception ex) {
                    debug("Ошибка при создании обработчика");
                    ex.printStackTrace();
                    checkResponse.setResultCode(ResultCode.INTERNAL_ERROR);
                }
                break;
            }
            case PaymentOrder.ORDER_STATUS_NOT_PAID_AND_REJECTED_BY_EMARKETPLACE: {
                checkResponse.setResultCode(ResultCode.ORDER_NOT_ACTUAL);
                break;
            }
            case PaymentOrder.ORDER_STATUS_PAID_AND_COMPLETED: {
                checkResponse.setResultCode(ResultCode.ORDER_ALREADY_PAID);
                break;
            }
            case PaymentOrder.ORDER_STATUS_PAID_BUT_NOT_COMPLETED_AND_STILL_PROCESSING: {
                checkResponse.setResultCode(ResultCode.ORDER_ALREADY_PAID);
                break;
            }
            case PaymentOrder.ORDER_STATUS_PAID_BUT_REJECTED_BY_EMARKETPLACE: {
                checkResponse.setResultCode(ResultCode.ORDER_ALREADY_PAID);
                break;
            }
            case PaymentOrder.ORDER_STATUS_UNDEFINED: {
                checkResponse.setResultCode(ResultCode.ERROR_TRY_AGAIN);
                break;
            }
            default: {
                checkResponse.setResultCode(ResultCode.UNKNOWN_CODE_RETURNED_BY_EMARKEPLACE);
                break;
            }
        }
        return checkResponse;
    }

    public void debug(String msg) {
        Logger.getLogger(MainPaymentHandler.class.getName()).log(Level.SEVERE, msg);
    }

    public void info(String msg) {
        Logger.getLogger(MainPaymentHandler.class.getName()).log(Level.INFO, msg);
    }
}
