/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import example.Dao;
import example.EMF;
import example.Order;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import org.junit.Test;

/**
 *
 * @author sulic
 */
public class AddOrderToTlsm {

    @Test
    public void testAddOrderToTlsm() throws MalformedURLException, IOException {
        EntityManager em = EMF.getEntityManager();
        List<Order> orders = new Dao(em).getActiveOrders();
        int i = 0;
        for (Order order : orders) {
            i++;
            String query = "http://localhost:8084/moneta/Assistant?MNT_ID=10";
            query += "&MNT_TRANSACTION_ID="+order.getId();
            query += "&MNT_CURRENCY_CODE=RUB";
            query += "&MNT_AMOUNT="+order.getAmount();
            query += "&contact.email=test"+i+"@test.com";
            query += "&contact.phone=000000"+i;
            query += "&contact.name=name"+i;
            URLConnection url = new URL(query).openConnection();
            url.setDoOutput(true);
            url.setDoInput(true);
            InputStream in = url.getInputStream();
            while(in.available()>0){
                System.out.print((char)in.read());
            }
        }

    }
}
