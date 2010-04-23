/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsc.moneta.test.module;

import com.rsc.moneta.bean.PaymentOrder;
import com.rsc.moneta.bean.PaymentOrderStatus;
import com.rsc.moneta.dao.EMF;
import com.rsc.moneta.dao.PaymentOrderDao;
import com.rsc.moneta.module.inputhandler.OSMPInputHandler;
import com.rsc.moneta.test.TestConf;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import org.junit.Test;

/**
 *
 * @author sulic
 */
public class TestExample {

    @Test
    public void testPayAllOrder() {
        TestConf.initConfig();
        EntityManager em = EMF.getEntityManager();
        List<PaymentOrder> list = new PaymentOrderDao(em).getAllPaymentOrderFilterByStatus(PaymentOrderStatus.ORDER_STATUS_ACCEPTED);
        for (PaymentOrder paymentOrder : list) {
            OSMPInputHandler handler = new OSMPInputHandler();
            long orderId = paymentOrder.getId();
            String account = String.format("%019d", paymentOrder.getId());
            String txn_id = account;
            Map map = new Properties();
            map.put("command", "check");
            map.put("txn_id", txn_id);
            map.put("account", account);
            String response = handler.check(map);
            Logger.getLogger(TestExample.class.getName()).severe(response);
        }
    }
}
