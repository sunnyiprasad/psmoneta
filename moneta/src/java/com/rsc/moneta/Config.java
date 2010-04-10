/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsc.moneta;

import com.rsc.moneta.module.OutputHandler;
import java.util.Hashtable;
import java.util.List;

/**
 *
 * @author sulic
 */
public class Config {

    private static Hashtable<Integer, String> outputHandlers;

    public static void addOutputHandler(int id, String outputHandler) {
        if (outputHandlers == null) {
            outputHandlers = new Hashtable<Integer, String>();
        }
        outputHandlers.put(id, outputHandler);
    }

    public OutputHandler buildOutputHandler(int type) throws ClassNotFoundException, InstantiationException, IllegalAccessException, OutputHandlerNotFoundException {
        String className = outputHandlers.get(type);
        if (className != null) {
             return (OutputHandler)this.getClass().getClassLoader().loadClass(className).newInstance();
        }
        throw new OutputHandlerNotFoundException(type);
    }

    
}
