package com.rsc.moneta.test.bean;

import com.rsc.moneta.dao.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class TestDao {

    protected EntityManager em;

    public TestDao(EntityManager em) {
        this.em = em;
    }

    public EntityManager getEntityManager() {
        return em;
    }

    public boolean persist(Object _obj) {
        EntityManager emf = getEntityManager();
        EntityTransaction tx = emf.getTransaction();
        tx.begin();
        try {
            emf.persist(_obj);
            tx.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            if (tx.isActive()) {
                tx.rollback();
            }
            return false;
        }
    }

    public boolean remove(Object _obj) {
        EntityManager emf = getEntityManager();
        EntityTransaction tx = emf.getTransaction();
        try {
            tx.begin();
            emf.remove(_obj);
            tx.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            if (tx.isActive()) {
                tx.rollback();
            }
            return false;
        }
    }
}
