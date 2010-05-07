/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsc.moneta.test.module;

import java.util.Map;
import java.util.Properties;
import junit.framework.Assert;
import org.junit.Test;
import javax.persistence.EntityManager;
import java.util.Vector;

import com.rsc.moneta.bean.Market;
import com.rsc.moneta.bean.PaymentOrder;
import com.rsc.moneta.bean.PaymentOrderStatus;
import com.rsc.moneta.module.inputhandler.OSMPInputHandler;
import com.rsc.moneta.module.inputhandler.Const;
import com.rsc.moneta.dao.EMF;
import com.rsc.moneta.dao.Dao;
import com.rsc.moneta.dao.MarketDao;
import com.rsc.moneta.dao.PaymentOrderDao;
import com.rsc.moneta.dao.UserDao;
import com.rsc.moneta.module.ResultCode;
import java.util.Calendar;
import java.util.Locale;
// import com.rsc.moneta.test.TestConf;

/**
 *
 * @author Солодовников Д.А.
 */
public class TestOsmpHandler {

    /*
     * Тест-метод запроса "check" - платеж уже завершён
     */
    @Test
    public void testOsmpCheckForPaidAndCompletedOrder(){
        // 1. Создать тестовый ИМ-н, записать его в БД
        EntityManager em = EMF.getEntityManager();
        Market market = new MarketDao(em).getMarketByName("test");
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

        // 2. Создать запись о заказе в т-це PaymentOrder, выставить заказу
        // статус "ORDER_STATUS_PAID_AND_COMPLETED"
        String txn_id = "1234567";
        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setStatus(PaymentOrderStatus.ORDER_STATUS_PAID_AND_COMPLETED);
        paymentOrder.setTest(Boolean.TRUE);
        paymentOrder.setDate(Calendar.getInstance().getTime());
        paymentOrder.setTransactionId(txn_id);
        paymentOrder.setMarket(market);
        new Dao(em).persist(paymentOrder);
        em.close();

        // 3. Сэмулировать тестовый запрос "check" к ОСМП-хендлеру по созданному
        // в п.1 номеру заказа
        long orderId = paymentOrder.getId();
        String account = String.format("%019d", orderId);
        OSMPInputHandler handler = new OSMPInputHandler();
        Map map = new Properties();
        map.put("command", "check");
        map.put("txn_id", txn_id);
        map.put("account", account);
        String response = handler.check(map);

        // 4. Удалить созданную в п.1 запись о заказе
        // TODO: Денис - что-то не даёт нормально удалять
        em = EMF.getEntityManager();
        paymentOrder = new PaymentOrderDao(em).getPaymentOrderById(orderId);
        new Dao(em).remove(paymentOrder);
        em.close();
        

        // 5. Сравнить полученный response с планируемым
        Assert.assertEquals("<?xml version='1.0' encoding='UTF-8'?>" + 
                "<response>" +
                    "<osmp_txn_id>" + txn_id + "</osmp_txn_id>" +
                    "<prv_txn>" + account + "</prv_txn>" +
                    "<result>" + OSMPInputHandler.OSMP_RETURN_CODE_OK  + "</result>" +
                    "<comment>" + Const.STRING_ORDER_PAID_AND_COMPLETED + "</comment>" +
                    "</response>",
                response);
    }

    /*
     * Тест-метод запроса "check" - Платеж принят и ИМ-н возвращает сумму
     * (с эмулированием кода возврата от ИМ-на)
     */
    @Test
    public void testOsmpCheckForAcceptedSucessWithAmountOrder() {
//        TestConf.initConfig();

        // Создать тестовый ИМ-н, записать его в БД
        EntityManager em = EMF.getEntityManager();
        Market market = new MarketDao(em).getMarketByName("tais");
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
//        if (market == null) {
//            market = new Market();
//            market.setName("tais");
//            market.setCheckUrl("http://195.68.159.98:50049/bitrix/components/travelshop/ibe.backoffice/callback_moneta.php");
//            market.setPayUrl("http://195.68.159.98:50049/bitrix/components/travelshop/ibe.backoffice/callback_moneta.php");
//            market.setOutputHandlerType(0);
//            market.setSignable(true);
//            market.setPassword("TravelShop23481");
//            market.setSuccessUrl("http://www.success.ru/");
//            market.setFailUrl("http://www.fail.ru/");
//            Assert.assertNotNull(new UserDao(em).getUserByPhone("test"));
//            market.setUser(new UserDao(em).getUserByPhone("test"));
//            new Dao(em).persist(market);
//            Vector vec = new Vector();
//            vec.addAll(market.getUser().getAccounts());
//            market.setAccounts(vec);
//            new Dao(em).persist(market);
//        }

        // Создать запись о заказе в т-це PaymentOrder, присвоить заказу
        // статус "ORDER_STATUS_ACCEPTED"
        PaymentOrder paymentOrder = new PaymentOrder();
        double amount = 812450.85;
        paymentOrder.setAmount(amount);
        paymentOrder.setStatus(PaymentOrderStatus.ORDER_STATUS_ACCEPTED);
        paymentOrder.setTest(Boolean.TRUE);
        paymentOrder.setMarket(market);
        new Dao(em).persist(paymentOrder);
        em.close();

        // Сэмулировать тестовый запрос "check" к ОСМП-хендлеру по созданному
        // заказу
        long paymentOrderId = paymentOrder.getId();
        String account = String.format("%019d", paymentOrderId);
        String txn_id = "1234567";
        OSMPInputHandler handler = new OSMPInputHandler();
        Map map = new Properties();
        map.put("command", "check");
        map.put("txn_id", txn_id);
        map.put("account", account);
        String response = handler.checkWithEmulating(map,
                ResultCode.SUCCESS_WITH_AMOUNT, amount);

//        System.out.println("<?xml version='1.0' encoding='UTF-8'?>"
//                + "<response>"
//                + "<osmp_txn_id>" + txn_id + "</osmp_txn_id>"
//                + "<prv_txn>" + account + "</prv_txn>"
//                + "<result>" + OSMPInputHandler.OSMP_RETURN_CODE_OK + "</result>"
//                + "<comment>" + "" + "</comment>"
//                + "</response>");
//        System.out.println(response);

        // Сравнить полученный response с планируемым
        Assert.assertEquals("<?xml version='1.0' encoding='UTF-8'?>"
                + "<response>"
                + "<osmp_txn_id>" + txn_id + "</osmp_txn_id>"
                + "<sum>" + String.format("%.2f", amount).replace(',', '.') + "</sum>"
                + "<result>" + OSMPInputHandler.OSMP_RETURN_CODE_OK + "</result>"
                + "<comment>" + "" + "</comment>"
                + "</response>",
                response);
    }

    /*
     * Тест-метод запроса "check" - Платеж принят но ИМ-н не возвращает сумму
     * (с эмулированием кода возврата от ИМ-на)
     */
    @Test
    public void testOsmpCheckForAcceptedSucessWithoutAmountOrder() {
//        TestConf.initConfig();

        // Создать тестовый ИМ-н, записать его в БД
        EntityManager em = EMF.getEntityManager();
        Market market = new MarketDao(em).getMarketByName("tais");
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

        // Создать запись о заказе в т-це PaymentOrder, присвоить заказу
        // статус "ORDER_STATUS_ACCEPTED"
        PaymentOrder paymentOrder = new PaymentOrder();
        double amount = 14891.52;
        paymentOrder.setAmount(amount);
        paymentOrder.setStatus(PaymentOrderStatus.ORDER_STATUS_ACCEPTED);
        paymentOrder.setTest(Boolean.TRUE);
        paymentOrder.setMarket(market);
        new Dao(em).persist(paymentOrder);
        em.close();

        // Сэмулировать тестовый запрос "check" к ОСМП-хендлеру по созданному
        // заказу
        long paymentOrderId = paymentOrder.getId();
        String account = String.format("%019d", paymentOrderId);
        String txn_id = "1234567";
        OSMPInputHandler handler = new OSMPInputHandler();
        Map map = new Properties();
        map.put("command", "check");
        map.put("txn_id", txn_id);
        map.put("account", account);
        String response = handler.checkWithEmulating(map,
                ResultCode.SUCCESS_WITHOUT_AMOUNT);

//        System.out.println("<?xml version='1.0' encoding='UTF-8'?>"
//                + "<response>"
//                + "<osmp_txn_id>" + txn_id + "</osmp_txn_id>"
//                + "<prv_txn>" + account + "</prv_txn>"
//                + "<result>" + OSMPInputHandler.OSMP_RETURN_CODE_OK + "</result>"
//                + "<comment>" + "" + "</comment>"
//                + "</response>");
//        System.out.println(response);

        // Сравнить полученный response с планируемым
        Assert.assertEquals("<?xml version='1.0' encoding='UTF-8'?>"
                + "<response>"
                + "<osmp_txn_id>" + txn_id + "</osmp_txn_id>"
                + "<sum>" + String.format("%.2f", amount).replace(',', '.') + "</sum>"
                + "<result>" + OSMPInputHandler.OSMP_RETURN_CODE_OK + "</result>"
                + "<comment>" + "" + "</comment>"
                + "</response>",
                response);
    }
}
