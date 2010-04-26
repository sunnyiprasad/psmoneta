/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsc.moneta.scheduller;

import com.rsc.moneta.bean.PaymentTransaction;
import com.rsc.moneta.dao.Dao;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author sulic
 */
public class PaymentTransactionDao extends Dao{

    public PaymentTransactionDao(EntityManager em) {
        super(em);
    }

    List<PaymentTransaction> getPaymentTransaction(Date time, Date time0) {
        return null;
    }
    

}
