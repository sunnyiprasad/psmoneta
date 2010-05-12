/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsc.moneta.action.webmoney;

import com.opensymphony.xwork2.Action;
import com.rsc.moneta.action.BaseAction;
import com.rsc.moneta.bean.PaymentOrder;
import com.rsc.moneta.module.CheckResponse;
import com.rsc.moneta.module.MainPaymentHandler;
import com.rsc.moneta.module.ResultCode;
import com.rsc.moneta.util.Utils;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Entity;
import javax.persistence.EntityManager;

/**
 *
 * @author sulic
 */
public class Result extends BaseAction {

    private Integer LMI_PREREQUEST;
    private Double LMI_PAYMENT_AMOUNT;
    private String LMI_PAYMENT_DESC;
    private Long LMI_PAYMENT_NO;
    private String LMI_PAYEE_PURSE;
    private Integer LMI_MODE;
    private String LMI_SYS_TRANS_DATE;
    private String LMI_SYS_INVS_NO;
    private String LMI_SYS_TRANS_NO;
    private String LMI_SECRET_KEY;
    private String LMI_PAYER_PURSE;
    private String LMI_PAYER_WM;
    private String message;
    private String LMI_HASH;

    private boolean checkMd5(){
        try {
            String str = LMI_PAYEE_PURSE + LMI_PAYMENT_AMOUNT + LMI_PAYMENT_NO;
            if (LMI_MODE != null) {
                str += LMI_MODE;
            }
            if (LMI_SYS_INVS_NO != null) {
                str += LMI_SYS_INVS_NO;
            }
            if (LMI_SYS_TRANS_NO != null) {
                str += LMI_SYS_TRANS_NO;
            }
            if (LMI_SYS_TRANS_DATE != null) {
                str += LMI_SYS_TRANS_DATE;
            }
            if (LMI_SECRET_KEY != null) {
                str += LMI_SECRET_KEY;
            }
            if (LMI_PAYER_PURSE != null) {
                str += LMI_PAYER_PURSE;
            }
            if (LMI_PAYER_WM != null) {
                str += LMI_PAYER_WM;
            }
            String hash = Utils.getMd5InHexString(str);
            return LMI_HASH != null && LMI_HASH.equals(hash);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Result.class.getName()).log(Level.SEVERE, ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public String execute() throws Exception {
        if (LMI_PREREQUEST == null || LMI_PREREQUEST != 1) {
            if (LMI_PAYMENT_NO == null) {
                message = "invalid_payment_number";
            } else {
                PaymentOrder paymentOrder = em.find(PaymentOrder.class, LMI_PAYMENT_NO);
                if (paymentOrder == null) {
                    message = getText("payment_order_id_not_found");
                } else {
                    if (paymentOrder.getAmount() != LMI_PAYMENT_AMOUNT) {
                        message = getText("invalid_amount");
                    } else {
                        MainPaymentHandler handler = new MainPaymentHandler(em);
                        CheckResponse response = handler.pay(paymentOrder, LMI_PAYMENT_AMOUNT);
                        if (response.getResultCode() == ResultCode.SUCCESS_WITH_AMOUNT
                                || response.getResultCode() == ResultCode.SUCCESS_WITHOUT_AMOUNT) {
                            message = "yes";
                        } else {
                            message = response.getDescription() + ", result code = " + response.getResultCode();
                        }
                    }
                }
            }
        } else {
            if (LMI_PAYMENT_NO == null) {
                message = "yes";
            } else {
                PaymentOrder paymentOrder = em.find(PaymentOrder.class, LMI_PAYMENT_NO);
                if (paymentOrder == null) {
                    message = getText("payment_order_id_not_found");
                } else {
                    if (paymentOrder.getAmount() != LMI_PAYMENT_AMOUNT) {
                        message = getText("invalid_amount");
                    } else {
                        MainPaymentHandler handler = new MainPaymentHandler(em);
                        CheckResponse response = handler.check(paymentOrder, LMI_PAYMENT_AMOUNT);
                        if (response.getResultCode() == ResultCode.SUCCESS_WITH_AMOUNT
                                || response.getResultCode() == ResultCode.SUCCESS_WITHOUT_AMOUNT) {
                            message = "yes";
                        } else {
                            message = response.getDescription() + ", result code = " + response.getResultCode();
                        }
                    }
                }
            }
        }
        return super.execute();
    }
}
