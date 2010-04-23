/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsc.moneta.module;

import com.rsc.moneta.Config;
import com.rsc.moneta.bean.PaymentOrder;
import com.rsc.moneta.bean.PaymentOrderStatus;
import com.rsc.moneta.dao.Dao;
import com.rsc.moneta.dao.PaymentOrderDao;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

/**
 *
 * @author sulic
 */
public class MainPaymentHandler {

    private EntityManager em;

    public MainPaymentHandler(EntityManager em) {
        this.em = em;
    }

    public CheckResponse check(PaymentOrder order) {
        CheckResponse checkResponse = new CheckResponse();
        switch (order.getStatus()) {
            case PaymentOrderStatus.ORDER_STATUS_ACCEPTED: {
                try {
                    OutputHandler outputHandler = new Config().buildOutputHandler(order.getMarket().getOutputHandlerType());
                    CheckResponse response = outputHandler.check(order);
                    if (response != null) {
                        if (response.getResultCode() == ResultCode.ORDER_NOT_ACTUAL ||
                                response.getResultCode() == ResultCode.ORDER_NOT_FOUND_IN_EMARKETPLACE) {
                            order.setStatus(PaymentOrderStatus.ORDER_STATUS_NOT_PAID_AND_REJECTED_BY_EMARKETPLACE);
                            new Dao(em).persist(order);
                            checkResponse.setResultCode(response.getResultCode());
                            checkResponse.setDescription(response.getDescription());
                        } else {
                            if (response.getResultCode() == ResultCode.SUCCESS_WITH_AMOUNT)
                                checkResponse.setAmount(response.getAmount());
                            else
                                checkResponse.setAmount(order.getAmount());
                            checkResponse.setResultCode(response.getResultCode());
                            checkResponse.setDescription(response.getDescription());
                        }
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
            case PaymentOrderStatus.ORDER_STATUS_NOT_PAID_AND_REJECTED_BY_EMARKETPLACE: {
                checkResponse.setResultCode(ResultCode.ORDER_NOT_ACTUAL);
                break;
            }
            case PaymentOrderStatus.ORDER_STATUS_PAID_AND_COMPLETED: {
                checkResponse.setResultCode(ResultCode.ORDER_ALREADY_PAID);
                break;
            }
            case PaymentOrderStatus.ORDER_STATUS_PAID_BUT_NOT_COMPLETED_AND_STILL_PROCESSING: {
                checkResponse.setResultCode(ResultCode.ORDER_ALREADY_PAID);
                break;
            }
            case PaymentOrderStatus.ORDER_STATUS_PAID_BUT_REJECTED_BY_EMARKETPLACE: {
                checkResponse.setResultCode(ResultCode.ORDER_ALREADY_PAID);
                break;
            }
            case PaymentOrderStatus.ORDER_STATUS_UNDEFINED: {
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
            case PaymentOrderStatus.ORDER_STATUS_ACCEPTED: {
                try {

                    order.setStatus(PaymentOrderStatus.ORDER_STATUS_PAID_BUT_NOT_COMPLETED_AND_STILL_PROCESSING);
                    new Dao(em).persist(order);
                    OutputHandler outputHandler = new Config().buildOutputHandler(order.getMarket().getOutputHandlerType());
                    checkResponse = outputHandler.pay(order);
                    debug(" ResultCode = " + checkResponse.getResultCode());
                    debug(" Description = " + checkResponse.getDescription());
                    if (checkResponse != null) {
                        if (checkResponse.getResultCode() == ResultCode.SUCCESS_WITHOUT_AMOUNT
                                || checkResponse.getResultCode() == ResultCode.SUCCESS_WITH_AMOUNT) {
                            if (order.getAccount() == null) {
                                order.setStatus(PaymentOrderStatus.ORDER_STATUS_PAID_AND_COMPLETED_BUT_NOT_FOUND_MARKET_ACCOUNT);
                                new Dao(em).persist(order);
                            } else {
                                //TODO: Сулик не реализована поддержка тестовых платежей. Любой платеж идет как не тестовый.
                                if (order.getAmount() == amount) {
                                    debug("Сумма совершенно одинаковая просто производим начисление на счет");
                                    new PaymentOrderDao(em).processOrderPay(order);
                                } else if (order.getAmount() < amount) {
                                    debug("Сумма больше чем нужно, поэтому сдачу начисляем на счет абонента в системе");
                                    new PaymentOrderDao(em).processOrderPayWithOddMoney(order, amount - order.getAmount());
                                } else {
                                    debug("Суммы меньше чем нужно. Проверяем есть ли деньги на счету абонента в нашей системе");
                                    if (order.getUser().getAccount(order.getCurrency()).getBalance() + amount > order.getAmount()) {
                                        debug("Денег на счету достаточно, можно проводить платеж с тех средств");
                                        new PaymentOrderDao(em).processOrderFromBalance(order, amount);
                                    } else {
                                        debug("Денег не достаточно, поэтому вся сумма остается на счету абонента в нашей системе.");
                                        new PaymentOrderDao(em).addUserAccountBalance(order.getUser().getAccount(order.getCurrency()).getId(), amount);
                                        order.setStatus(PaymentOrderStatus.ORDER_STATUS_PAID_AND_COMPLETED_BUT_MONEY_ADDED_ON_ACCOUNT_BALANCE);
                                        new Dao(em).persist(em);
                                        checkResponse.setResultCode(ResultCode.SUCCESS_BUT_AMOUNT_LESS_THAN_MUST_BE);
                                        checkResponse.setDescription("Деньги зачислены на счет абонента в нашей системе, т.к. внесенных средств недостаточно для оплаты заказа.");
                                        checkResponse.setTransactionId(order.getTransactionId());
                                        checkResponse.setMarketId(order.getMarketId());
                                    }
                                }
                            }
                        } else if (checkResponse.getResultCode() == ResultCode.ORDER_NOT_ACTUAL) {
                            order.setStatus(PaymentOrderStatus.ORDER_STATUS_PAID_BUT_REJECTED_BY_EMARKETPLACE);
                            new Dao(em).persist(order);
                            order.setStatus(PaymentOrderStatus.ORDER_STATUS_PAID_BUT_ORDER_NOT_FOUND);
                            new Dao(em).persist(order);
                        } else if (checkResponse.getResultCode() == ResultCode.ERROR_TRY_AGAIN) {
                        } else {
                            order.setStatus(PaymentOrderStatus.ORDER_STATUS_PAID_BUT_EMARKETPLACE_CANNOT_PROCESS_IT);
                            new Dao(em).persist(order);
                        }
                    } else {
                        checkResponse = new CheckResponse();
                        checkResponse.setResultCode(ResultCode.ERROR_TRY_AGAIN);
                        checkResponse.setDescription("В ответ пришел нуль");
                    }
                } catch (Exception ex) {
                    debug("Ошибка при создании обработчика "+ex.toString()+"\n"+ex.getMessage());
                    ex.printStackTrace();
                    checkResponse.setResultCode(ResultCode.INTERNAL_ERROR);
                }
                break;
            }
            case PaymentOrderStatus.ORDER_STATUS_NOT_PAID_AND_REJECTED_BY_EMARKETPLACE: {
                checkResponse.setResultCode(ResultCode.ORDER_NOT_ACTUAL);
                break;
            }
            case PaymentOrderStatus.ORDER_STATUS_PAID_AND_COMPLETED: {
                checkResponse.setResultCode(ResultCode.ORDER_ALREADY_PAID);
                break;
            }
            case PaymentOrderStatus.ORDER_STATUS_PAID_BUT_NOT_COMPLETED_AND_STILL_PROCESSING: {
                break;
            }
            case PaymentOrderStatus.ORDER_STATUS_PAID_BUT_REJECTED_BY_EMARKETPLACE: {
                checkResponse.setResultCode(ResultCode.ORDER_ALREADY_PAID);
                break;
            }
            case PaymentOrderStatus.ORDER_STATUS_UNDEFINED: {
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
