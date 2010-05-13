/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsc.moneta.dao;

import com.rsc.moneta.bean.WebMoneyPayment;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

/**
 *
 * @author sulic
 */
public class WebMoneyPaymentDao extends Dao{

    public WebMoneyPaymentDao(EntityManager em) {
        super(em);
    }

    public WebMoneyPayment getPaymentByWmTransactionId(String wmTransactionId) {
        Query q = getEntityManager().createQuery("select p from WebMoneyPayment p where p.wmTransactionId=:txid");
        try {
            q.setParameter("txid", wmTransactionId);
            return  (WebMoneyPayment) q.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
