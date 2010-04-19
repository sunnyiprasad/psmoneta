/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

import example.Dao;
import example.EMF;
import example.Order;
import org.junit.Test;

/**
 *
 * @author sulic
 */
public class GenerateOrder {
    @Test
    public void generateOrder(){
        for (int i=0;i<100;i++){
            Order order= new Order();
            order.setAmount(i*1000);
            order.setStatus(Order.STATUS_ACTIVE);
            new Dao(EMF.getEntityManager()).persist(order);
        }
    }

}
