/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsc.moneta;

/**
 *
 * @author sulic
 */
public class OutputHandlerNotFoundException extends Exception{

    int type;

    public OutputHandlerNotFoundException(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Cannot find output handler with id = "+type;
    }



}
