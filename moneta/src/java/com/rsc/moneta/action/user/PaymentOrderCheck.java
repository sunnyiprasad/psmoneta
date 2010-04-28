/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsc.moneta.action.user;

import com.opensymphony.xwork2.Action;
import com.rsc.moneta.action.BaseAction;
import com.rsc.moneta.bean.Account;
import com.rsc.moneta.bean.PaymentOrder;
import com.rsc.moneta.module.CheckResponse;
import com.rsc.moneta.module.MainPaymentHandler;
import com.rsc.moneta.module.ResultCode;

/**
 *
 * @author sulic
 */
public class PaymentOrderCheck extends BaseAction{

    private Long paymentOrderId;
    private Long accountId;

    @Override
    public String execute() throws Exception {
        if (paymentOrderId == null){
            addActionError(getText("payment_order_id_not_defined"));
            return Action.ERROR;
        }
        PaymentOrder paymentOrder = em.find(PaymentOrder.class, paymentOrderId);
        if (paymentOrder == null){
            addActionError(getText("payment_order_not_found"));
            return Action.ERROR;
        }
        Account account = user.getAccount(paymentOrder.getCurrency());
        accountId = account.getId();
        if (accountId == null){
            addActionError(getText("account_not_defined"));
            return Action.ERROR;
        }else{
            //Account account = em.find(Account.class, accountId);
            if (account == null) {
                addActionError(getText("account_not_found"));
                return Action.ERROR;
            } else {
                if (account.getBalance() < paymentOrder.getAmount()) {
                    addActionError(getText("you_hasnt_money"));
                    return Action.ERROR;
                } else {
                    MainPaymentHandler handler = new MainPaymentHandler(em);
                    CheckResponse response = handler.check(paymentOrder, paymentOrder.getAmount());
                    if (response == null){
                        addActionError(getText("system_error_connect_with_admin"));
                        return Action.ERROR;
                    }else{
                        if (response.getResultCode() == ResultCode.SUCCESS_WITH_AMOUNT
                                || response.getResultCode() == ResultCode.SUCCESS_WITHOUT_AMOUNT){
                            return Action.SUCCESS;
                        }else{
                            addActionError(response.getDescription());
                            return Action.ERROR;
                        }
                    }
                }
            }
        }
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getPaymentOrderId() {
        return paymentOrderId;
    }

    public void setPaymentOrderId(Long paymentOrderId) {
        this.paymentOrderId = paymentOrderId;
    }

    
}
