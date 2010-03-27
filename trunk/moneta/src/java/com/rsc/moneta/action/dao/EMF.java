package com.rsc.moneta.action.dao;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.EntityManager;

/**
 * Created by IntelliJ IDEA.
 * User: admin
 * Date: 21.03.2010
 * Time: 17:58:08
 * To change this template use File | Settings | File Templates.
 */
public class EMF {
    private static EntityManagerFactory emfInstance;

    

    public static EntityManager getEntityManager() {
        if (emfInstance == null) {
            try {
                emfInstance = Persistence.createEntityManagerFactory("moneta-PU");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return emfInstance.createEntityManager();
    }
}
