/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsc.moneta.dao;

import com.rsc.moneta.bean.Account;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

/**
 *
 * @author Солодовников Д.А.
 */
public class OSMPPaymentDao extends Dao{
    public OSMPPaymentDao(EntityManager em) {
        super(em);
    }
}
