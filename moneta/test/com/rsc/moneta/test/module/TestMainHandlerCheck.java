/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsc.moneta.test.module;

import com.rsc.moneta.bean.Market;
import com.rsc.moneta.bean.PaymentOrder;
import com.rsc.moneta.dao.EMF;
import com.rsc.moneta.dao.MarketDao;
import com.rsc.moneta.dao.PaymentOrderDao;
import com.rsc.moneta.module.CheckResponse;
import com.rsc.moneta.module.MainPaymentHandler;
import com.rsc.moneta.module.ResultCode;
import com.rsc.moneta.test.InitTestData;
import com.rsc.moneta.test.TestConf;
import javax.persistence.EntityManager;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author sulic
 */
public class TestMainHandlerCheck {

    @Test
    public void testCheckNotExistsPaymentKeyWithInValidMarketId() throws Exception{
        TestConf.initConfig();        
        EntityManager em = EMF.getEntityManager();
        Market market = new MarketDao(em).getMarketByName("tais");
        if (market == null){
            new InitTestData().testCreateMarket();
            market = new MarketDao(em).getMarketByName("tais");
            Assert.assertNotNull(market);
        }
        PaymentOrder paymentOrder = new PaymentOrderDao(em).getPaymentOrder("97665487", market.getId());        
        MainPaymentHandler handler = new MainPaymentHandler(em);       
        CheckResponse checkResponse = handler.check(paymentOrder, paymentOrder.getAmount());
        Assert.assertNotNull(checkResponse);
        Assert.assertEquals(ResultCode.ORDER_NOT_FOUND_IN_EMARKETPLACE, checkResponse.getResultCode());
    }


    @Test
    public void testCheckNotExistsPaymentKeyWithValidMarketId() throws Exception{
        TestConf.initConfig();
        EntityManager em = EMF.getEntityManager();
        Market market = new MarketDao(em).getMarketByName("tais2");
        if (market == null){
            new InitTestData().testCreateMarket();
            market = new MarketDao(em).getMarketByName("tais2");
            Assert.assertNotNull(market);
        }
        PaymentOrder paymentOrder = new PaymentOrderDao(em).getPaymentOrder("97665487", market.getId());
        MainPaymentHandler handler = new MainPaymentHandler(em);
        CheckResponse checkResponse = handler.check(paymentOrder, paymentOrder.getAmount());
        Assert.assertNotNull(checkResponse);
        Assert.assertEquals(ResultCode.ORDER_NOT_FOUND_IN_EMARKETPLACE, checkResponse.getResultCode());
    }

    @Test
    public void testCheckExistsPaymentKey() throws Exception{
        TestConf.initConfig();
        EntityManager em = EMF.getEntityManager();
        Market market = new MarketDao(em).getMarketByName("tais2");
        if (market == null){
            new InitTestData().testCreateMarket();
            market = new MarketDao(em).getMarketByName("tais2");
            Assert.assertNotNull(market);
        }
        PaymentOrder paymentOrder = new PaymentOrderDao(em).getPaymentOrder("1561", market.getId());
        MainPaymentHandler handler = new MainPaymentHandler(em);
        paymentOrder.setAmount(0.0);
        CheckResponse checkResponse = handler.check(paymentOrder, paymentOrder.getAmount());
        Assert.assertNotNull(checkResponse);
        Assert.assertEquals(ResultCode.SUCCESS_WITH_AMOUNT, checkResponse.getResultCode());
    }

    @Test
    public void testCheckExistsPaymentKeyWithAmount() throws Exception{
        TestConf.initConfig();
        EntityManager em = EMF.getEntityManager();
        Market market = new MarketDao(em).getMarketByName("tais2");
        if (market == null){
            new InitTestData().testCreateMarket();
            market = new MarketDao(em).getMarketByName("tais2");
            Assert.assertNotNull(market);
        }
        PaymentOrder paymentOrder = new PaymentOrderDao(em).getPaymentOrder("1561", market.getId());
        MainPaymentHandler handler = new MainPaymentHandler(em);
        CheckResponse checkResponse = handler.check(paymentOrder, paymentOrder.getAmount());
        Assert.assertNotNull(checkResponse);
        Assert.assertEquals(ResultCode.SUCCESS_WITHOUT_AMOUNT, checkResponse.getResultCode());
    }

   


}
