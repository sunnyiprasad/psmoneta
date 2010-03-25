package com.batyrov.ps.action;

import com.batyrov.ps.Const;
import com.batyrov.ps.Dao;
import com.batyrov.ps.bean.Dealer;
import com.batyrov.ps.bean.Point;
import com.batyrov.ps.bean.PointPayment;

import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.sql.Timestamp;

/**
 * Created by IntelliJ IDEA.
 * User: sulic
 * Date: 15.03.2008
 * Time: 20:15:33
 * To change this template use File | Settings | File Templates.
 */
public class AddPointBalance extends Action{
    private long pointId;
    private double summa = 0;

    public long getPointId() {
        return pointId;
    }

    public void setPointId(long pointId) {
        this.pointId = pointId;
    }

    public double getSumma() {
        return summa;
    }

    public void setSumma(double summa) {
        this.summa = summa;
    }

    public String execute() throws Exception {
        super.execute();
        if (em.find(Point.class, pointId) == null){
            addActionError(getText("point_not_found"));
            return Const.ERROR;
        }
        if (em.find(Dealer.class, myDealerId) == null){
            addActionError(getText("dealer_not_found"));
            return Const.ERROR;
        }
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try{
            Query q = em.createNativeQuery("UPDATE T_POINT SET BALANCE = BALANCE + "+summa+" WHERE ID= "+pointId);
            q.executeUpdate();
            q = em.createNativeQuery("UPDATE T_DEALER SET BALANCE = BALANCE - "+summa+" WHERE ID= "+myDealerId);
            q.executeUpdate();
            PointPayment p = new PointPayment(new Timestamp(System.currentTimeMillis()), summa, myDealerId, pointId);
            p.setDealer(em.find(Dealer.class, myDealerId));
            p.setPoint(em.find(Point.class, pointId));
            em.persist(p);
            tx.commit();
        }catch (Exception e){
            tx.rollback();
        }
        return Const.SUCCESS;
    }
}
