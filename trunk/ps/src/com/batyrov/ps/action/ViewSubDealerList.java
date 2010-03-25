package com.batyrov.ps.action;

import com.batyrov.ps.bean.Dealer;
import com.batyrov.ps.bean.Point;
import com.batyrov.ps.Const;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: sulic
 * Date: 11.03.2008
 * Time: 22:09:17
 * To change this template use File | Settings | File Templates.
 */
public class ViewSubDealerList extends Action{
    private Dealer dealer = null;
    private Collection<Dealer> dealers = null;

    

    public Dealer getDealer() {
        return dealer;
    }

    public void setDealer(Dealer dealer) {
        this.dealer = dealer;
    }

    public Collection<Dealer> getDealers() {
        return dealers;
    }

    public void setDealers(Collection<Dealer> dealers) {
        this.dealers = dealers;
    }

    public String execute() throws Exception {
        super.execute();
        dealer = em.find(Dealer.class, myDealerId);
        if (dealer == null){
            addActionError(getText("dealer_not_found"));
            return Const.ERROR;
        }
        dealers = dealer.getDealers();
        return Const.SUCCESS;
    }
}
