/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsc.moneta.dao;

import com.rsc.moneta.bean.Account;
import java.util.Collection;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

/**
 *
 * @author sulic
 */
public class AccountDao extends Dao{

    public AccountDao(EntityManager em) {
        super(em);
    }

    public Collection<Account> getAccounts(long userId, int type) {
        Query q = getEntityManager().createQuery("select u from Account u where u.user.id=:uid and u.type=:type");
        try {
            q.setParameter("uid", userId);
            q.setParameter("type", type);
            return  q.getResultList();
        } catch (NoResultException e1) {
            return null;
        } catch (NonUniqueResultException e2) {
            return null;
        }
    }

}
