/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsc.moneta;

import com.rsc.moneta.dao.EMF;
import com.rsc.moneta.dao.ProviderDao;
import com.rsc.moneta.module.OutputHandler;
import com.rsc.moneta.module.cyberplat.Provider;
import java.util.Collection;
import java.util.Hashtable;
import javax.persistence.EntityManager;

/**
 *
 * @author sulic
 */
public class Config {

    private static Hashtable<Integer, String> outputHandlers;
    private static Collection<Provider> providerList;
    private static String webmoneyEURO;
    private static String webmoneyUSD;
    private static String webmoneyRUB;

    public static String getWebmoneyEURO() {
        return webmoneyEURO;
    }

    public static void setWebmoneyEURO(String webmoneyEURO) {
        Config.webmoneyEURO = webmoneyEURO;
    }

    public static String getWebmoneyRUB() {
        return webmoneyRUB;
    }

    public static void setWebmoneyRUB(String webmoneyRUB) {
        Config.webmoneyRUB = webmoneyRUB;
    }

    public static String getWebmoneyUSD() {
        return webmoneyUSD;
    }

    public static void setWebmoneyUSD(String webmoneyUSD) {
        Config.webmoneyUSD = webmoneyUSD;
    }

    public static void addOutputHandler(int id, String outputHandler) {
        if (outputHandlers == null) {
            outputHandlers = new Hashtable<Integer, String>();
        }
        outputHandlers.put(id, outputHandler);
    }

    public static Collection<Provider> getProviderList() {
        if (providerList == null) {
            EntityManager em = EMF.getEntityManager();
            providerList = new ProviderDao(em).getAllProvider();
        }
        return providerList;
    }

    public OutputHandler buildOutputHandler(int type) throws ClassNotFoundException, InstantiationException, IllegalAccessException, OutputHandlerNotFoundException {
        String className = outputHandlers.get(type);
        if (className != null) {
            return (OutputHandler) this.getClass().getClassLoader().loadClass(className).newInstance();
        }
        throw new OutputHandlerNotFoundException(type);
    }

    public static String getWebmoneyAccount(int type) throws Exception {
        switch (type) {
            case Currency.EURO: {
                return webmoneyEURO;
            }
            case Currency.RUB: {
                return webmoneyRUB;
            }
            case Currency.USD: {
                return webmoneyUSD;
            }
            default:{
                throw new Exception("Unknown currency type");
            }
        }
    }
}
