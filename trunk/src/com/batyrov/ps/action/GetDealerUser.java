package com.batyrov.ps.action;

import com.batyrov.ps.Const;
import com.batyrov.ps.Dao;
import com.batyrov.ps.util.DealerUserType;
import com.batyrov.ps.bean.DealerUser;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sulic
 * Date: 23.03.2008
 * Time: 10:22:59
 * To change this template use File | Settings | File Templates.
 */
public class GetDealerUser extends Action {

    private long id = -1;
    private long dealerId = -1;
    private DealerUser user = null;
    private List types = DealerUserType.getUserTypes();
    private boolean dealerUser = true;

    public boolean isDealerUser() {
        return dealerUser;
    }

    public void setDealerUser(boolean dealerUser) {
        this.dealerUser = dealerUser;
    }

    public List getTypes() {
        return types;
    }

    public void setTypes(List types) {
        this.types = types;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public DealerUser getUser() {
        return user;
    }

    public void setUser(DealerUser user) {
        this.user = user;
    }

    public long getDealerId() {
        return dealerId;
    }

    public void setDealerId(long dealerId) {
        this.dealerId = dealerId;
    }

    public String execute() throws Exception {
        super.execute();
        user = em.find(DealerUser.class, id);
        if (user == null) {
            user = new DealerUser();
        }else{
            if (myDealerId != user.getDealerId() && myDealerId != user.getDealer().getOwnerDealerId())
            {
                addActionError(getText("access_denied"));
                return Const.ERROR;
            }
        }
        return Const.SUCCESS;
    }
}
