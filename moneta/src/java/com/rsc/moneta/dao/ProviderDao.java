/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsc.moneta.dao;


import com.rsc.moneta.module.cyberplat.Provider;
import java.util.Collection;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author sulic
 */
public class ProviderDao extends Dao{   

    public ProviderDao(EntityManager em) {
        super(em);
    }

    public Provider getProviderByName(String name) {
        Query q = getEntityManager().createQuery("select u from Provider u where u.name=:name");
        try {
            q.setParameter("name", name);
            return (Provider) q.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public Collection<Provider> getAllProvider() {
        Query q = getEntityManager().createQuery("select p from Provider p");
        try {
            return q.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
}
