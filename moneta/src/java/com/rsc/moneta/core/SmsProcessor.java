/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsc.moneta.core;

import com.rsc.moneta.bean.Sms;
import com.rsc.moneta.dao.EMF;
import com.rsc.moneta.dao.SmsDao;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

/**
 *
 * @author sulic
 */
public class SmsProcessor implements Runnable{

    private boolean flag = true;

    public void run() {
        while (flag){
            try {
                EntityManager em = EMF.getEntityManager();
                Collection<Sms> smsList = new SmsDao(em).getNewSms();
                
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
