/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsc.moneta.action;

import com.rsc.moneta.bean.Sms;
import com.opensymphony.xwork2.Action;
import com.rsc.moneta.bean.PaymentOrder;
import com.rsc.moneta.dao.Dao;
import com.rsc.moneta.bean.User;
import com.rsc.moneta.util.PasswordGenerator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.InvalidPreferencesFormatException;

/**
 *
 * @author sulic
 */
public class RegisterByPhoneNumber extends BaseAction {

    private String phone;
    private Long paymentId;
    private PaymentOrder paymentKey;

    @Override
    public String execute() throws Exception {
        if (phone != null && !"".equals(phone)) {
            User user = new User();
            user.setPhone(phone);
            user.setPassword(generatePassword());
            Dao dao = new Dao(em);
            dao.persist(user);
            Sms sms = new Sms();
            sms.setPhone(phone);
            sms.setMessage(getText("reg_phone_sms_message", user.getPassword()));
            dao.persist(sms);
            if (paymentId != null){
                paymentKey = em.find(PaymentOrder.class, paymentId);
                if (paymentKey == null){
                    addActionError(getText("payment_key_not_found"));
                    return Action.ERROR;
                }
            }
        }
        return Action.SUCCESS;
    }

    public PaymentOrder getPaymentKey() {
        return paymentKey;
    }

    public void setPaymentKey(PaymentOrder paymentKey) {
        this.paymentKey = paymentKey;
    }


    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public String generatePassword() throws InvalidPreferencesFormatException {
        // Генерация пароля
        PasswordGenerator passGen = new PasswordGenerator();
        passGen.clearTemplate();
        passGen.setNumbersIncluded(true);
        passGen.generatePassword();
        return passGen.getPassword();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
