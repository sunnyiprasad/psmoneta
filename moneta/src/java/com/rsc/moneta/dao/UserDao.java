/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsc.moneta.dao;

import com.rsc.moneta.action.Const;
import com.rsc.moneta.bean.User;
import java.util.Collection;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

/**
 *
 * @author sulic
 */
public class UserDao extends Dao {

    public UserDao(EntityManager em) {
        super(em);
    }


    public User getUserByPhone(String phone) {
        Query q = getEntityManager().createQuery("select u from User u where u.phone=:phone");
        try {
            q.setParameter("phone", phone);
            return (User) q.getSingleResult();
        } catch (NoResultException e1) {
            return null;
        } catch (NonUniqueResultException e2) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public Collection<User> getAllUserList() {
        Query q = getEntityManager().createQuery("select u from User u");
        try {
            return q.getResultList();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e2) {
            return null;
        }
    }

    public User getUserByPhoneAndPassword(String phone, String password) {
        Query q = em.createQuery("select u from User u where u.phone=:phone and u.password=:pswd");
        try {
            q.setParameter("phone", phone);
            q.setParameter("pswd", password);
            return (User) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e2) {
            return null;
        }
    }

    public long getUserCount() {
        Query q = em.createQuery("select count(u) from User u");
        return (Integer) q.getSingleResult();
    }

    public Collection<User> getUsers(int page) {
        Query q = em.createQuery("select u from User u");
        q.setFirstResult(page * Const.ROWS_COUNT);
        q.setMaxResults(Const.ROWS_COUNT);
        return q.getResultList();
    }
}
