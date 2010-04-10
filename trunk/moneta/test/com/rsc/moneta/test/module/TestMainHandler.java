/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsc.moneta.test.module;

import com.rsc.moneta.Config;
import com.rsc.moneta.Const;
import com.rsc.moneta.OutputHandlerNotFoundException;
import com.rsc.moneta.bean.Market;
import com.rsc.moneta.bean.PaymentKey;
import com.rsc.moneta.bean.User;
import com.rsc.moneta.dao.Dao;
import com.rsc.moneta.dao.EMF;
import com.rsc.moneta.dao.MarketDao;
import com.rsc.moneta.dao.UserDao;
import com.rsc.moneta.module.CheckRequest;
import com.rsc.moneta.module.MainPaymentHandler;
import com.rsc.moneta.module.OutputHandler;
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
    public void testCheck() throws Exception{
        TestConf.initConfig();
        EntityManager em = EMF.getEntityManager();
        PaymentKey paymentKey = new PaymentKey();
        paymentKey.setCurrency(Const.RUB);
        paymentKey.setKey("");
        paymentKey.setOrderStatus(Const.ORDER_STATUS_ACCEPTED);
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
        Assert.assertNotNull(handler.check(request));
        

    }

}
