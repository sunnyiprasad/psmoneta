package com.batyrov.ps.action;

import com.batyrov.ps.bean.Point;
import com.batyrov.ps.bean.Dealer;
import com.batyrov.ps.Const;
import com.batyrov.ps.Dao;

/**
 * Created by IntelliJ IDEA.
 * User: sulic
 * Date: 11.03.2008
 * Time: 22:12:06
 * To change this template use File | Settings | File Templates.
 */
public class RemoveSubDealer extends Action{
    private long id;

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
            return Const.ERROR;
        }
        dao.remove(dealer);
        return Const.SUCCESS;
    }
}
