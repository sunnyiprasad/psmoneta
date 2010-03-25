package com.batyrov.ps.action;

import com.batyrov.ps.bean.Dealer;
import com.batyrov.ps.Dao;
import com.batyrov.ps.Const;

/**
 * Created by IntelliJ IDEA.
 * User: sulic
 * Date: 11.03.2008
 * Time: 22:11:51
 * To change this template use File | Settings | File Templates.
 */
public class UpdateSubDealer extends Action {
    private long id = -1;
    private String email = null;
    private String name = null;
    private String phone = null;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String execute() throws Exception {
        super.execute();
        Dealer dealer;
        if (id == -1) {
            dealer = new Dealer();
            dealer.setEmail(email);
            dealer.setName(name);
            dealer.setPhone(phone);
            dealer.setOwnerDealerId(myDealerId);
            dealer.setOwnerDealer(em.find(Dealer.class, myDealerId));
            if (dao.persist(dealer))
                return Const.SUCCESS;
            else {
                return Const.ERROR;
            }
        } else {
            dealer = em.find(Dealer.class, id);
            if (dealer == null) {
                addActionError(getText("dealer_not_found"));
                return Const.ERROR;
            }
            em.getTransaction().begin();
            try {
                dealer.setEmail(email);
                dealer.setName(name);
                dealer.setPhone(phone);
                dealer.setOwnerDealerId(myDealerId);
                dealer.setOwnerDealer(em.find(Dealer.class, myDealerId));
                em.getTransaction().commit();
                return Const.SUCCESS;
            } catch (Exception e) {
                e.printStackTrace();
                addActionError(e.toString());
                if (em.getTransaction().isActive())
                    em.getTransaction().rollback();
                return Const.ERROR;
            }
        }
    }
}
