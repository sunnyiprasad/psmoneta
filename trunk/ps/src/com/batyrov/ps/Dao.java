package com.batyrov.ps;

import com.batyrov.ps.bean.*;

import javax.persistence.*;
import java.util.List;
import java.sql.Timestamp;

/**
 * Created by IntelliJ IDEA.
 * User: sulic
 * Date: 09.03.2008
 * Time: 16:34:02
 * To change this template use File | Settings | File Templates.
 */
public class Dao {
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("aaa");
    private EntityManager em;

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public Dao() {
        em = Dao.getEntityManager();
    }

    public Dao(EntityManager em){
        this.em = em;
    }

    public synchronized static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public User getUserBySerialKey(long serialKey){
        Query q = em.createQuery("select p from User p where p.publicKeyId = :publicKeyId");
        q.setParameter("publicKeyId", serialKey);
        try { return (User)q.getSingleResult(); } catch (NoResultException e){ return null; }
    }

    public Point getPointBySerialKey(long serialKey){
        Query q = em.createQuery("select p from Point p where p.publicKeyId = :publicKeyId");
        q.setParameter("publicKeyId", serialKey);
        try { return (Point)q.getSingleResult(); } catch (NoResultException e){ return null; }
    }

    public AbonentPayment getPayment(String session) {
        Query q = em.createQuery("select p from AbonentPayment p where p.session = :session");
        q.setParameter("session", session);
        try { return (AbonentPayment)q.getSingleResult(); } catch (NoResultException e){ return null; }
    }

    public  void setPaymentStatus(AbonentPayment pay, int status)
    {
        em.getTransaction().begin();
        try{
            pay.setStatus(status);
            em.getTransaction().commit();
        }catch(Exception e){
            em.getTransaction().rollback();
            e.printStackTrace();
        }
    }


    public boolean persist(Object obj){
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try{            
            em.persist(obj);
            tx.commit();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            if (tx.isActive())
                tx.rollback();
            return false;
        }
    }

    public  boolean remove(Object obj){
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try{
            em.remove(obj);
            tx.commit();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            if (tx.isActive())
                tx.rollback();
            return false;
        }
    }

    public DealerUser getDealerUserBySerialKey(long serialKey) {
        Query q = em.createQuery("select p from DealerUser p where p.publicKeyId = :publicKeyId");
        q.setParameter("publicKeyId", serialKey);
        try { return (DealerUser)q.getSingleResult(); } catch (NoResultException e){ return null; }
    }

    public List<DealerPayment> getDealerPayment(long dealerId, Timestamp startDate, Timestamp endDate){
        Query q = em.createQuery("select dp from DealerPayment dp where dp.date >= :startDate and dp.date <= :endDate and dp.fromDealerId = :fromDealerId");
        q.setParameter("startDate", startDate);
        q.setParameter("endDate", endDate);
        q.setParameter("fromDealerId", dealerId);
        try { return (List<DealerPayment>)q.getResultList(); } catch (NoResultException e){ return null; }              
    }

    public List<PointPayment> getPointPayment(long dealerId, Timestamp startDate, Timestamp endDate){
        Query q = em.createQuery("select dp from PointPayment dp where dp.date >= :startDate and dp.date <= :endDate and dp.dealerId=:dealerId");
        q.setParameter("startDate", startDate);
        q.setParameter("endDate", endDate);
        q.setParameter("dealerId", dealerId);
        try { return (List<PointPayment>)q.getResultList(); } catch (NoResultException e){ return null; }
    }


}
