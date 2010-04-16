package com.rsc.moneta.test;

import com.rsc.moneta.Currency;
import com.rsc.moneta.bean.Account;
import com.rsc.moneta.dao.Dao;
import com.rsc.moneta.dao.EMF;
import com.rsc.moneta.bean.Market;
import com.rsc.moneta.bean.User;
import com.rsc.moneta.dao.AccountDao;
import com.rsc.moneta.dao.MarketDao;
import com.rsc.moneta.dao.UserDao;
import com.rsc.moneta.bean.PaymentOrder;
import com.rsc.moneta.bean.OSMPPayment;
import com.rsc.moneta.dao.OSMPPaymentDao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collection;
import java.util.Vector;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import org.junit.Assert;
import org.junit.Test;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author sulic
 */
public class InitTestData {

    @Test
    public void testCreateUser() throws MalformedURLException, IOException {
        EntityManager em = EMF.getEntityManager();
        User u = new User();
        u.setPhone("admin");
        u.setPassword("12345");
        u.setRole(User.ADMINISTRATOR);
        if (new UserDao(em).getUserByPhone("admin") == null) {
            new Dao(em).persist(u);
        }
        User user = new User();
        user.setPhone("test");
        user.setPassword("12345");
        if (new UserDao(em).getUserByPhone("test") == null) {
            new Dao(em).persist(user);
        }
        em.close();
    }

    @Test
    public void testCreateAccount() throws MalformedURLException, IOException {
        EntityManager em = EMF.getEntityManager();
        Account account = new Account();
        account.setType(Currency.RUB);
        User user = new UserDao(em).getUserByPhone("test");
        Assert.assertNotNull(user);
        Collection<Account> accounts = new AccountDao(em).getAccounts(user.getId(), Currency.RUB);
        if (accounts.size() == 1) {
            return;
        }
        account.setUser(user);
        account.setBalance(0);
        new Dao(em).persist(account);
        em.close();
    }

    @Test
    public void testCreateMarket() throws Exception {
        TestConf.initConfig();
        EntityManager em = EMF.getEntityManager();
        if (new MarketDao(em).getMarketByName("test") == null) {
            Market market = new Market();
            market.setName("test");
            Assert.assertNotNull(new UserDao(em).getUserByPhone("test"));
            market.setUser(new UserDao(em).getUserByPhone("test"));
            market.setCheckUrl("http://localhost:8084/testIM/Check");
            market.setFailUrl("http://localhost:8084/testIM/fail.jsp");
            market.setPayUrl("http://localhost:8084/testIM/Pay");
            market.setSignable(true);
            market.setSuccessUrl("http://localhost:8084/testIM/success.jsp");
            market.setPassword("12345");
            market.setOutputHandlerType(0);
            new Dao(em).persist(market);
            Vector vec = new Vector();
            vec.addAll(market.getUser().getAccounts());
            market.setAccounts(vec);
            new Dao(em).persist(market);
        }
        if (new MarketDao(em).getMarketByName("tais") == null) {
            Market market = new Market();
            market.setName("tais");
            market.setCheckUrl("http://195.68.159.98:50049/bitrix/components/travelshop/ibe.backoffice/callback_moneta.php");
            market.setPayUrl("http://195.68.159.98:50049/bitrix/components/travelshop/ibe.backoffice/callback_moneta.php");
            market.setOutputHandlerType(0);
            market.setSignable(true);
            market.setPassword("TravelShop23481");
            market.setSuccessUrl("http://www.success.ru/");
            market.setFailUrl("http://www.fail.ru/");
            Assert.assertNotNull(new UserDao(em).getUserByPhone("test"));
            market.setUser(new UserDao(em).getUserByPhone("test"));            
            new Dao(em).persist(market);
            Vector vec = new Vector();
            vec.addAll(market.getUser().getAccounts());
            market.setAccounts(vec);
            new Dao(em).persist(market);            
        }
        em.close();
    }

    @Test
    public void testCreateOSMPPayment() throws Exception {
        EntityManager em = EMF.getEntityManager();

        // 1. Создать тестовый ИМ-н, записать его в БД
        Market market = new MarketDao(em).getMarketByName("testMarket1");
        if (market == null) {
            market = new Market();
            market.setName("testMarket1");
            Assert.assertNotNull(new UserDao(em).getUserByPhone("test"));
            market.setUser(new UserDao(em).getUserByPhone("test"));
            market.setCheckUrl("http://localhost:8084/testIM/Check");
            market.setFailUrl("http://localhost:8084/testIM/fail.jsp");
            market.setPayUrl("http://localhost:8084/testIM/Pay");
            market.setSignable(true);
            market.setSuccessUrl("http://localhost:8084/testIM/success.jsp");
            market.setPassword("12345");
            market.setOutputHandlerType(0);
            new Dao(em).persist(market);
            Vector vec = new Vector();
            vec.addAll(market.getUser().getAccounts());
            market.setAccounts(vec);
            new Dao(em).persist(market);
        }

        // 2. Создать запись о заказе в т-це PaymentOrder
        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setStatus(PaymentOrder.ORDER_STATUS_ACCEPTED);
        paymentOrder.setTest(Boolean.TRUE);
        paymentOrder.setMarket(market);
        new Dao(em).persist(paymentOrder);
        
        // 3. Создать запись об ОСМП-платеже в т-це OSMPPayment
        OSMPPayment payment = new OSMPPayment();
        payment.setOSMPTxnId(123456789);
        payment.setPaymentOrderId(paymentOrder.getId());
        new Dao(em).persist(payment);

        em.close();
    }
}
