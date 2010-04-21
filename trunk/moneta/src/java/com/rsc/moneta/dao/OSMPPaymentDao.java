/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsc.moneta.dao;

import com.rsc.moneta.bean.OSMPPayment;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author Солодовников Д.А.
 */
public class OSMPPaymentDao extends Dao{
    public OSMPPaymentDao(EntityManager em) {
        super(em);
    }

    public OSMPPayment getPaymentByTransactionIdAndPaymentSystemId(String transactionId, short paymentSystemId) {
        try {
            Query q = em.createQuery("select p from OSMPPayment p where p.transactionId=:txid and p.paymentSystemId=:pid");
            q.setParameter("txid", transactionId);
            q.setParameter("pid", paymentSystemId);
            return (OSMPPayment) q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }
}
