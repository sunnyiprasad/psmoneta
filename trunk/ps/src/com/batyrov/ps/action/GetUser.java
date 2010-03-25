package com.batyrov.ps.action;

import com.batyrov.ps.bean.User;
import com.batyrov.ps.bean.Dealer;
import com.batyrov.ps.Const;
import com.batyrov.ps.Dao;
import com.batyrov.ps.util.DealerUserType;
import com.batyrov.ps.util.UserType;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sulic
 * Date: 23.03.2008
 * Time: 10:22:46
 * To change this template use File | Settings | File Templates.
 */
public class GetUser extends Action {
    private long id = -1;
    private User user = null;
    private List types = UserType.getUserTypes();
    private boolean dealerUser = false;
    private long pointId = -1;

    public long getPointId() {
        return pointId;
    }

    public void setPointId(long pointId) {
        this.pointId = pointId;
    }

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String execute() throws Exception {
        super.execute();
        user = em.find(User.class, id);
        if (user == null){
            user = new User();
        }else{
            if (user.getPoint().getDealerId() != myDealerId){
                addActionError(getText("access_denied"));
                return Const.ERROR;
            }
        }
        return Const.SUCCESS;
    }
}
