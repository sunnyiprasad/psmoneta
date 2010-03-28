/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsc.moneta.listener;

import com.rsc.moneta.core.SmsProcessor;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *
 * @author sulic
 */
public class MainAppListener implements ServletContextListener{

    SmsProcessor processor = new SmsProcessor();

    public void contextInitialized(ServletContextEvent sce) {
        Thread thread = new Thread(processor);
        thread.start();
    }

    public void contextDestroyed(ServletContextEvent sce) {
        processor.stop();
    }

}
