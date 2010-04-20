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
        Thread thread = new Thread(processor);
        //thread.start();
        System.out.println("MainAppListener started");
        String countString = sce.getServletContext().getInitParameter("OutputHandlerCount");
        if (countString != null) {
            System.out.println("Found "+countString+" output handlers");
            int count = Integer.parseInt(countString);
            for (int i = 0; i < count; i++) {
                try {
                    String idString = sce.getServletContext().getInitParameter("OutputHandlerId." + i);
                    if (idString != null) {
                        System.out.println("Output handler id: "+idString);
                        int id = Integer.parseInt(idString);
                        String idClass = sce.getServletContext().getInitParameter("OutputHandlerClass." + i);
                        if (idClass != null) {
                            System.out.println("Output handler class: "+idString);
                            Object obj = this.getClass().getClassLoader().loadClass(idClass).newInstance();
                            if (obj != null){
                                Config.addOutputHandler(id, idClass);
                            }
                        }
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
        }else{
            System.out.println("output handlers count not found");
        }
    }

    public void contextDestroyed(ServletContextEvent sce) {
        //processor.stop();
    }
}
