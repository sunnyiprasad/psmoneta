package com.batyrov.ps.action;

import com.batyrov.ps.Const;
import com.batyrov.ps.bean.User;
import com.batyrov.ps.bean.Dealer;

/**
 * Created by IntelliJ IDEA.
 * User: sulic
 * Date: 26.03.2008
 * Time: 20:59:47
 * To change this template use File | Settings | File Templates.
 */
public class LockSubDealer extends Action{
    long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String execute() throws Exception {
        super.execute();
        Dealer dealer = em.find(Dealer.class, id);
        if (dealer == null){
            addActionError(getText("dealer_not_found"));
            return Const.SUCCESS;
        }
        if (dealer.getOwnerDealerId() != myDealerId){
            addActionError(getText("access_denied"));
            return Const.ERROR;
        }
        em.getTransaction().begin();
        try
        {
            dealer.setStatus(!dealer.isStatus());
            em.getTransaction().commit();
        }catch (Exception e){
            e.printStackTrace();
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
        }
        return Const.SUCCESS;
    }
}
