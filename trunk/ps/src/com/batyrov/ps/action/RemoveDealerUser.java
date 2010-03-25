package com.batyrov.ps.action;

import com.batyrov.ps.bean.Dealer;
import com.batyrov.ps.bean.User;
import com.batyrov.ps.bean.DealerUser;
import com.batyrov.ps.Const;
import com.batyrov.ps.Dao;

/**
 * Created by IntelliJ IDEA.
 * User: sulic
 * Date: 11.03.2008
 * Time: 22:12:34
 * To change this template use File | Settings | File Templates.
 */
public class RemoveDealerUser extends Action{
    private long id;
    private long dealerId;

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
        if (myUserId == id){
            addActionError(getText("cannot_remove_self_user"));
            return Const.ERROR;
        }
        DealerUser user = em.find(DealerUser.class, id);
        if (user == null){
            addActionError(getText("user_not_found"));
            return Const.ERROR;
        }
        dao.remove(user);
        return Const.SUCCESS;
    }
}