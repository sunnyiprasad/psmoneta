package com.batyrov.ps.action;

import com.batyrov.ps.bean.Point;
import com.batyrov.ps.bean.Dealer;
import com.batyrov.ps.Dao;
import com.batyrov.ps.Const;

/**
 * Created by IntelliJ IDEA.
 * User: sulic
 * Date: 11.03.2008
 * Time: 22:13:41
 * To change this template use File | Settings | File Templates.
 */
public class UpdatePoint extends Action {
    private long id = -1;
    private long dealerId;
    private String address;
    private String phone;
    private String name = null;
    private int region;
    private int type;

    public int getRegion() {
        return region;
    }

    public void setRegion(int region) {
        this.region = region;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDealerId() {
        return dealerId;
    }

    public void setDealerId(long dealerId) {
        this.dealerId = dealerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String execute() throws Exception {
        super.execute();        
        dealerId = myDealerId;
        Point point;
        if (id == -1) {
            point = new Point();
            point.setName(name);
            point.setAddress(address);
            point.setPhone(phone);
            point.setRegion(region);
            point.setType(type);
            point.setName(name);
            point.setAddress(address);
            point.setPhone(phone);
            point.setRegion(region);
            point.setType(type);            
            point.setDealerId(dealerId);
            point.setDealer(em.find(Dealer.class, dealerId));
            if (dao.persist(point)) {
                return Const.SUCCESS;
            } else {
                addActionError(getText("error_during_save_point"));
                return Const.ERROR;
            }
        } else {
            point = em.find(Point.class, id);
            if (point == null) {
                addActionError(getText("point_not_found"));
                return Const.ERROR;
            }
            em.getTransaction().begin();
            try {
                point.setName(name);
                point.setAddress(address);
                point.setPhone(phone);
                point.setRegion(region);
                point.setType(type);
                point.setName(name);
                point.setAddress(address);
                point.setPhone(phone);
                point.setRegion(region);
                point.setType(type);
                em.getTransaction().commit();
                return Const.SUCCESS;
            } catch (Exception e) {
                em.getTransaction().rollback();
                addActionError(getText("error_during_save_point"));
                return Const.ERROR;
            }
        }

    }
}
