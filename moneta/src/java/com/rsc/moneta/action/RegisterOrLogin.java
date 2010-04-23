/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsc.moneta.action;

import com.opensymphony.xwork2.Action;
import com.rsc.moneta.bean.PaymentOrder;
import com.rsc.moneta.dao.Dao;
import com.rsc.moneta.bean.User;
import com.rsc.moneta.dao.UserDao;

/**
 *
 * @author sulic
 */
public class RegisterOrLogin extends BaseAction {

    private String phone;
    private Long paymentId;
    private PaymentOrder paymentOrder;
    private String password;
    private String paymentOrderId;
    private String name;
    private String email;
    private Boolean _new;

    @Override
    public String execute() throws Exception {

        if (paymentId != null) {
            paymentOrder = em.find(PaymentOrder.class, paymentId);
            if (paymentOrder == null) {
                addActionError(getText("payment_key_not_found"));
                return Action.ERROR;
            }else{
                if (paymentOrder.getUser() != null){
                    return Action.LOGIN;
                }
            }
        } else {
            addActionError(getText("payment_key_not_found"));
            return Action.ERROR;
        }
        if (email != null && !"".equals(email)) {
            User localUser = new UserDao(em).getUserByEmail(email);
            if (localUser == null) {
                localUser = new UserDao(em).createUserAndSendNotify(phone, name, email);
                paymentOrder.setUser(localUser);
                new Dao(em).persist(paymentOrder);
            } else {
                if (localUser.getPassword().equals(password)) {
                    paymentOrder.setUser(localUser);
                    new Dao(em).persist(paymentOrder);
                    session.put("user", localUser);
                } else {
                    addActionError(getText("incorrect_password_please_try_again"));
                    return "again";
                }
            }
        }
        paymentOrderId = String.format("%019d", paymentOrder.getId());
        return Action.SUCCESS;
    }

    public Boolean getNew() {
        return _new;
    }

    public void setNew(Boolean _new) {
        this._new = _new;
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

    public PaymentOrder getPaymentOrder() {
        return paymentOrder;
    }

    public void setPaymentOrder(PaymentOrder paymentOrder) {
        this.paymentOrder = paymentOrder;
    }

    public String getPaymentOrderId() {
        return paymentOrderId;
    }

    public void setPaymentOrderId(String paymentOrderId) {
        this.paymentOrderId = paymentOrderId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
