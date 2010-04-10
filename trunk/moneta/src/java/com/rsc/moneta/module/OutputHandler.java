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
public interface OutputHandler {
    CheckResponse check(PaymentKey key);
    CheckResponse pay(PaymentKey key);
    int convertForeignCodeToBase(int code)throws UnknownStatusException;    
    CheckResponseReturnCodes convertEmarketplaceReturnCodeToTLSMReturnCode(int emarketplaceReturnCode) throws UnknownStatusException;
}
