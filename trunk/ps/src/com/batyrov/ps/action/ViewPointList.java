package com.batyrov.ps.action;

import com.batyrov.ps.bean.Point;
import com.batyrov.ps.bean.Dealer;
import com.batyrov.ps.Const;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: sulic
 * Date: 11.03.2008
 * Time: 22:09:28
 * To change this template use File | Settings | File Templates.
 */
public class ViewPointList extends Action{

    public long dealerId = -1;
    public Dealer dealer = null;

    public Collection<Point> points = null;

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

    public Collection<Point> getPoints() {
        return points;
    }

    public void setPoints(Collection<Point> points) {
        this.points = points;
    }



    public String execute() throws Exception {
        super.execute();
        dealer = em.find(Dealer.class, myDealerId);
        if (dealer == null){
            addActionError(getText("dealer_not_found"));
            return Const.ERROR;
        }
        points = dealer.getPoints();
        return Const.SUCCESS;
    }
}
