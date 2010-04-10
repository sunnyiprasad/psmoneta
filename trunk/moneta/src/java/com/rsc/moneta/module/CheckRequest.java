/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsc.moneta.module;

import com.rsc.moneta.bean.PaymentKey;

/**
 *
 * @author sulic
 */
public class CheckRequest {
    private PaymentKey paymentKey;

    public PaymentKey getPaymentKey() {
        return paymentKey;
    }

    public void setPaymentKey(PaymentKey paymentKey) {
        this.paymentKey = paymentKey;
    }
    
}
