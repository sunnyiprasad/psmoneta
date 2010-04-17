/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsc.moneta.test;


import com.rsc.moneta.dao.ProviderDao;
import com.rsc.moneta.dao.Dao;
import com.rsc.moneta.dao.EMF;
import com.rsc.moneta.module.cyberplat.AbonentPayment;
import com.rsc.moneta.module.cyberplat.Cyberplat;
import com.rsc.moneta.module.cyberplat.Provider;
import java.io.IOException;
import java.util.Date;
import javax.persistence.EntityManager;
import org.junit.Test;

/**
 *
 * @author sulic
 */
public class TestCyberplat {

    @Test
    public void testCheckPay() throws IOException{
        System.out.println(System.getProperty("user.dir"));
        EntityManager em = EMF.getEntityManager();
        Provider provider = new ProviderDao(em).getProviderByName("МТС");
        if (provider == null){
            provider = new Provider();
            provider.setName("МТС");
            provider.setPaymentUrl("https://service.cyberplat.ru/cgi-bin/es/es_pay.cgi");
            provider.setCheckUrl("https://service.cyberplat.ru/cgi-bin/es/es_pay_check.cgi");
            provider.setClassName("com.rsc.moneta.module.cyberplat.Cyberplat");
            provider.setGetStatusUrl("https://service.cyberplat.ru/cgi-bin/es/es_pay_status.cgi");
            new Dao(em).persist(provider);
        }
        AbonentPayment abonentPayment = new AbonentPayment();
        abonentPayment.setAmount(1);
        abonentPayment.setAmountAll(1);
        abonentPayment.setSession("20040929144042321237");
        abonentPayment.setAccount(null);
        abonentPayment.setStatus(0);
        abonentPayment.setProvider(provider);
        abonentPayment.setDate(new Date());
        abonentPayment.setStartDate(new Date());
        abonentPayment.setNumber("8888888888");
        abonentPayment.setAuthCode(null);
        abonentPayment.setTransId(null);
        abonentPayment.setComment(null);
        new Dao(em).persist(abonentPayment);
        Cyberplat cyberplat = new Cyberplat(em);
        cyberplat.check(abonentPayment, 1);

    }
}
