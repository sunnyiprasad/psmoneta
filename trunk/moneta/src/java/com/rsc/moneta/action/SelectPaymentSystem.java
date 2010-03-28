/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsc.moneta.action;

import com.opensymphony.xwork2.Action;

/**
 *
 * @author sulic
 */
public class SelectPaymentSystem extends BaseAction{
    private String phone;
    private Long paymentId;

    @Override
    public String execute() throws Exception {
        if (paymentId == null){
            addActionError(getText("payment_id_not_defined"));
            return Action.ERROR;
        }
        return "osmp";
    }
    
    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


}
