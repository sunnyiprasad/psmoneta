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
import com.rsc.moneta.module.inputhandler.OSMPInputHandler;
import com.rsc.moneta.test.InitTestData;
import com.rsc.moneta.test.TestConf;
import com.rsc.moneta.test.bean.TESTEMF;
import com.rsc.moneta.util.Utils;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.persistence.EntityManager;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author sulic
 */
class Response {

    String osmpTxnId;
    String account;
    Double sum;
    String comment;
    Integer result;
}

public class TestOSMPHandlerSulic {

    SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");

    @Test
    public void test() throws MalformedURLException, IOException, Exception {
        new InitTestData().testCreateUser();
        new InitTestData().testCreateMarket();
    }

    private Response parseResponse(Document doc) {
        Response response = new Response();
        NodeList nodeList = doc.getElementsByTagName("osmp_txn_id");
        if (nodeList != null) {
            if (nodeList.getLength() > 0) {
                response.osmpTxnId = nodeList.item(0).getTextContent();
            }
        }
        nodeList = doc.getElementsByTagName("account");
        if (nodeList != null) {
            if (nodeList.getLength() > 0) {
                response.account = nodeList.item(0).getTextContent();
            }
        }
        nodeList = doc.getElementsByTagName("comment");
        if (nodeList != null) {
            if (nodeList.getLength() > 0) {
                response.comment = nodeList.item(0).getTextContent();
            }
        }
        response.result = Utils.getIntValue("result", doc);
        response.sum = Utils.getDoubleValue("sum", doc);
        return response;
    }

    String getTxnId() {
        Random r = new Random();
        long txn_id = r.nextLong();
        while (txn_id < 0) {
            txn_id = r.nextLong();
        }
        return String.format("%020d", txn_id);
    }

    private Document execute(String query) throws MalformedURLException, IOException, ParserConfigurationException, SAXException {
        URLConnection url = new URL(query).openConnection();
        DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
        fac.setNamespaceAware(true);
        String xml = IOUtils.toString(url.getInputStream());
        System.out.println(xml);
        return fac.newDocumentBuilder().parse(IOUtils.toInputStream(xml));
    }

    @Test
    public void test0() throws MalformedURLException, IOException, ParserConfigurationException, SAXException {
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
            System.out.println(val);
            String query = "http://localhost:8084/moneta/Platika.html?command=check&txn_id=" + getTxnId()
                    + "&account=" + String.format("%019d", order.getId());
            Document doc = execute(query);
            Response response = parseResponse(doc);
            Assert.assertNotNull(response.result);
            Assert.assertEquals(OSMPInputHandler.OSMP_RETURN_CODE_OK, (int) response.result);
            Assert.assertTrue(response.sum > 0);
            query = "http://localhost:8084/moneta/Platika.html?command=check&txn_id=" + getTxnId()
                    + "&account=" + String.format("%019d", order.getId()) + "&sum=" + response.sum;
            doc = execute(query);
            response = parseResponse(doc);
            Assert.assertNotNull(response.result);
            Assert.assertEquals(OSMPInputHandler.OSMP_RETURN_CODE_OK, (int) response.result);
            Assert.assertTrue(response.sum > 0);
            query = "http://localhost:8084/moneta/Platika.html?command=pay&txn_id=" + getTxnId()
                    + "&account=" + String.format("%019d", order.getId()) + "&sum=" + response.sum
                    + "&txn_date=" + df.format(new Date());
            doc = execute(query);
            response = parseResponse(doc);
            Assert.assertNotNull(response);
            Assert.assertNotNull(response.result);
            Assert.assertEquals((int) response.result, OSMPInputHandler.OSMP_RETURN_CODE_OK);
            em.refresh(order.getAccount());
            Assert.assertEquals(val + order.getAmount(), order.getAccount().getBalance(), 0);
        }
    }

    @Test
    public void test1() throws MalformedURLException, IOException, ParserConfigurationException, SAXException {
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
            order.setUser(new UserDao(em).getUserByEmail("sulic@batyrov.ru"));
            new Dao(em).persist(order);
            Account a = order.getUser().getAccount(order.getCurrency());
            double val2 = a.getBalance();
            if (val2 > 0) {
                val2 = 0;
                a.setBalance(val2);
                new Dao(em).persist(a);
            }
            System.out.println(val);
            String query = "http://localhost:8084/moneta/Platika.html?command=check&txn_id=" + getTxnId()
                    + "&account=" + String.format("%019d", order.getId());
            Document doc = execute(query);
            Response response = parseResponse(doc);
            Assert.assertNotNull(response.result);
            Assert.assertEquals(OSMPInputHandler.OSMP_RETURN_CODE_OK, (int) response.result);
            Assert.assertTrue(response.sum > 0);
            query = "http://localhost:8084/moneta/Platika.html?command=check&txn_id=" + getTxnId()
                    + "&account=" + String.format("%019d", order.getId()) + "&sum=" + (response.sum - 10);
            doc = execute(query);
            response = parseResponse(doc);
            Assert.assertNotNull(response.result);
            Assert.assertEquals(OSMPInputHandler.OSMP_RETURN_CODE_OK, (int) response.result);
            Assert.assertTrue(response.sum > 0);
            query = "http://localhost:8084/moneta/Platika.html?command=pay&txn_id=" + getTxnId()
                    + "&account=" + String.format("%019d", order.getId()) + "&sum=" + (response.sum - 10)
                    + "&txn_date=" + df.format(new Date());
            doc = execute(query);
            response = parseResponse(doc);
            Assert.assertNotNull(response);
            Assert.assertNotNull(response.result);
            Assert.assertEquals((int) response.result, OSMPInputHandler.OSMP_RETURN_CODE_OK);
            em.refresh(order.getAccount());
            em.refresh(a);
            Assert.assertEquals(val, order.getAccount().getBalance(), 0);
            Assert.assertEquals(order.getAmount() - 10, order.getUser().getAccount(order.getCurrency()).getBalance(), 0);
        }
    }

    @Test
    public void test2() throws MalformedURLException, IOException, ParserConfigurationException, SAXException {
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
            order.setUser(new UserDao(em).getUserByEmail("sulic@batyrov.ru"));
            new Dao(em).persist(order);
            Account a = order.getUser().getAccount(order.getCurrency());
            double val2 = a.getBalance();
            System.out.println(val);
            String query = "http://localhost:8084/moneta/Platika.html?command=check&txn_id=" + getTxnId()
                    + "&account=" + String.format("%019d", order.getId());
            Document doc = execute(query);
            Response response = parseResponse(doc);
            Assert.assertNotNull(response.result);
            Assert.assertEquals(OSMPInputHandler.OSMP_RETURN_CODE_OK, (int) response.result);
            Assert.assertTrue(response.sum > 0);
            query = "http://localhost:8084/moneta/Platika.html?command=check&txn_id=" + getTxnId()
                    + "&account=" + String.format("%019d", order.getId()) + "&sum=" + (response.sum - 10);
            doc = execute(query);
            response = parseResponse(doc);
            Assert.assertNotNull(response.result);
            Assert.assertEquals(OSMPInputHandler.OSMP_RETURN_CODE_OK, (int) response.result);
            Assert.assertTrue(response.sum > 0);
            query = "http://localhost:8084/moneta/Platika.html?command=pay&txn_id=" + getTxnId()
                    + "&account=" + String.format("%019d", order.getId()) + "&sum=" + (response.sum - 10)
                    + "&txn_date=" + df.format(new Date());
            doc = execute(query);
            response = parseResponse(doc);
            Assert.assertNotNull(response);
            Assert.assertNotNull(response.result);
            Assert.assertEquals((int) response.result, OSMPInputHandler.OSMP_RETURN_CODE_OK);
            em.refresh(order.getAccount());
            em.refresh(a);
            if (val2 > 10) {
                Assert.assertEquals(val+order.getAmount(), order.getAccount().getBalance(), 0);
                Assert.assertEquals(val2-10, order.getUser().getAccount(order.getCurrency()).getBalance(), 0);
            } else {
                Assert.assertEquals(val, order.getAccount().getBalance(), 0);
                Assert.assertEquals(order.getAmount() - 10, order.getUser().getAccount(order.getCurrency()).getBalance(), 0);
            }
        }
    }

    @Test
    public void test3() throws MalformedURLException, IOException, ParserConfigurationException, SAXException {
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
            order.setUser(new UserDao(em).getUserByEmail("sulic@batyrov.ru"));
            new Dao(em).persist(order);
            Account a = order.getUser().getAccount(order.getCurrency());
            double val2 = a.getBalance();            
            System.out.println(val);
            String query = "http://localhost:8084/moneta/Platika.html?command=check&txn_id=" + getTxnId()
                    + "&account=" + String.format("%019d", order.getId());
            Document doc = execute(query);
            Response response = parseResponse(doc);
            Assert.assertNotNull(response.result);
            Assert.assertEquals(OSMPInputHandler.OSMP_RETURN_CODE_OK, (int) response.result);
            Assert.assertTrue(response.sum > 0);
            query = "http://localhost:8084/moneta/Platika.html?command=check&txn_id=" + getTxnId()
                    + "&account=" + String.format("%019d", order.getId()) + "&sum=" + (response.sum + 10);
            doc = execute(query);
            response = parseResponse(doc);
            Assert.assertNotNull(response.result);
            Assert.assertEquals(OSMPInputHandler.OSMP_RETURN_CODE_OK, (int) response.result);
            Assert.assertTrue(response.sum > 0);
            query = "http://localhost:8084/moneta/Platika.html?command=pay&txn_id=" + getTxnId()
                    + "&account=" + String.format("%019d", order.getId()) + "&sum=" + (response.sum + 10)
                    + "&txn_date=" + df.format(new Date());
            doc = execute(query);
            response = parseResponse(doc);
            Assert.assertNotNull(response);
            Assert.assertNotNull(response.result);
            Assert.assertEquals((int) response.result, OSMPInputHandler.OSMP_RETURN_CODE_OK);
            em.refresh(order.getAccount());
            em.refresh(a);
            Assert.assertEquals(val+order.getAmount(), order.getAccount().getBalance(), 0);
            Assert.assertEquals(val2+10, order.getUser().getAccount(order.getCurrency()).getBalance(), 0);
        }
    }

    @Test
    public void test4() throws MalformedURLException, IOException, ParserConfigurationException, SAXException {
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
            System.out.println(val);
            String query = "http://localhost:8084/moneta/Platika.html?command=check&txn_id=" + getTxnId()
                    + "&account=" + String.format("%019d", order.getId());
            Document doc = execute(query);
            Response response = parseResponse(doc);
            Assert.assertNotNull(response.result);
            Assert.assertEquals(OSMPInputHandler.OSMP_RETURN_CODE_OK, (int) response.result);
            Assert.assertTrue(response.sum > 0);
            double sum = response.sum;
            query = "http://localhost:8084/moneta/Platika.html?command=check&txn_id=" + getTxnId()
                    + "&account=" + String.format("%019d", order.getId()) + "&sum=" + (response.sum + 10);
            doc = execute(query);
            response = parseResponse(doc);
            Assert.assertNotNull(response.result);
            Assert.assertEquals(OSMPInputHandler.OSMP_RETURN_CODE_SUM_TOO_BIG, (int) response.result);
            query = "http://localhost:8084/moneta/Platika.html?command=pay&txn_id=" + getTxnId()
                    + "&account=" + String.format("%019d", order.getId()) + "&sum=" + (sum + 10)
                    + "&txn_date=" + df.format(new Date());
            doc = execute(query);
            response = parseResponse(doc);
            Assert.assertNotNull(response);
            Assert.assertNotNull(response.result);
            Assert.assertEquals(OSMPInputHandler.OSMP_RETURN_CODE_SUM_TOO_BIG, (int) response.result);
            em.refresh(order.getAccount());
            Assert.assertEquals(val, order.getAccount().getBalance(), 0);
        }
    }

    @Test
    public void test5() throws MalformedURLException, IOException, ParserConfigurationException, SAXException {
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
            System.out.println(val);
            String query = "http://localhost:8084/moneta/Platika.html?command=check&txn_id=" + getTxnId()
                    + "&account=" + String.format("%019d", order.getId());
            Document doc = execute(query);
            Response response = parseResponse(doc);
            Assert.assertNotNull(response.result);
            Assert.assertEquals(OSMPInputHandler.OSMP_RETURN_CODE_OK, (int) response.result);
            Assert.assertTrue(response.sum > 0);
            query = "http://localhost:8084/moneta/Platika.html?command=check&txn_id=" + getTxnId()
                    + "&account=" + String.format("%019d", order.getId()) + "&sum=" + (response.sum - 10);
            double sum = response.sum;
            doc = execute(query);
            response = parseResponse(doc);
            Assert.assertNotNull(response.result);
            Assert.assertEquals(OSMPInputHandler.OSMP_RETURN_CODE_SUM_TOO_SMALL, (int) response.result);

            query = "http://localhost:8084/moneta/Platika.html?command=pay&txn_id=" + getTxnId()
                    + "&account=" + String.format("%019d", order.getId()) + "&sum=" + (sum - 10)
                    + "&txn_date=" + df.format(new Date());
            doc = execute(query);
            response = parseResponse(doc);
            Assert.assertNotNull(response);
            Assert.assertNotNull(response.result);
            Assert.assertEquals(OSMPInputHandler.OSMP_RETURN_CODE_SUM_TOO_SMALL, (int) response.result);
            em.refresh(order.getAccount());
            Assert.assertEquals(val, order.getAccount().getBalance(), 0);
        }
    }

    @Test
    public void test6() throws MalformedURLException, IOException, ParserConfigurationException, SAXException {
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
            order.setUser(new UserDao(em).getUserByEmail("sulic@batyrov.ru"));
            new Dao(em).persist(order);
            Account a = order.getUser().getAccount(order.getCurrency());
            double val2 = a.getBalance();
            System.out.println(val);
            String query = "http://localhost:8084/moneta/Platika.html?command=check&txn_id=" + getTxnId()
                    + "&account=" + String.format("%019d", order.getId());
            Document doc = execute(query);
            Response response = parseResponse(doc);
            Assert.assertNotNull(response.result);
            Assert.assertEquals(OSMPInputHandler.OSMP_RETURN_CODE_OK, (int) response.result);
            Assert.assertTrue(response.sum > 0);
            query = "http://localhost:8084/moneta/Platika.html?command=check&txn_id=" + getTxnId()
                    + "&account=" + String.format("%019d", order.getId()) + "&sum=" + response.sum;
            doc = execute(query);
            response = parseResponse(doc);
            Assert.assertNotNull(response.result);
            Assert.assertEquals(OSMPInputHandler.OSMP_RETURN_CODE_OK, (int) response.result);
            Assert.assertTrue(response.sum > 0);
            query = "http://localhost:8084/moneta/Platika.html?command=pay&txn_id=" + getTxnId()
                    + "&account=" + String.format("%019d", order.getId()) + "&sum=" + response.sum
                    + "&txn_date=" + df.format(new Date());
            doc = execute(query);
            response = parseResponse(doc);
            Assert.assertNotNull(response);
            Assert.assertNotNull(response.result);
            Assert.assertEquals((int) response.result, OSMPInputHandler.OSMP_RETURN_CODE_OK);
            em.refresh(order.getAccount());
            em.refresh(a);
            Assert.assertEquals(val + order.getAmount(), order.getAccount().getBalance(), 0);
            Assert.assertEquals(val2 , order.getUser().getAccount(order.getCurrency()).getBalance(), 0);
        }
    }

    
}

