/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsc.moneta.module;

import com.rsc.moneta.Config;
import com.rsc.moneta.bean.PaymentOrder;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sulic
 */
public class MainPaymentHandler {

    public CheckResponse check(CheckRequest request) {
        CheckResponse checkResponse = new CheckResponse();
        PaymentOrder paymentKey = request.getPaymentKey();
        switch (paymentKey.getStatus()) {
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
                checkResponse.setResultCode(ResultCode.ORDER_NOT_ACTUAL);
                break;
            }
            case com.rsc.moneta.Const.ORDER_STATUS_PAID_AND_COMPLETED: {
                checkResponse.setResultCode(ResultCode.ORDER_ALREADY_PAID);
                break;
            }
            case com.rsc.moneta.Const.ORDER_STATUS_PAID_BUT_NOT_COMPLETED_AND_STILL_PROCESSING: {
                checkResponse.setResultCode(ResultCode.ORDER_ALREADY_PAID);
                break;
            }
            case com.rsc.moneta.Const.ORDER_STATUS_PAID_BUT_REJECTED_BY_EMARKETPLACE: {
                checkResponse.setResultCode(ResultCode.ORDER_ALREADY_PAID);
                break;
            }
            case com.rsc.moneta.Const.ORDER_STATUS_UNDEFINED: {
                checkResponse.setResultCode(ResultCode.ERROR_TRY_AGAIN);
                break;
            }
            default: {
                checkResponse.setResultCode(ResultCode.UNKNOWN_CODE);
                break;
            }
        }
        return checkResponse;
    }

    public CheckResponse pay(PaymentOrder order) {
        CheckResponse checkResponse = new CheckResponse();
        switch (order.getStatus()) {
            case com.rsc.moneta.Const.ORDER_STATUS_ACCEPTED: {
                try {
                    OutputHandler outputHandler = new Config().buildOutputHandler(order.getMarket().getOutputHandlerType());
                    CheckResponse response = outputHandler.pay(order);
                    if (response != null) {
                        checkResponse.setResultCode(response.getResultCode());
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
                checkResponse.setResultCode(ResultCode.ORDER_NOT_ACTUAL);
                break;
            }
            case com.rsc.moneta.Const.ORDER_STATUS_PAID_AND_COMPLETED: {
                checkResponse.setResultCode(ResultCode.ORDER_ALREADY_PAID);
                break;
            }
            case com.rsc.moneta.Const.ORDER_STATUS_PAID_BUT_NOT_COMPLETED_AND_STILL_PROCESSING: {
                checkResponse.setResultCode(ResultCode.ORDER_ALREADY_PAID);
                break;
            }
            case com.rsc.moneta.Const.ORDER_STATUS_PAID_BUT_REJECTED_BY_EMARKETPLACE: {
                checkResponse.setResultCode(ResultCode.ORDER_ALREADY_PAID);
                break;
            }
            case com.rsc.moneta.Const.ORDER_STATUS_UNDEFINED: {
                checkResponse.setResultCode(ResultCode.ERROR_TRY_AGAIN);
                break;
            }
            default: {
                checkResponse.setResultCode(ResultCode.UNKNOWN_CODE);
                break;
            }
        }
        return checkResponse;
    }
}
