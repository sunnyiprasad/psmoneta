/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsc.moneta.core;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sulic
 */
public class SmsProcessor implements Runnable{

    private boolean flag = true;

    public void run() {
        while (flag){
            try {
                //TODO: Здесь должна быть отправка СМС из очереди
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                flag = false;
                Logger.getLogger(SmsProcessor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void stop(){
        flag = false;
    }
}
