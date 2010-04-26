/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsc.moneta.bean;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author sulic
 */
@Entity
public class Market implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable=false)
    private String name;

    // Емайл куда будет высылаться реестр платежей
    @Column
    private String paymentListSendEmail;

    // URL куда отправлять чек запрос
    @Column(nullable=false)
    private String checkUrl;

    // Куда отправлять pay запрос
    @Column(nullable=false)
    private String payUrl;

    //
    @Column(nullable=false)
    private String successUrl;

    @Column(nullable=false)
    private String failUrl;

    @Column(nullable=false)
    private String password;

    @Column(nullable=false) // Данное поле показывает требовать ли обязательно подпись при каждом запросе от магазина.
    private boolean signable;

    // Тип протокола по которому мы взаимодействуем с Магазином.
    @Column(nullable=false)
    private int outputHandlerType = 0;

    @Column(insertable=false,updatable=false,nullable=false)
    private long userId;

    @ManyToOne
    @JoinColumn(name="userId")
    private User user;

    @OneToMany(mappedBy = "market")
    private List<PaymentOrder> paymentKeys;

    @ManyToMany
    private List<Account> accounts;

    public String getPaymentListSendEmail() {
        return paymentListSendEmail;
    }

    public void setPaymentListSendEmail(String paymentListSendEmail) {
        this.paymentListSendEmail = paymentListSendEmail;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }



    public int getOutputHandlerType() {
        return outputHandlerType;
    }

    public void setOutputHandlerType(int outputHandlerType) {
        this.outputHandlerType = outputHandlerType;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public boolean isSignable() {
        return signable;
    }

    public void setSignable(boolean signable) {
        this.signable = signable;
    }

    public String getCheckUrl() {
        return checkUrl;
    }

    public void setCheckUrl(String checkUrl) {
        this.checkUrl = checkUrl;
    }

    public String getPayUrl() {
        return payUrl;
    }

    public void setPayUrl(String payUrl) {
        this.payUrl = payUrl;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFailUrl() {
        return failUrl;
    }

    public void setFailUrl(String failUrl) {
        this.failUrl = failUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PaymentOrder> getPaymentKeys() {
        return paymentKeys;
    }

    public void setPaymentKeys(List<PaymentOrder> paymentKeys) {
        this.paymentKeys = paymentKeys;
    }

    public String getSuccessUrl() {
        return successUrl;
    }

    public void setSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Account getAccount(int currency){
        for (Account account : accounts) {
            if (account.getType()==currency)
                return account;
        }
        return null;
    }
}
