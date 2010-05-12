/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsc.moneta.listener;

import com.rsc.moneta.Config;
import com.rsc.moneta.core.SmsProcessor;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *
 * @author sulic
 */
public class MainAppListener implements ServletContextListener {

    SmsProcessor processor = new SmsProcessor();

    public void contextInitialized(ServletContextEvent sce) {
        try {
           /* FileOutputStream f = new FileOutputStream("stdout.log");
            System.setErr(new PrintStream(f));
            System.setOut(new PrintStream(f));*/
        } catch (Exception e) {
            Logger.getLogger(MainAppListener.class.getName()).log(Level.SEVERE, e.toString()+"\n"+e.getMessage());
        }
        Thread thread = new Thread(processor);
        //thread.start();
        Logger.getLogger(MainAppListener.class.getName()).log(Level.SEVERE, "MainAppListener started");
        String countString = sce.getServletContext().getInitParameter("OutputHandlerCount");
        if (countString != null) {
            Logger.getLogger(MainAppListener.class.getName()).log(Level.SEVERE, "Found " + countString + " output handlers");
            int count = Integer.parseInt(countString);
            for (int i = 0; i < count; i++) {
                try {
                    String idString = sce.getServletContext().getInitParameter("OutputHandlerId." + i);
                    if (idString != null) {
                        Logger.getLogger(MainAppListener.class.getName()).log(Level.SEVERE, "Output handler id: " + idString);
                        int id = Integer.parseInt(idString);
                        String idClass = sce.getServletContext().getInitParameter("OutputHandlerClass." + i);
                        if (idClass != null) {
                            Logger.getLogger(MainAppListener.class.getName()).log(Level.SEVERE, "Output handler class: " + idClass);
                            Object obj = this.getClass().getClassLoader().loadClass(idClass).newInstance();
                            if (obj != null) {
                                Config.addOutputHandler(id, idClass);
                            }
                        }
                    } else {
                        Logger.getLogger(MainAppListener.class.getName()).log(Level.SEVERE, "OutputHandlerId." + i + " not found");
                    }
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(MainAppListener.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NumberFormatException ex) {
                    Logger.getLogger(MainAppListener.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InstantiationException ex) {
                    Logger.getLogger(MainAppListener.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(MainAppListener.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            Logger.getLogger(MainAppListener.class.getName()).log(Level.SEVERE, "output handlers count not found");
        }
        Config.setWebmoneyEURO(sce.getServletContext().getInitParameter("webmoney.euro.account"));
        Config.setWebmoneyUSD(sce.getServletContext().getInitParameter("webmoney.usd.account"));
        Config.setWebmoneyRUB(sce.getServletContext().getInitParameter("webmoney.rub.account"));


    }

    public void contextDestroyed(ServletContextEvent sce) {
        //processor.stop();
    }
}
