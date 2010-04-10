/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsc.moneta.dao;

import com.rsc.moneta.bean.Account;
import com.rsc.moneta.bean.Market;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

/**
 *
 * @author sulic
 */
public class MarketDao extends Dao{

    public MarketDao(EntityManager em) {
        super(em);
    }

    public Market getMarketByName(String name) {
        Query q = getEntityManager().createQuery("select u from Market u where u.name=:name");
        try {
            q.setParameter("name", name);
            return (Market) q.getSingleResult();
        } catch (NoResultException e1) {
            return null;
        } catch (NonUniqueResultException e2) {
            return null;
        }
    }

    public boolean isMarketHaveAccount(int type, Market market) {
        for (Account account : market.getAccounts()) {
            if (account.getType() == type){
                return true;
            }
        }
        return false;
    }

}
