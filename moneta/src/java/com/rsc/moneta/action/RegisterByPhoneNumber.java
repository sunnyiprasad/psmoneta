/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsc.moneta.action;

import com.rsc.moneta.bean.Sms;
import com.opensymphony.xwork2.Action;
import com.rsc.moneta.action.dao.Dao;
import com.rsc.moneta.bean.User;

/**
 *
 * @author sulic
 */
public class RegisterByPhoneNumber extends BaseAction {

    private String phone;

    @Override
    public String execute() throws Exception {
        if (phone != null && !"".equals(phone)){            
            User user = new User();
            user.setPhone(phone);
            user.setPassword(generatePassword());
            Dao dao = new Dao(em);
            dao.persist(user);
            Sms sms = new Sms();
            sms.setPhone(phone);
            sms.setMessage(getText("reg_phone_sms_message", user.getPassword()));
            dao.persist(sms);
        }
        return Action.SUCCESS;
    }

    public String generatePassword(){
        // TODO: 
        return null;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
