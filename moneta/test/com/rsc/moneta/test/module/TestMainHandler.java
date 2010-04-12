/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsc.moneta.test.module;

import com.rsc.moneta.Config;
import com.rsc.moneta.Const;
import com.rsc.moneta.OutputHandlerNotFoundException;
import com.rsc.moneta.bean.Market;
import com.rsc.moneta.bean.PaymentOrder;
import com.rsc.moneta.bean.User;
import com.rsc.moneta.dao.Dao;
import com.rsc.moneta.dao.EMF;
import com.rsc.moneta.dao.MarketDao;
import com.rsc.moneta.dao.UserDao;
import com.rsc.moneta.module.CheckRequest;
import com.rsc.moneta.module.CheckResponse;
import com.rsc.moneta.module.MainPaymentHandler;
import com.rsc.moneta.module.OutputHandler;
import com.rsc.moneta.module.ResultCode;
import com.rsc.moneta.test.InitTestData;
import com.rsc.moneta.test.TestConf;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author sulic
 */
public class TestMainHandler {

    @Test
    public void testCheckNotExistsPaymentKeyWithInValidMarketId() throws Exception{
        TestConf.initConfig();
        EntityManager em = EMF.getEntityManager();
        PaymentOrder paymentKey = new PaymentOrder();
        paymentKey.setCurrency(Const.RUB);
        paymentKey.setTransactionId("97665487");
        paymentKey.setStatus(Const.ORDER_STATUS_ACCEPTED);
        paymentKey.setAmount(100.0);
        paymentKey.setTest(true);
        paymentKey.setDate(new Date());
        paymentKey.setDescription("");
        
        Market market = new MarketDao(em).getMarketByName("tais");
        if (market == null){
            new InitTestData().testCreateMarket();
            market = new MarketDao(em).getMarketByName("tais");
            Assert.assertNotNull(market);
        }
        paymentKey.setMarket(market);
        new Dao(em).persist(paymentKey);
        MainPaymentHandler handler = new MainPaymentHandler();
        CheckRequest request = new CheckRequest();
        request.setPaymentKey(paymentKey);
        CheckResponse checkResponse = handler.check(request);
        Assert.assertNotNull(checkResponse);
        Assert.assertEquals(ResultCode.ORDER_NOT_ACTUAL, checkResponse.getResultCode());
    }


    @Test
    public void testCheckNotExistsPaymentKeyWithValidMarketId() throws Exception{
        TestConf.initConfig();
        EntityManager em = EMF.getEntityManager();
        PaymentOrder paymentKey = new PaymentOrder();
        paymentKey.setCurrency(Const.RUB);
        paymentKey.setTransactionId("97665487");
        paymentKey.setStatus(Const.ORDER_STATUS_ACCEPTED);
        paymentKey.setAmount(100.0);
        paymentKey.setTest(true);
        paymentKey.setDate(new Date());
        paymentKey.setDescription("");

        Market market = new MarketDao(em).getMarketByName("tais2");
        if (market == null){
            new InitTestData().testCreateMarket();
            market = new MarketDao(em).getMarketByName("tais2");
            Assert.assertNotNull(market);
        }
        paymentKey.setMarket(market);
        new Dao(em).persist(paymentKey);
        MainPaymentHandler handler = new MainPaymentHandler();
        CheckRequest request = new CheckRequest();
        request.setPaymentKey(paymentKey);
        CheckResponse checkResponse = handler.check(request);
        Assert.assertNotNull(checkResponse);
        Assert.assertEquals(ResultCode.ORDER_NOT_ACTUAL, checkResponse.getResultCode());
    }

    @Test
    public void testCheckExistsPaymentKey() throws Exception{
        TestConf.initConfig();
        EntityManager em = EMF.getEntityManager();
        PaymentOrder paymentKey = new PaymentOrder();
        paymentKey.setCurrency(Const.RUB);
        paymentKey.setTransactionId("1561");
        paymentKey.setStatus(Const.ORDER_STATUS_ACCEPTED);
        paymentKey.setAmount(null);
        paymentKey.setTest(true);
        paymentKey.setDate(new Date());
        paymentKey.setDescription("");
        Market market = new MarketDao(em).getMarketByName("tais2");
        if (market == null){
            new InitTestData().testCreateMarket();
            market = new MarketDao(em).getMarketByName("tais2");
            Assert.assertNotNull(market);
        }
        paymentKey.setMarket(market);
        new Dao(em).persist(paymentKey);
        MainPaymentHandler handler = new MainPaymentHandler();
        CheckRequest request = new CheckRequest();
        request.setPaymentKey(paymentKey);
        CheckResponse checkResponse = handler.check(request);
        Assert.assertNotNull(checkResponse);
        Assert.assertEquals(ResultCode.SUCCESS_WITH_AMOUNT, checkResponse.getResultCode());
    }

    @Test
    public void testCheckExistsPaymentKeyWithAmount() throws Exception{
        TestConf.initConfig();
        EntityManager em = EMF.getEntityManager();
        PaymentOrder paymentKey = new PaymentOrder();
        paymentKey.setCurrency(Const.RUB);
        paymentKey.setTransactionId("1561");
        paymentKey.setStatus(Const.ORDER_STATUS_ACCEPTED);
        paymentKey.setAmount(16900.00);
        paymentKey.setTest(true);
        paymentKey.setDate(new Date());
        paymentKey.setDescription("");
        Market market = new MarketDao(em).getMarketByName("tais2");
        if (market == null){
            new InitTestData().testCreateMarket();
            market = new MarketDao(em).getMarketByName("tais2");
            Assert.assertNotNull(market);
        }
        paymentKey.setMarket(market);
        new Dao(em).persist(paymentKey);
        MainPaymentHandler handler = new MainPaymentHandler();
        CheckRequest request = new CheckRequest();
        request.setPaymentKey(paymentKey);
        CheckResponse checkResponse = handler.check(request);
        Assert.assertNotNull(checkResponse);
        Assert.assertEquals(ResultCode.SUCCESS_WITHOUT_AMOUNT, checkResponse.getResultCode());
    }


}
