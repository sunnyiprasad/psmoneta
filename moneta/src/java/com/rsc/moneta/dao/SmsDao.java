/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsc.moneta.dao;

import com.rsc.moneta.bean.Sms;
import java.util.Collection;
import java.util.Vector;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author sulic
 */
public class SmsDao extends Dao {

    public SmsDao(EntityManager em) {
        super(em);
    }

    public Collection<Sms> getNewSms() {
        Query q = em.createQuery("select s from Sms s where s.status = 0");
        try {
            return q.getResultList();
        } catch (NoResultException e) {
            e.printStackTrace();
            return new Vector<Sms>();
        }
        
    }
}
