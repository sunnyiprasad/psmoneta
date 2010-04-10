/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsc.moneta.module;

import com.rsc.moneta.Config;
import com.rsc.moneta.bean.PaymentKey;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sulic
 */
public class MainPaymentHandler {

    public CheckResponse check(CheckRequest request) {
        CheckResponse checkResponse = new CheckResponse();
        PaymentKey paymentKey = request.getPaymentKey();
        if (paymentKey.getOrderStatus() == com.rsc.moneta.Const.ORDER_STATUS_UNDEFINED) {
            checkResponse.setResultCode(Const.OSMP_RETURN_CODE_ACCOUNT_NOT_FOUND);
            checkResponse.setComment(Const.STRING_ORDER_DOES_NOT_EXIST_ERROR);
        } else {
            switch (paymentKey.getOrderStatus()) {
                case com.rsc.moneta.Const.ORDER_STATUS_ACCEPTED: {
                    try {                        
                        OutputHandler outputHandler = new Config().buildOutputHandler(paymentKey.getMarket().getOutputHandlerType());
                        CheckResponse monetaResponse = outputHandler.check(paymentKey);
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
                case com.rsc.moneta.Const.ORDER_STATUS_NOT_PAID_AND_REJECTED_BY_EMARKETPLACE: {
                    // TODO: Отменен, значит выбрасываем ошибку, данный заказ не может быть оплачен.
                    checkResponse.setResultCode(ResultCode.ORDER_NOT_ACTUAL);
                    break;
                }
                case com.rsc.moneta.Const.ORDER_STATUS_PAID_AND_COMPLETED: {
                    // TODO: Заказ не может быть оплачен, т.к. он уже оплачен
                    checkResponse.setResultCode(ResultCode.ORDER_ALREADY_PAID);
                    break;
                }
                case com.rsc.moneta.Const.ORDER_STATUS_PAID_BUT_NOT_COMPLETED_AND_STILL_PROCESSING: {
                    // TODO: Заказ не может быть оплачен, т.к. он уже оплачен и в процессе обработки
                    checkResponse.setResultCode(ResultCode.ORDER_ALREADY_PAID);
                    break;
                }
                case com.rsc.moneta.Const.ORDER_STATUS_PAID_BUT_REJECTED_BY_EMARKETPLACE: {
                    // TODO: Заказ не может быть оплачен, т.к. он уже оплачен но был отказан в обработке магазином.
                    checkResponse.setResultCode(ResultCode.ORDER_ALREADY_PAID);
                    break;
                }
                case com.rsc.moneta.Const.ORDER_STATUS_UNDEFINED: {
                    // TODO: Статус заказа неизвестен попробуйте повторить запрос позже.
                    checkResponse.setResultCode(ResultCode.ERROR_TRY_AGAIN);
                    break;
                }
                default: {
                    // TODO: Статус заказа неизвестен попробуйте повторить запрос позже.
                    checkResponse.setResultCode(ResultCode.UNKNOWN_CODE);
                    break;
                }
            }
        }
        return checkResponse;
    }
}
