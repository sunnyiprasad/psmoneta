/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsc.moneta.dao;

import com.rsc.moneta.action.Const;
import com.rsc.moneta.action.admin.SumAndCount;
import com.rsc.moneta.bean.Account;
import com.rsc.moneta.bean.PaymentOrder;
import com.rsc.moneta.bean.PaymentOrderStatus;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Vector;
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
            Query q = em.createQuery("update Account a set a.balance=a.balance+:amount where id=:id");
            q.setParameter("id", order.getAccount().getId());
            q.setParameter("amount", order.getAmount());
            q.executeUpdate();
            em.refresh(order.getAccount());
            order.setStatus(PaymentOrderStatus.ORDER_STATUS_PAID_AND_COMPLETED);
            em.persist(order);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
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

    public void addUserAccountBalance(Account account, double amount) {
        em.getTransaction().begin();
        try {
            addBalance(account.getId(), amount);
            em.refresh(account);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        }
    }

    public void processOrderPayWithOddMoney(PaymentOrder order, double oddMoney) {
        em.getTransaction().begin();
        try {
            // Проводим платеж, баланс владельца магазина увеличивается
            addBalance(order.getAccount().getId(), order.getAmount());
            // Проводим сдачу, кидаем её на счет заказавщего
            addBalance(order.getUser().getAccount(order.getCurrency()).getId(), oddMoney);
            //задаем статус
            order.setStatus(PaymentOrderStatus.ORDER_STATUS_PAID_AND_COMPLETED);
            em.refresh(order.getAccount());
            em.refresh(order.getUser().getAccount(order.getCurrency()));
            em.persist(order);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        }
    }

    public void processOrderFromBalance(PaymentOrder order, double amount) {
        em.getTransaction().begin();
        try {
            // Просто пополняем баланс пользователя.
            addBalance(order.getUser().getAccount(order.getCurrency()).getId(), amount);
            // Проводим платеж, баланс владельца магазина увеличивается
            addBalance(order.getAccount().getId(), order.getAmount());
            // Проводим платеж списывая деньги со счета заказавщего
            subBalance(order.getUser().getAccount(order.getCurrency()).getId(), order.getAmount());
            //задаем статус
            order.setStatus(PaymentOrderStatus.ORDER_STATUS_PAID_AND_COMPLETED);
            em.refresh(order.getAccount());
            em.refresh(order.getUser().getAccount(order.getCurrency()));
            em.persist(order);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        }
    }

    public SumAndCount getPaymentOrdersCountAndSumFilterByStatus(Date startDate, Date endDate, int status) {
        Query q = em.createQuery("select count(c), sum(c.amount) from PaymentOrder c where c.date >= :stdt and c.date<=:endt and status=:status");
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

    public Collection<PaymentOrder> getPaymentOrdersPageFilterByStatus(int page, Date startDate, Date endDate, int status) {
        Query q = em.createQuery("select c from PaymentOrder c where c.date >= :stdt and c.date<=:endt and c.status = :status");
        try {
            q.setFirstResult(page * Const.ROWS_COUNT);
            q.setMaxResults(Const.ROWS_COUNT);
            q.setParameter("stdt", startDate);
            q.setParameter("endt", endDate);
            q.setParameter("status", status);
            return (Collection<PaymentOrder>) q.getResultList();
        } catch (NoResultException exception) {
            exception.printStackTrace();
            return new Vector<PaymentOrder>();
        }
    }

    public SumAndCount getPaymentOrdersCountAndSumFilterByStatusAndUser(Date startDate, Date endDate, int status, long userId) {
        Query q = em.createQuery("select count(c), sum(c.amount) from PaymentOrder c where c.date >= :stdt and c.date<=:endt and status=:status and c.user.id=:uid");
        try {
            q.setParameter("stdt", startDate);
            q.setParameter("endt", endDate);
            q.setParameter("status", status);
            q.setParameter("uid", userId);
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

    public Collection<PaymentOrder> getPaymentOrdersPageFilterByStatusAndUser(int page, Date startDate, Date endDate, int status, long userId) {
        Query q = em.createQuery("select c from PaymentOrder c where c.date >= :stdt and c.date<=:endt and c.status = :status and c.user.id=:uid");
        try {
            q.setFirstResult(page * Const.ROWS_COUNT);
            q.setMaxResults(Const.ROWS_COUNT);
            q.setParameter("stdt", startDate);
            q.setParameter("endt", endDate);
            q.setParameter("status", status);
            q.setParameter("uid", userId);
            return (Collection<PaymentOrder>) q.getResultList();
        } catch (NoResultException exception) {
            exception.printStackTrace();
            return new Vector<PaymentOrder>();
        }
    }

    public List<PaymentOrder> getAllPaymentOrderFilterByStatus(int status) {
        Query q = em.createQuery("select c from PaymentOrder c where c.status = :status");
        try {
            q.setParameter("status", status);
            return q.getResultList();
        } catch (NoResultException exception) {
            exception.printStackTrace();
            return new Vector<PaymentOrder>();
        }
    }

    public SumAndCount getPaymentOrdersCountAndSumFilterByUser(Date startDate, Date endDate, long userId) {
        Query q = em.createQuery("select count(c), sum(c.amount) from PaymentOrder c where c.date >= :stdt and c.date<=:endt and c.user.id=:uid");
        try {
            q.setParameter("stdt", startDate);
            q.setParameter("endt", endDate);
            q.setParameter("uid", userId);
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

    public Collection<PaymentOrder> getPaymentOrdersPageFilterByUser(int page, Date startDate, Date endDate, Long userId) {
        Query q = em.createQuery("select c from PaymentOrder c where c.date >= :stdt and c.date<=:endt and c.user.id=:uid");
        try {
            q.setFirstResult(page * Const.ROWS_COUNT);
            q.setMaxResults(Const.ROWS_COUNT);
            q.setParameter("stdt", startDate);
            q.setParameter("endt", endDate);
            q.setParameter("uid", userId);
            return (Collection<PaymentOrder>) q.getResultList();
        } catch (NoResultException exception) {
            exception.printStackTrace();
            return new Vector<PaymentOrder>();
        }
    }
}
