package com.batyrov.ps.action;

import com.batyrov.ps.bean.Point;
import com.batyrov.ps.bean.Dealer;
import com.batyrov.ps.bean.User;
import com.batyrov.ps.Const;

import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Created by IntelliJ IDEA.
 * User: sulic
 * Date: 21.12.2008
 * Time: 21:54:55
 * To change this template use File | Settings | File Templates.
 */
public class AddUserBalance extends Action{
    private long userId;
    private double summa = 0;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public double getSumma() {
        return summa;
    }

    public void setSumma(double summa) {
        this.summa = summa;
    }

    public String execute() throws Exception {
        super.execute();
        if (em.find(User.class, userId) == null){
            addActionError(getText("user_not_found"));
            return Const.ERROR;
        }
        if (em.find(Dealer.class, myDealerId) == null){
            addActionError(getText("dealer_not_found"));
            return Const.ERROR;
        }
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try{
            Query q = em.createNativeQuery("UPDATE T_USER SET BALANCE = BALANCE + "+summa+" WHERE ID= "+userId);
            q.executeUpdate();
            q = em.createNativeQuery("UPDATE T_DEALER SET BALANCE = BALANCE - "+summa+" WHERE ID= "+myDealerId);
            q.executeUpdate();
            tx.commit();
        }catch (Exception e){
            tx.rollback();
        }
        return Const.SUCCESS;
    }


}
