/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsc.moneta.module;

import com.rsc.moneta.bean.PaymentOrder;

/**
 *
 * @author sulic
 */
public class CheckRequest {
    private PaymentOrder paymentKey;

    public PaymentOrder getPaymentKey() {
        return paymentKey;
    }

    public void setPaymentKey(PaymentOrder paymentKey) {
        this.paymentKey = paymentKey;
    }
    
}
