/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsc.moneta.test.module;

import com.rsc.moneta.bean.Account;
import com.rsc.moneta.bean.Market;
import com.rsc.moneta.bean.PaymentOrder;
import com.rsc.moneta.bean.PaymentOrderStatus;
import com.rsc.moneta.dao.Dao;
import com.rsc.moneta.dao.EMF;
import com.rsc.moneta.dao.MarketDao;
import com.rsc.moneta.dao.PaymentOrderDao;
import com.rsc.moneta.dao.UserDao;
import com.rsc.moneta.module.CheckResponse;
import com.rsc.moneta.module.MainPaymentHandler;
import com.rsc.moneta.module.ResultCode;
import com.rsc.moneta.test.InitTestData;
import com.rsc.moneta.test.TestConf;
import com.rsc.moneta.test.bean.TESTEMF;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author sulic
 */
public class TestMainHandler {

    @Test
    public void test() throws MalformedURLException, IOException, Exception {
        new InitTestData().testCreateUser();
        new InitTestData().testCreateMarket();

        test0();
        test1();
        test2();
        test3();
        test4();
        test5();

    }

    public void test0() throws MalformedURLException, IOException {
        TestConf.initConfig();
        new GenerateOrder().generateOrder();
        new AddOrderToTlsm().testAddOrderToTlsm("sulic@batyrov.ru");
        EntityManager testEm = TESTEMF.getEntityManager();
        EntityManager em = EMF.getEntityManager();
        Market market = new MarketDao(em).getMarketByName("test");
        Assert.assertNotNull(market);
        List<PaymentOrder> orderList = new PaymentOrderDao(em).getAllPaymentOrderFilterByStatus(PaymentOrderStatus.ORDER_STATUS_ACCEPTED);
        for (PaymentOrder order : orderList) {
            double val = order.getAccount().getBalance();
            System.out.println(val);
            MainPaymentHandler handler = new MainPaymentHandler(em);
            CheckResponse checkResponse = handler.check(order, order.getAmount());
            System.out.println(checkResponse.getDescription());
            Assert.assertEquals(ResultCode.SUCCESS_WITH_AMOUNT, checkResponse.getResultCode());
            handler.pay(order, order.getAmount());
            Assert.assertEquals(ResultCode.SUCCESS_WITH_AMOUNT, checkResponse.getResultCode());
            System.out.println(val + order.getAmount());
            System.out.println(order.getAccount().getBalance());
            Assert.assertEquals(PaymentOrderStatus.ORDER_STATUS_PAID_AND_COMPLETED, order.getStatus());
            Assert.assertEquals(val + order.getAmount(), order.getAccount().getBalance(), 0);
        }

    }

    public void test1() throws MalformedURLException, IOException {
        TestConf.initConfig();
        new GenerateOrder().generateOrder();
        new AddOrderToTlsm().testAddOrderToTlsm("sulic@batyrov.ru");
        EntityManager testEm = TESTEMF.getEntityManager();
        EntityManager em = EMF.getEntityManager();
        Market market = new MarketDao(em).getMarketByName("test");
        Assert.assertNotNull(market);
        List<PaymentOrder> orderList = new PaymentOrderDao(em).getAllPaymentOrderFilterByStatus(PaymentOrderStatus.ORDER_STATUS_ACCEPTED);
        for (PaymentOrder order : orderList) {
            double val = order.getAccount().getBalance();
            MainPaymentHandler handler = new MainPaymentHandler(em);
            CheckResponse checkResponse = handler.check(order, order.getAmount() - 10);
            System.out.println(checkResponse.getDescription());
            Assert.assertEquals(ResultCode.AMOUNT_LESS_THAN_MUST_BE, checkResponse.getResultCode());
            handler.pay(order, order.getAmount() - 10);
            Assert.assertEquals(ResultCode.AMOUNT_LESS_THAN_MUST_BE, checkResponse.getResultCode());
            Assert.assertEquals(val, order.getAccount().getBalance(), 0);
        }
    }

    public void test2() throws MalformedURLException, IOException {
        TestConf.initConfig();
        new GenerateOrder().generateOrder();
        new AddOrderToTlsm().testAddOrderToTlsm("sulic@batyrov.ru");
        EntityManager testEm = TESTEMF.getEntityManager();
        EntityManager em = EMF.getEntityManager();
        Market market = new MarketDao(em).getMarketByName("test");
        Assert.assertNotNull(market);
        List<PaymentOrder> orderList = new PaymentOrderDao(em).getAllPaymentOrderFilterByStatus(PaymentOrderStatus.ORDER_STATUS_ACCEPTED);
        for (PaymentOrder order : orderList) {
            double val = order.getAccount().getBalance();
            Assert.assertNull(order.getUser());
            MainPaymentHandler handler = new MainPaymentHandler(em);
            CheckResponse checkResponse = handler.check(order, order.getAmount() + 10);
            System.out.println(checkResponse.getDescription());
            Assert.assertEquals(ResultCode.AMOUNT_MORE_THAN_MUST_BE, checkResponse.getResultCode());
            handler.pay(order, order.getAmount() + 10);
            Assert.assertEquals(ResultCode.AMOUNT_MORE_THAN_MUST_BE, checkResponse.getResultCode());
            Assert.assertEquals(val, order.getAccount().getBalance(), 0);
        }

    }

    public void test3() throws MalformedURLException, IOException {
        TestConf.initConfig();
        new GenerateOrder().generateOrder();
        new AddOrderToTlsm().testAddOrderToTlsm();
        EntityManager testEm = TESTEMF.getEntityManager();
        EntityManager em = EMF.getEntityManager();
        Market market = new MarketDao(em).getMarketByName("test");
        Assert.assertNotNull(market);
        List<PaymentOrder> orderList = new PaymentOrderDao(em).getAllPaymentOrderFilterByStatus(PaymentOrderStatus.ORDER_STATUS_ACCEPTED);
        for (PaymentOrder order : orderList) {
            double val = order.getAccount().getBalance();
            order.setUser(new UserDao(em).getUserByEmail("sulic@batyrov.ru"));
            new Dao(em).persist(order);
            MainPaymentHandler handler = new MainPaymentHandler(em);
            CheckResponse checkResponse = handler.check(order, order.getAmount());
            System.out.println(checkResponse.getDescription());
            Assert.assertEquals(ResultCode.SUCCESS_WITH_AMOUNT, checkResponse.getResultCode());
            handler.pay(order, order.getAmount());
            Assert.assertEquals(ResultCode.SUCCESS_WITH_AMOUNT, checkResponse.getResultCode());
            Assert.assertEquals(val + order.getAmount(), order.getAccount().getBalance(), 0);
        }

    }

    public void test4() throws MalformedURLException, IOException {
        TestConf.initConfig();
        new GenerateOrder().generateOrder();
        new AddOrderToTlsm().testAddOrderToTlsm();
        EntityManager testEm = TESTEMF.getEntityManager();
        EntityManager em = EMF.getEntityManager();
        Market market = new MarketDao(em).getMarketByName("test");
        Assert.assertNotNull(market);
        List<PaymentOrder> orderList = new PaymentOrderDao(em).getAllPaymentOrderFilterByStatus(PaymentOrderStatus.ORDER_STATUS_ACCEPTED);
        for (PaymentOrder order : orderList) {
            double val = order.getAccount().getBalance();
            order.setUser(new UserDao(em).getUserByEmail("sulic@batyrov.ru"));
            new Dao(em).persist(order);
            double val2 = order.getUser().getAccount(order.getCurrency()).getBalance();
            if (val2 >= 10) {
                val2 = 0;
                Account a = order.getUser().getAccount(order.getCurrency());
                a.setBalance(val2);
                new Dao(em).persist(a);
                em.refresh(order.getUser());
                em.refresh(order.getUser().getAccount(order.getCurrency()));
            }
            MainPaymentHandler handler = new MainPaymentHandler(em);
            CheckResponse checkResponse = handler.check(order, order.getAmount() - 10);
            System.out.println(checkResponse.getDescription());
            Assert.assertEquals(ResultCode.SUCCESS_WITH_AMOUNT, checkResponse.getResultCode());
            handler.pay(order, order.getAmount() - 10);
            Assert.assertEquals(ResultCode.SUCCESS_WITH_AMOUNT, checkResponse.getResultCode());
            Assert.assertEquals(val, order.getAccount().getBalance(), 0);
            Assert.assertEquals(val2 + order.getAmount() - 10, order.getUser().getAccount(order.getCurrency()).getBalance(), 0);
        }
    }

    public void test5() throws MalformedURLException, IOException {
        TestConf.initConfig();
        new GenerateOrder().generateOrder();
        new AddOrderToTlsm().testAddOrderToTlsm();
        EntityManager testEm = TESTEMF.getEntityManager();
        EntityManager em = EMF.getEntityManager();
        Market market = new MarketDao(em).getMarketByName("test");
        Assert.assertNotNull(market);
        List<PaymentOrder> orderList = new PaymentOrderDao(em).getAllPaymentOrderFilterByStatus(PaymentOrderStatus.ORDER_STATUS_ACCEPTED);
        for (PaymentOrder order : orderList) {
            double val = order.getAccount().getBalance();
            order.setUser(new UserDao(em).getUserByEmail("sulic@batyrov.ru"));
            new Dao(em).persist(order);
            double val2 = order.getUser().getAccount(order.getCurrency()).getBalance();
            MainPaymentHandler handler = new MainPaymentHandler(em);
            CheckResponse checkResponse = handler.check(order, order.getAmount() + 10);
            System.out.println(checkResponse.getDescription());
            Assert.assertEquals(ResultCode.SUCCESS_WITH_AMOUNT, checkResponse.getResultCode());
            handler.pay(order, order.getAmount() + 10);
            Assert.assertEquals(ResultCode.SUCCESS_WITH_AMOUNT, checkResponse.getResultCode());
            Assert.assertEquals(val + order.getAmount(), order.getAccount().getBalance(), 0);
            Assert.assertEquals(val2 + 10, order.getUser().getAccount(order.getCurrency()).getBalance(), 0);
        }

    }

    public void test6() throws MalformedURLException, IOException {
        TestConf.initConfig();
        new GenerateOrder().generateOrder();
        new AddOrderToTlsm().testAddOrderToTlsm();
        EntityManager testEm = TESTEMF.getEntityManager();
        EntityManager em = EMF.getEntityManager();
        Market market = new MarketDao(em).getMarketByName("test");
        Assert.assertNotNull(market);
        List<PaymentOrder> orderList = new PaymentOrderDao(em).getAllPaymentOrderFilterByStatus(PaymentOrderStatus.ORDER_STATUS_ACCEPTED);
        for (PaymentOrder order : orderList) {
            double val = order.getAccount().getBalance();
            order.setUser(new UserDao(em).getUserByEmail("sulic@batyrov.ru"));
            new Dao(em).persist(order);
            double val2 = order.getUser().getAccount(order.getCurrency()).getBalance();
            MainPaymentHandler handler = new MainPaymentHandler(em);
            CheckResponse checkResponse = handler.check(order, order.getAmount() - 10);
            System.out.println(checkResponse.getDescription());
            Assert.assertEquals(ResultCode.SUCCESS_WITH_AMOUNT, checkResponse.getResultCode());
            handler.pay(order, order.getAmount() - 10);
            Assert.assertEquals(ResultCode.SUCCESS_WITH_AMOUNT, checkResponse.getResultCode());
            if (val2 + 10 < order.getAmount()) {
                Assert.assertEquals(val, order.getAccount().getBalance(), 0);
                Assert.assertEquals(val2 + order.getAmount() - 10, order.getUser().getAccount(order.getCurrency()).getBalance(), 0);
            }else{
                Assert.assertEquals(val+order.getAmount(), order.getAccount().getBalance(), 0);
                Assert.assertEquals(val2 - 10, order.getUser().getAccount(order.getCurrency()).getBalance(), 0);
            }
        }
    }
}
