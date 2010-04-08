/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsc.moneta.module;

/**
 *
 * @author sulic
 */
public class UnknownStatusException extends Exception{
    private int code;

    public UnknownStatusException(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "Unknown status : "+code;
    }



}
