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
public interface OutputHandler {
    CheckResponse check(PaymentOrder key);
    CheckResponse pay(PaymentOrder key);
    int convertForeignCodeToBase(int code)throws UnknownStatusException;    
    CheckResponseReturnCodes convertEmarketplaceReturnCodeToTLSMReturnCode(int emarketplaceReturnCode) throws UnknownStatusException;
}
