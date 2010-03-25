package com.batyrov.ps.action;

import com.batyrov.ps.bean.User;
import com.batyrov.ps.bean.DealerUser;
import com.batyrov.ps.bean.Point;
import com.batyrov.ps.bean.Dealer;
import com.batyrov.ps.Dao;
import com.batyrov.ps.Const;

import javax.persistence.EntityTransaction;

/**
 * Created by IntelliJ IDEA.
 * User: sulic
 * Date: 11.03.2008
 * Time: 22:13:55
 * To change this template use File | Settings | File Templates.
 */
public class UpdateUser extends Action {
    private Boolean dealerUser = false;
    private long id = -1;
    private long pointId = -1;
    private String name = null;
    private String phone = null;
    private long dealerId = -1;
    private int type = -1;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDealerId() {
        return dealerId;
    }

    public void setDealerId(long dealerId) {
        this.dealerId = dealerId;
    }

    public Boolean getDealerUser() {
        return dealerUser;
    }

    public void setDealerUser(Boolean dealerUser) {
        this.dealerUser = dealerUser;
    }

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
        if (dealerUser) {
            if (dealerId < 1)
            {
                dealerId = myDealerId;
            }
            dealer = em.find(Dealer.class, dealerId);
            if (dealer == null)
            {
                addActionError(getText("dealer_not_found"));
                return Const.ERROR2;
            }
            if (dealerId != myDealerId && dealer.getOwnerDealerId() != myDealerId)
            {
                addActionError(getText("access_denied"));
                return Const.ERROR2;
            }
            DealerUser user;
            if (id == -1) {
                user = new DealerUser();
                user.setName(name);
                user.setPhone(phone);
                user.setType(type);
                user.setDealerId(dealerId);
                user.setDealer(em.find(Dealer.class, dealerId));
                if (dao.persist(user)) {
                    return Const.SUCCESS2;
                } else {
                    return Const.ERROR2;
                }
            } else {
                user = em.find(DealerUser.class, id);
                if (user == null) {
                    addActionError(getText("user_not_found"));
                    return Const.ERROR2;
                }
                em.getTransaction().begin();
                try {
                    user.setName(name);
                    user.setPhone(phone);
                    user.setType(type);
                    user.setDealerId(dealerId);
                    user.setDealer(em.find(Dealer.class, dealerId));
                    em.getTransaction().commit();
                    return Const.SUCCESS2;
                } catch (Exception e) {
                    e.printStackTrace();
                    em.getTransaction().rollback();
                    return Const.ERROR2;
                }
            }
        } else {
            User user;
            if (pointId < 1)
            {
                addActionError(getText("point_not_defined"));
                return Const.ERROR2;
            }
            point = em.find(Point.class, pointId);
            if (point == null)
            {
                addActionError(getText("point_not_found"));
                return Const.ERROR2;
            }
            if (point.getDealerId() != myDealerId)
            {
                addActionError(getText("access_denied"));
                return Const.ERROR2;
            }
            if (id == -1) {
                user = new User();
                user.setName(name);
                user.setPhone(phone);
                user.setType(type);
                user.setPointId(pointId);
                user.setPoint(em.find(Point.class, pointId));
                if (dao.persist(user)) {
                    return Const.SUCCESS;
                } else {
                    return Const.ERROR;
                }
            } else {
                user = em.find(User.class, id);
                if (user == null) {
                    addActionError(getText("user_not_found"));
                    return Const.ERROR;
                }
                em.getTransaction().begin();
                try {
                    user.setName(name);
                    user.setPhone(phone);
                    user.setType(type);
                    user.setPointId(pointId);
                    user.setPoint(em.find(Point.class, pointId));
                    em.getTransaction().commit();
                    return Const.SUCCESS;
                } catch (Exception e) {
                    e.printStackTrace();
                    em.getTransaction().rollback();
                    return Const.ERROR;
                }
            }
        }
    }
}
