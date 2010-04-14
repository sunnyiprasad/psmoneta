/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsc.moneta.action;

import com.rsc.moneta.bean.Sms;
import com.opensymphony.xwork2.Action;
import com.rsc.moneta.Currency;
import com.rsc.moneta.bean.Account;
import com.rsc.moneta.bean.PaymentOrder;
import com.rsc.moneta.dao.Dao;
import com.rsc.moneta.bean.User;
import com.rsc.moneta.dao.UserDao;
import com.rsc.moneta.util.PasswordGenerator;
import java.util.prefs.InvalidPreferencesFormatException;

/**
 *
 * @author sulic
 */
public class RegisterByPhoneNumber extends BaseAction {

    private String phone;
    private Long paymentId;
    private PaymentOrder paymentKey;
    private String password;

    @Override
    public String execute() throws Exception {
        if (phone != null && !"".equals(phone)) {
            User user = new UserDao(em).getUserByPhone(phone);
            if (user == null) {
                user = new User();
                user.setPhone(phone);
                user.setPassword(generatePassword());
                Account account = new Account();
                account.setType(Currency.EURO);
                account.setUser(user);
                account.setBalance(0);
                Dao dao = new Dao(em);
                dao.persist(user);
                dao.persist(account);
                account = new Account();
                account.setType(Currency.RUB);
                account.setUser(user);
                account.setBalance(0);
                dao.persist(account);
                account = new Account();
                account.setType(Currency.USD);
                account.setUser(user);
                account.setBalance(0);
                dao.persist(account);
                Sms sms = new Sms();
                sms.setPhone(phone);
                sms.setMessage(getText("reg_phone_sms_message", user.getPassword()));
                dao.persist(sms);
                if (paymentId != null) {
                    paymentKey = em.find(PaymentOrder.class, paymentId);
                    if (paymentKey == null) {
                        addActionError(getText("payment_key_not_found"));
                        return Action.ERROR;
                    }
                    paymentKey.setUser(user);
                }
            } else {
                if (user.getPassword().equals(password)) {
                    if (paymentId != null) {
                        paymentKey = em.find(PaymentOrder.class, paymentId);
                        if (paymentKey == null) {
                            addActionError(getText("payment_key_not_found"));
                            return Action.ERROR;
                        }
                        paymentKey.setUser(user);
                    }
                } else  {
                    addActionError(getText("incorrect_password_please_try_again"));
                    return Action.LOGIN;
                }
            }
        }
        return Action.SUCCESS;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
