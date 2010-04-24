/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsc.moneta.test.module;

import com.rsc.moneta.test.bean.Order;
import com.rsc.moneta.test.bean.TESTEMF;
import com.rsc.moneta.test.bean.TestDao;
import javax.persistence.EntityManager;
import org.junit.Test;

/**
 *
 * @author sulic
 */
public class GenerateOrder {
    @Test
    public void generateOrder(){
        EntityManager em = TESTEMF.getEntityManager();
        for (int i=1;i<3;i++){
            Order order= new Order();
            order.setAmount(i*1000);
            order.setStatus(Order.STATUS_ACTIVE);
            new TestDao(em).persist(order);
        }
    }

}
