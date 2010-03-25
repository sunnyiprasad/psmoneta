package com.batyrov.ps.servlet;

import com.batyrov.ps.module.Cyberplat;
import com.batyrov.ps.module.Processor;
import com.batyrov.ps.bean.AbonentPayment;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.persistence.EntityTransaction;
import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.Properties;
import java.sql.Timestamp;


/**
 * Created by IntelliJ IDEA.
 * User: sulic
 * Date: 16.02.2008
 * Time: 0:26:25
 * To change this template use File | Settings | File Templates.
 */
public class Check extends Servlet {
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        super.doPost(httpServletRequest, httpServletResponse);
        if (getAnswer() != null) {
            return;
        }
        if (getPayment() != null) {
            Properties res = Cyberplat.getErrorAnswer(1, 1);
            print(res);
        }
        EntityManager em = dao.getEm();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            AbonentPayment pay = new AbonentPayment();
            pay.setSession(getSession());
            pay.setAccount(getAccount());
            pay.setAmount(getAmount());
            pay.setAmountAll(getAmountAll());
            pay.setComment(getComment());
            pay.setDate(getDate());
            pay.setDealerId(getDealerId());
            pay.setNumber(getNumber());
            pay.setPointId(getPointId());
            pay.setProviderId(getProviderId());
            pay.setStartDate(new Timestamp(System.currentTimeMillis()));
            pay.setEndDate(new Timestamp(System.currentTimeMillis()));
            pay.setUserId(getUserId());
            pay.setProvider(getProvider());
            pay.setDealer(getDealer());
            pay.setPoint(getPoint());
            pay.setUser(getUser());
            pay.setStatus(AbonentPayment.NEW);
            em.persist(pay);
            tx.commit();
            try {
                setPayment(pay);
                Processor proc = (Processor) this.getClass().getClassLoader().loadClass(getProvider().getClassName()).newInstance();
                print(proc.check(getPayment(), getReq_type()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }
    }

}
