/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsc.moneta.module;

import com.rsc.moneta.bean.PaymentKey;
import java.util.Map;

/**
 *
 * @author sulic
 */
public interface OutputHandler {
    CheckResponse check(PaymentKey key);
    CheckResponse pay(PaymentKey key);
    int convertForeignCodeToBase(int code)throws UnknownStatusException;
    int convertBaseCodeToForeign(int code)throws UnknownStatusException;
}
