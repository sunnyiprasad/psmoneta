package com.batyrov.ps.action;

import com.batyrov.ps.Const;
import com.batyrov.ps.bean.DealerUser;

/**
 * Created by IntelliJ IDEA.
 * User: sulic
 * Date: 26.03.2008
 * Time: 20:59:47
 * To change this template use File | Settings | File Templates.
 */
public class LockDealerUser extends Action{

    long id;
    long dealerId;

    public long getDealerId() {
        return dealerId;
    }

    public void setDealerId(long dealerId) {
        this.dealerId = dealerId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String execute() throws Exception {
        super.execute();
        DealerUser user = em.find(DealerUser.class, id);
        if (user == null){
            addActionError(getText("user_not_found"));
            return Const.SUCCESS;
        }
        if (user.getDealerId() != myDealerId && user.getDealer().getOwnerDealerId() != myDealerId){
            addActionError(getText("access_denied"));
            return Const.ERROR;
        }
        em.getTransaction().begin();
        try
        {
            user.setStatus(!user.isStatus());
            em.getTransaction().commit();
        }catch (Exception e){
            e.printStackTrace();
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
        }
        return Const.SUCCESS;
    }


}