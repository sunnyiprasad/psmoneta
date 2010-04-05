/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsc.moneta.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;

/**
 *
 * @author sulic
 */
@Entity
public class PaymentKey implements Serializable {
    @OneToMany(mappedBy = "key")
    private List<PaymentParameter> paymentParameters;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "_key")
    private String key;

    private Double amount;
    private Boolean test;
    private String description;
    private String custom1;
    private String custom2;
    private String custom3;
    private String successUrl;
    private String failUrl;
    private String monetaLocale;
    private String paymentSystemUnitId;
    private String paymentSystemLimitIds;
    private int currency;


    @Column(name = "_date")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date date;

    @ManyToOne
    private Market market;

    public int getCurrency() {
        return currency;
    }

    public void setCurrency(int currency) {
        this.currency = currency;
    }

    public List<PaymentParameter> getPaymentParameters() {
        return paymentParameters;
    }

    public void setPaymentParameters(List<PaymentParameter> paymentParameters) {
        this.paymentParameters = paymentParameters;
    }

    public Market getMarket() {
        return market;
    }

    public void setMarket(Market market) {
        this.market = market;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCustom1() {
        return custom1;
    }

    public void setCustom1(String custom1) {
        this.custom1 = custom1;
    }

    public String getCustom2() {
        return custom2;
    }

    public void setCustom2(String custom2) {
        this.custom2 = custom2;
    }

    public String getCustom3() {
        return custom3;
    }

    public void setCustom3(String custom3) {
        this.custom3 = custom3;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFailUrl() {
        return failUrl;
    }

    public void setFailUrl(String failUrl) {
        this.failUrl = failUrl;
    }

    public String getMonetaLocale() {
        return monetaLocale;
    }

    public void setMonetaLocale(String monetaLocale) {
        this.monetaLocale = monetaLocale;
    }

    public String getPaymentSystemLimitIds() {
        return paymentSystemLimitIds;
    }

    public void setPaymentSystemLimitIds(String paymentSystemLimitIds) {
        this.paymentSystemLimitIds = paymentSystemLimitIds;
    }

    public String getPaymentSystemUnitId() {
        return paymentSystemUnitId;
    }

    public void setPaymentSystemUnitId(String paymentSystemUnitId) {
        this.paymentSystemUnitId = paymentSystemUnitId;
    }

    public String getSuccessUrl() {
        return successUrl;
    }

    public void setSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
    }

    public Boolean getTest() {
        return test;
    }

    public void setTest(Boolean test) {
        this.test = test;
    }

    
    
}
