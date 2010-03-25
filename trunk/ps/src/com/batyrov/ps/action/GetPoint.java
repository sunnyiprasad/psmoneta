package com.batyrov.ps.action;

import com.batyrov.ps.Const;
import com.batyrov.ps.Dao;
import com.batyrov.ps.util.PointType;
import com.batyrov.ps.util.Region;
import com.batyrov.ps.bean.Point;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sulic
 * Date: 23.03.2008
 * Time: 10:20:53
 * To change this template use File | Settings | File Templates.
 */
public class GetPoint extends Action{
    private long id = -1;
    private Point point = null;
    private List types = PointType.getPointTypes();
    private List regions = Region.getRegions();
    private long dealerId = -1;

    public long getDealerId() {
        return dealerId;
    }

    public void setDealerId(long dealerId) {
        this.dealerId = dealerId;
    }

    public List getRegions() {
        return regions;
    }

    public void setRegions(List regions) {
        this.regions = regions;
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

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public String execute() throws Exception {
        super.execute();
        point = em.find(Point.class, id);
        if (point == null){
            point = new Point();
        }else{
            if (point.getDealerId() != myDealerId && point.getDealer().getOwnerDealerId() != myDealerId){
                addActionError(getText("access_denied"));
                return Const.ERROR;
            }
            dealerId = point.getDealerId();
        }
        return Const.SUCCESS;
    }
}
