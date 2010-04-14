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

import com.rsc.moneta.bean.PaymentOrder;
import com.rsc.moneta.module.inputhandler.OSMPInputHandler;
import com.rsc.moneta.dao.EMF;
import com.rsc.moneta.dao.Dao;

/**
 *
 * @author Солодовников Д.А.
 */
public class TestOsmpHandler {

    // Тест "Этот платеж уже завершён"
    @Test
    public void testOsmpCheck1(){
        // 1. Создать запись о заказе в т-це PaymentOrder, выставить заказу 
        // статус "ORDER_STATUS_PAID_AND_COMPLETED"
        EntityManager em = EMF.getEntityManager();
        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setStatus(PaymentOrder.ORDER_STATUS_PAID_AND_COMPLETED);
        new Dao(em).persist(paymentOrder);
        em.close();

        // 2. Сэмулировать тестовый запрос "check" к ОСМП-хендлеру по созданному
        // в п.1 номеру заказа
        String account = String.format("%019d", paymentOrder.getId());
        String txn_id = "1234567";
        OSMPInputHandler handler = new OSMPInputHandler();
        Map map = new Properties();
        map.put("command", "check");
        map.put("txn_id", txn_id);
        map.put("account", account);
        String xml = handler.check(map);

        // 3. Удалить созданную в п.1 запись о заказе
        em = EMF.getEntityManager();
        new Dao(em).remove(paymentOrder);
        em.close();
        

        // 4. Сравнить полученный response с планируемым
        Assert.assertEquals("<?xml version='1.0' encoding='UTF-8'?><response><osmp_txn_id>" + txn_id + "</osmp_txn_id><prv_txn>" + account + "</prv_txn><result>0</result><comment>Этот платеж уже завершён</comment></response>",
                xml);
    }
}
