package com.batyrov.ps.action;

import com.batyrov.ps.bean.User;
import com.batyrov.ps.bean.Point;
import com.batyrov.ps.bean.Dealer;
import com.batyrov.ps.bean.DealerUser;
import com.batyrov.ps.Const;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: sulic
 * Date: 11.03.2008
 * Time: 22:09:43
 * To change this template use File | Settings | File Templates.
 */
public class ViewDealerUserList extends Action {
    private long dealerId = -1;
    private Dealer dealer = null;
    private Collection<DealerUser> users = null;

    public long getDealerId() {
        return dealerId;
    }

    public void setDealerId(long dealerId) {
        this.dealerId = dealerId;
    }

    public Dealer getDealer() {
        return dealer;
    }

    public void setDealer(Dealer dealer) {
        this.dealer = dealer;
    }

    public Collection<DealerUser> getUsers() {
        return users;
    }

    public void setUsers(Collection<DealerUser> users) {
        this.users = users;
    }

    public String execute() throws Exception {
        super.execute();
        if (dealerId == 0)
            dealerId = myDealerId;
        dealer = em.find(Dealer.class, dealerId);
        if (dealer == null) {
            addActionError(getText("dealer_not_found"));
            return Const.ERROR;
        }
        users = dealer.getUsers();
        return Const.SUCCESS;
    }
}