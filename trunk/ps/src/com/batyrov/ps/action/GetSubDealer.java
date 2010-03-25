package com.batyrov.ps.action;

import com.batyrov.ps.Const;
import com.batyrov.ps.Dao;
import com.batyrov.ps.bean.Dealer;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sulic
 * Date: 23.03.2008
 * Time: 10:21:00
 * To change this template use File | Settings | File Templates.
 */
public class GetSubDealer extends Action{
    private long id = -1;
    private Dealer dealer = null;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Dealer getDealer() {
        return dealer;
    }

    public void setDealer(Dealer dealer) {
        this.dealer = dealer;
    }

    public String execute() throws Exception {
        super.execute();
        dealer = em.find(Dealer.class, id);
        if (dealer == null){
            dealer = new Dealer();
        }else{
            if (dealer.getOwnerDealerId() != myDealerId && dealer.getId() != myDealerId){
                addActionError(getText("access_denied"));
                return Const.ERROR;
            }
        }
        return Const.SUCCESS;
    }
}
