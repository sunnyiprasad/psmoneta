/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsc.moneta.dao;

import com.rsc.moneta.action.Const;
import com.rsc.moneta.action.admin.SumAndCount;
import com.rsc.moneta.bean.Account;
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

    public void checkoutCashOrder(Account account, double amount) {
        em.getTransaction().begin();
        try {
            CashOrder cashOrder = new CashOrder();
            cashOrder.setAccount(account);
            cashOrder.setAmount(account.getBalance());
            cashOrder.setStatus(CashOrder.OPEN);
            cashOrder.setUser(account.getUser());
            cashOrder.setDate(new Date());
            em.persist(cashOrder);
            Query q = em.createQuery("update Account a set a.balance=a.balance-:amount where id=:id");
            q.setParameter("id", account.getId());
            q.setParameter("amount", cashOrder.getAmount());
            q.executeUpdate();
            em.getTransaction().commit();
        } catch (Exception exception) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            exception.printStackTrace();
        }
    }

    public boolean cancelCashOrder(long id) {
        CashOrder cashOrder = em.find(CashOrder.class, id);
        if (cashOrder == null){
            return false;
        }
        return cancelCashOrder(cashOrder);
    }

    public boolean cancelCashOrder(CashOrder cashOrder) {
        em.getTransaction().begin();
        try {
            cashOrder.setStatus(CashOrder.CANCEL);            
            em.persist(cashOrder);
            Query q = em.createQuery("update Account a set a.balance=a.balance+:amount where id=:id");
            q.setParameter("id", cashOrder.getAccount().getId());
            q.setParameter("amount", cashOrder.getAmount());
            q.executeUpdate();
            em.getTransaction().commit();
            return true;
        } catch (Exception exception) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            exception.printStackTrace();
        }
        return false;
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

    public Collection<CashOrder> getCashOrderListFilterByStatus(int i) {
        Query q = em.createQuery("select c from CashOrder c where c.status=:status");
        try {
            q.setParameter("status", i);
            return (Collection<CashOrder>) q.getResultList();
        } catch (NoResultException exception) {
            exception.printStackTrace();
            return new Vector<CashOrder>();
        }
    }

    public SumAndCount getCashOrderCountAndSumFilterByStatus(Date startDate, Date endDate, int status) {
        Query q = em.createQuery("select count(c), sum(c.amount) from CashOrder c where c.date >= :stdt and c.date<=:endt and status=:status");
        try {
            q.setParameter("stdt", startDate);
            q.setParameter("endt", endDate);
            q.setParameter("status", status);
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

    public Collection<CashOrder> getCashOrdersPageFilterByStatus(int page, Date startDate, Date endDate, int status) {
        Query q = em.createQuery("select c from CashOrder c where c.date >= :stdt and c.date<=:endt and c.status = :status");
        try {
            q.setFirstResult(page * Const.ROWS_COUNT);
            q.setMaxResults(Const.ROWS_COUNT);
            q.setParameter("stdt", startDate);
            q.setParameter("endt", endDate);
            q.setParameter("status", status);
            return (Collection<CashOrder>) q.getResultList();
        } catch (NoResultException exception) {
            exception.printStackTrace();
            return new Vector<CashOrder>();
        }
    }
}
