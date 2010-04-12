/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsc.moneta.test.module;

import com.rsc.moneta.bean.PaymentOrder;
import com.rsc.moneta.dao.EMF;
import com.rsc.moneta.dao.PaymentOrderDao;
import com.rsc.moneta.module.MainPaymentHandler;
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
    public void testPay(){
        TestConf.initConfig();
        EntityManager em = EMF.getEntityManager();
        PaymentOrder order = new PaymentOrderDao(em).getPaymentOrder("1561", 67699912);
        Assert.assertNotNull(order);
        MainPaymentHandler handler = new MainPaymentHandler();
        handler.pay(order);
    }
}
