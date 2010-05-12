/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsc.moneta.action.webmoney;

import com.opensymphony.xwork2.Action;
import com.rsc.moneta.Currency;
import com.rsc.moneta.action.BaseAction;
import com.rsc.moneta.bean.PaymentOrder;
import com.rsc.moneta.dao.PaymentOrderDao;
import com.rsc.moneta.module.CheckResponse;
import com.rsc.moneta.module.MainPaymentHandler;
import com.rsc.moneta.module.ResultCode;

/**
 *
 * @author sulic
 */
public class Pay extends BaseAction {
    /*   private Long paymentOrderId;
    private Integer paymentSystemId;*/

    // WEBMONEY
    private Double LMI_PAYMENT_AMOUNT;
    private String LMI_PAYMENT_DESC;
    private Long LMI_PAYMENT_NO;
    private String LMI_PAYEE_PURSE;
    private Integer LMI_SIM_MODE;
    private String message;

    @Override
    public String execute() throws Exception {
        if (LMI_PAYMENT_NO == null) {
            message = "invalid_payment_number";
            /*char ch = LMI_PAYEE_PURSE.charAt(0);
            if (ch == 'R'){
                new PaymentOrderDao(em).addUserAccountBalance(null, ch);
            }else if(ch == 'Z'){

            }else if(ch == 'E'){

            }*/
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
        return super.execute();
    }
}
