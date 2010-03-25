package com.batyrov.ps.action;

import com.batyrov.ps.bean.Point;
import com.batyrov.ps.bean.Dealer;
import com.batyrov.ps.bean.DealerPayment;
import com.batyrov.ps.Const;

import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.sql.Timestamp;

/**
 * Created by IntelliJ IDEA.
 * User: sulic
 * Date: 15.03.2008
 * Time: 20:19:57
 * To change this template use File | Settings | File Templates.
 */
public class AddDealerBalance extends Action {
    private long dealerId;
    private double summa = 0;

    public double getSumma() {
        return summa;
    }

    public void setSumma(double summa) {
        this.summa = summa;
    }

    public long getDealerId() {
        return dealerId;
    }

    public void setDealerId(long dealerId) {
        this.dealerId = dealerId;
    }

    public String execute() throws Exception {
        // ¬ данном ситуации бабки списываютс€ по любому у твоего дилера так что вопрос только
        // блокировать ли переброску денег другому дилеру.
        super.execute();
        if (em.find(Dealer.class, dealerId) == null) {
            addActionError(getText("dealer_not_found"));
            return Const.ERROR;
        }
        if (em.find(Dealer.class, myDealerId) == null) {
            addActionError(getText("dealer_not_found"));
            return Const.ERROR;
        }
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            Query q = em.createNativeQuery("UPDATE T_DEALER SET BALANCE = BALANCE + " + summa + " WHERE ID= " + dealerId);
            q.executeUpdate();
            q = em.createNativeQuery("UPDATE T_DEALER SET BALANCE = BALANCE - " + summa + " WHERE ID= " + myDealerId);
            q.executeUpdate();
            DealerPayment pay = new DealerPayment(myDealerId,dealerId,summa,new Timestamp(System.currentTimeMillis()));
            pay.setFromDealer(em.find(Dealer.class, myDealerId));
            pay.setToDealer(em.find(Dealer.class, dealerId));
            em.persist(pay);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        }
        return Const.SUCCESS;
    }
}
