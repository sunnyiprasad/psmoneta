/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsc.moneta.dao;

import com.rsc.moneta.bean.PaymentKey;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author sulic
 */
public class PaymentKeyDao extends Dao {

    public PaymentKeyDao(EntityManager em) {
        super(em);
    }

    public PaymentKey getPaymentKeyByTransactionId(String transactionId) {
        try {
            Query q = em.createQuery("select p from PaymentKey p where p.transactionId=:txid");
            q.setParameter("txid", transactionId);
            return (PaymentKey)q.getSingleResult();
        } catch (NoResultException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
