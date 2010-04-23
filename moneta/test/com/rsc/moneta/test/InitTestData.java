package com.rsc.moneta.test;

import com.rsc.moneta.Currency;
import com.rsc.moneta.dao.Dao;
import com.rsc.moneta.dao.EMF;
import com.rsc.moneta.bean.Market;
import com.rsc.moneta.bean.User;
import com.rsc.moneta.dao.MarketDao;
import com.rsc.moneta.dao.UserDao;
import com.rsc.moneta.bean.PaymentOrder;
import com.rsc.moneta.bean.OSMPPayment;
import com.rsc.moneta.bean.PaymentOrderStatus;
import com.rsc.moneta.dao.OSMPPaymentDao;


import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;
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
        try {
            EntityManager em = EMF.getEntityManager();
            if (new UserDao(em).getUserByEmail("suleyman.batyrov@gmail.com") == null) {
                new UserDao(em).createUserAndSendNotify("+79882970412", "Administrator", "suleyman.batyrov@gmail.com");
                User u = new UserDao(em).getUserByEmail("suleyman.batyrov@gmail.com");
                Assert.assertNotNull(u);
            }
            if (new UserDao(em).getUserByEmail("suleyman.batyrov@gmail.com") == null) {
                new UserDao(em).createUserAndSendNotify("+79882970413", "TestMarket", "sbatyrov@yandex.ru");
                User u = new UserDao(em).getUserByEmail("suleyman.batyrov@gmail.com");
                Assert.assertNotNull(u);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateMarket() throws Exception {
        try {
            TestConf.initConfig();
            EntityManager em = EMF.getEntityManager();
            if (new MarketDao(em).getMarketByName("test") == null) {
                Market market = new Market();
                market.setName("test");
                Assert.assertNotNull(new UserDao(em).getUserByPhone("+79882970413"));
                market.setUser(new UserDao(em).getUserByPhone("+79882970413"));
                market.setCheckUrl("http://localhost:8084/testIM/Handler");
                market.setFailUrl("http://localhost:8084/testIM/fail.jsp");
                market.setPayUrl("http://localhost:8084/testIM/Handler");
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
                Assert.assertNotNull(new UserDao(em).getUserByPhone("+79882970413"));
                market.setUser(new UserDao(em).getUserByPhone("+79882970413"));
                new Dao(em).persist(market);
                Vector vec = new Vector();
                vec.addAll(market.getUser().getAccounts());
                market.setAccounts(vec);
                new Dao(em).persist(market);
            }
            em.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateOSMPPayment() throws Exception {
        try {
            EntityManager em = EMF.getEntityManager();

            // 1. Создать тестовый ИМ-н, записать его в БД
            Market market = new MarketDao(em).getMarketByName("testMarket1");
            if (market == null) {
                market = new Market();
                market.setName("testMarket1");
                Assert.assertNotNull(new UserDao(em).getUserByPhone("+79882970413"));
                market.setUser(new UserDao(em).getUserByPhone("+79882970413"));
                market.setCheckUrl("http://localhost:8084/testIM/Handler");
                market.setFailUrl("http://localhost:8084/testIM/fail.jsp");
                market.setPayUrl("http://localhost:8084/testIM/Handler");
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
            paymentOrder.setStatus(PaymentOrderStatus.ORDER_STATUS_ACCEPTED);
            paymentOrder.setTest(Boolean.TRUE);
            paymentOrder.setMarket(market);
            paymentOrder.setAmount(0.0);
            paymentOrder.setDate(new Date());
            paymentOrder.setAccount(market.getAccount(Currency.RUB));
            new Dao(em).persist(paymentOrder);

            // 3. Создать запись об ОСМП-платеже в т-це OSMPPayment
            short paymentSystemId = 1;
            if (new OSMPPaymentDao(em).getPaymentByTransactionIdAndPaymentSystemId(123456789, paymentSystemId) == null) {
                OSMPPayment payment = new OSMPPayment();
                payment.setAmount(9999.99);
                payment.setPayDate(new GregorianCalendar(2010, 3, 17, 8, 50, 40).getTime());
                payment.setPaymentOrder(paymentOrder);
//        payment.setPaymentOrderId(paymentOrder.getId());
                // TODO1: Денис - должен браться из конфигурационных файлов

                payment.setPaymentSystemId(paymentSystemId);
                payment.setTransactionId(123456789);
                new Dao(em).persist(payment);
            }
            em.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
