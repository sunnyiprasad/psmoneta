/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsc.moneta.dao;

import com.rsc.moneta.bean.PaymentOrder;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author sulic
 */
public class PaymentOrderDao extends Dao {

    public PaymentOrderDao(EntityManager em) {
        super(em);
    }

    public PaymentOrder getPaymentOrder(String transactionId, long marketId) {
        try {
            Query q = em.createQuery("select p from PaymentOrder p where p.transactionId=:txid and p.market.id=:mid");
            q.setParameter("txid", transactionId);
            q.setParameter("mid", marketId);
            return (PaymentOrder) q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    public PaymentOrder getPaymentOrderById(long id) {
        try {
            Query q = em.createQuery("select p from PaymentOrder p where p.id=:id");
            q.setParameter("id", id);
            return (PaymentOrder) q.getSingleResult();
        } catch (NoResultException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void processOrderPay(PaymentOrder order) {
        em.getTransaction().begin();
        try {
            try {
                Query q = em.createQuery("update Account a set a.balance=a.balance+:amount where id=:id");
                q.setParameter("id", order.getAccount().getId());
                q.setParameter("amount", order.getAmount());
                q.executeUpdate();
                order.setStatus(PaymentOrder.ORDER_STATUS_PAID_AND_COMPLETED);
                em.persist(order);
                em.getTransaction().commit();
            } catch (Exception e) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
            }
        } finally {
            em.close();
        }
    }

    public void addBalance(long accountId, double amount) {
        // Просто уменьшаем баланс пользователя.
        Query q = em.createQuery("update Account a set a.balance=a.balance+:amount where id=:id");
        q.setParameter("id", accountId);
        q.setParameter("amount", amount);
        q.executeUpdate();
    }

    public void subBalance(long accountId, double amount) {
        // Просто пополняем баланс пользователя.
        Query q = em.createQuery("update Account a set a.balance=a.balance-:amount where id=:id");
        q.setParameter("id", accountId);
        q.setParameter("amount", amount);
        q.executeUpdate();
    }

    public void addUserAccountBalance(long accountId, double amount) {
        em.getTransaction().begin();
        try {
            try {
                addBalance(accountId, amount);
                em.getTransaction().commit();
            } catch (Exception e) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
            }
        } finally {
            em.close();
        }
    }

    public void processOrderPayWithOddMoney(PaymentOrder order, double oddMoney) {
        em.getTransaction().begin();
        try {
            try {
                // Проводим платеж, баланс владельца магазина увеличивается
                addBalance(order.getAccount().getId(), order.getAmount());
                // Проводим сдачу, кидаем её на счет заказавщего
                addBalance(order.getUser().getAccount(order.getCurrency()).getId(), oddMoney);
                //задаем статус
                order.setStatus(PaymentOrder.ORDER_STATUS_PAID_AND_COMPLETED);
                em.persist(order);
                em.getTransaction().commit();
            } catch (Exception e) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
            }
        } finally {
            em.close();
        }
    }

    public void processOrderFromBalance(PaymentOrder order, double amount) {
        em.getTransaction().begin();
        try {
            try {
                // Просто пополняем баланс пользователя.
                addBalance(order.getUser().getAccount(order.getCurrency()).getId(), amount);
                // Проводим платеж, баланс владельца магазина увеличивается
                addBalance(order.getAccount().getId(), order.getAmount());
                // Проводим платеж списывая деньги со счета заказавщего
                subBalance(order.getUser().getAccount(order.getCurrency()).getId(), order.getAmount());
                //задаем статус
                order.setStatus(PaymentOrder.ORDER_STATUS_PAID_AND_COMPLETED);
                em.persist(order);
                em.getTransaction().commit();
            } catch (Exception e) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
            }
        } finally {
            em.close();
        }
    }
}
