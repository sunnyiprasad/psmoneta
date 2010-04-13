/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsc.moneta.test.module;

import com.rsc.moneta.bean.PaymentOrder;
import com.rsc.moneta.dao.EMF;
import com.rsc.moneta.dao.PaymentOrderDao;
import com.rsc.moneta.module.CheckResponse;
import com.rsc.moneta.module.MainPaymentHandler;
import com.rsc.moneta.module.ResultCode;
import com.rsc.moneta.test.TestConf;
import javax.persistence.EntityManager;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author sulic
 */
public class TestMainHandlerPay {

    @Test
    public void testPayWithInvalidTransactionId(){
        TestConf.initConfig();
        EntityManager em = EMF.getEntityManager();
        PaymentOrder order = new PaymentOrderDao(em).getPaymentOrder("97665487", 67699912);
        Assert.assertNotNull(order);
        MainPaymentHandler handler = new MainPaymentHandler();
        CheckResponse response = handler.pay(order);
        Assert.assertNotNull(response);
        Assert.assertEquals(ResultCode.ORDER_NOT_FOUND , response.getResultCode());
    }

    @Test
    public void testPay(){
        TestConf.initConfig();
        EntityManager em = EMF.getEntityManager();
        PaymentOrder order = new PaymentOrderDao(em).getPaymentOrder("1561", 67699912);
        Assert.assertNotNull(order);
        MainPaymentHandler handler = new MainPaymentHandler();
        CheckResponse response = handler.pay(order);
        Assert.assertNotNull(response);
        Assert.assertEquals(ResultCode.SUCCESS_WITH_AMOUNT , response.getResultCode());
    }

    @Test
    public void testPayWithOutSum(){
        TestConf.initConfig();
        EntityManager em = EMF.getEntityManager();
        PaymentOrder order = new PaymentOrderDao(em).getPaymentOrder("1561", 67699912);
        Assert.assertNotNull(order);
        order.setAmount(null);
        MainPaymentHandler handler = new MainPaymentHandler();
        CheckResponse response = handler.pay(order);
        Assert.assertNotNull(response);
        Assert.assertEquals(ResultCode.SUCCESS_WITH_AMOUNT , response.getResultCode());
    }
}
