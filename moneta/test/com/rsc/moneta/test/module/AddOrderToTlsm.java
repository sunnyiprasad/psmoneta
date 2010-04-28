/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsc.moneta.test.module;

import com.rsc.moneta.bean.Market;
import com.rsc.moneta.bean.PaymentOrder;
import com.rsc.moneta.bean.User;
import com.rsc.moneta.dao.EMF;
import com.rsc.moneta.dao.MarketDao;
import com.rsc.moneta.dao.PaymentOrderDao;
import com.rsc.moneta.dao.UserDao;
import com.rsc.moneta.test.bean.Order;
import com.rsc.moneta.test.bean.TESTEMF;
import com.rsc.moneta.test.bean.TestDao;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author sulic
 */
public class AddOrderToTlsm {

    @Test
    public void testAddOrderToTlsm() throws MalformedURLException, IOException {
        EntityManager em = EMF.getEntityManager();
        EntityManager testEm = TESTEMF.getEntityManager();
        List<Order> orders = new TestDao(testEm).getActiveOrders();
        Market market = new MarketDao(em).getMarketByName("test");
        Assert.assertNotNull(market);
        int i = 0;
        for (Order order : orders) {
            i++;
            String query = "http://localhost:8084/moneta/Assistant?MNT_ID="+market.getId();
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

    public void testAddOrderToTlsm(String email) throws MalformedURLException, IOException {
        EntityManager em = EMF.getEntityManager();
        EntityManager testEm = TESTEMF.getEntityManager();
        List<Order> orders = new TestDao(testEm).getActiveOrders();
        Market market = new MarketDao(em).getMarketByName("test");
        Assert.assertNotNull(market);
        int i = 0;
        for (Order order : orders) {
            i++;
            String query = "http://localhost:8084/moneta/Assistant?MNT_ID="+market.getId();
            query += "&MNT_TRANSACTION_ID="+order.getId();
            query += "&MNT_CURRENCY_CODE=RUB";
            query += "&MNT_AMOUNT="+order.getAmount();
            query += "&contact.email="+email;
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

    @Test
    public void test(){
        EntityManager em = EMF.getEntityManager();
        UserDao userDao = new UserDao(em);
        User user = userDao.getUserByEmail("test1@test.com");
        List<PaymentOrder> paymentOrders = new PaymentOrderDao(em).getAllPaymentOrderWhereUserIsNull();
        for (PaymentOrder paymentOrder : paymentOrders) {
            paymentOrder.setUser(user);
            userDao.persist(paymentOrder);
        }
    }
}
