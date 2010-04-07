/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsc.moneta.dao;

import com.rsc.moneta.action.Const;
import com.rsc.moneta.action.admin.SumAndCount;
import com.rsc.moneta.bean.CashOrder;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TemporalType;

/**
 *
 * @author sulic
 */
public class CashOrderDao extends Dao {

    public CashOrderDao(EntityManager em) {
        super(em);
    }

    public SumAndCount getCashOrderCountAndSum(Date startDate, Date endDate) {
        Query q = em.createQuery("select count(c), sum(c.amount) from CashOrder c where c.date >= :stdt and c.date<=:endt");
        try {
            q.setParameter("stdt", startDate);
            q.setParameter("endt", endDate);
            List list = q.getResultList();
            Object[] array = (Object[]) list.get(0);
            SumAndCount sumAndCount = new SumAndCount();
            sumAndCount.setCount((Long) array[0]);
            if (array.length == 2) {
                if (array[1] != null) {
                    sumAndCount.setAmount((Double) array[1]);
                }
            }
            return sumAndCount;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Collection<CashOrder> getCashOrdersPage(int page, Date startDate, Date endDate) {
        Query q = em.createQuery("select c from CashOrder c where c.date >= :stdt and c.date<=:endt");
        try {
            q.setFirstResult(page * Const.ROWS_COUNT);
            q.setMaxResults(Const.ROWS_COUNT);
            q.setParameter("stdt", startDate);
            q.setParameter("endt", endDate);
            return (Collection<CashOrder>) q.getResultList();
        } catch (NoResultException exception) {
            exception.printStackTrace();
            return new Vector<CashOrder>();
        }
    }
}
