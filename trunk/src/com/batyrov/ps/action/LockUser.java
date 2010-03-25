package com.batyrov.ps.action;

import com.batyrov.ps.Const;
import com.batyrov.ps.bean.DealerUser;
import com.batyrov.ps.bean.User;

/**
 * Created by IntelliJ IDEA.
 * User: sulic
 * Date: 26.03.2008
 * Time: 20:59:47
 * To change this template use File | Settings | File Templates.
 */
public class LockUser extends Action{
    long id;
    long pointId;

    public long getPointId() {
        return pointId;
    }

    public void setPointId(long pointId) {
        this.pointId = pointId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String execute() throws Exception {
        super.execute();
        User user = em.find(User.class, id);
        if (user == null){
            addActionError(getText("user_not_found"));
            return Const.SUCCESS;
        }
        if (user.getPoint().getDealerId() != myDealerId){
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