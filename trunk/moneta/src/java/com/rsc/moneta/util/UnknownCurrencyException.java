/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsc.moneta.util;

/**
 *
 * @author sulic
 */
public class UnknownCurrencyException extends Exception{
    private String currency;

    public UnknownCurrencyException(String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "Unknown currency type  "+currency;
    }



}
