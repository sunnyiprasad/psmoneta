/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsc.moneta.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author sulic
 * PaymentOrder это короче говоря один заказ.
 * Он генерится когда присылает ТАИС мне запрос
 * что пользователь заказ билет и хочет оплатить
 */
@Entity
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"transactionId", "marketId"})})
public class PaymentOrder implements Serializable {

    // Идентификатор кода заказа ПС ТЛСМ
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // Это номер заказа в системе интернет магазина.
    @Column(name = "transactionId")
    private String transactionId;

    // Сумма заказа
    @Column(nullable=false)
    private Double amount;

    // Это тестовый платеж?
    @Column(nullable=false)
    private boolean test = false;
    // Описание
    private String description;
    private String custom1;
    private String custom2;
    private String custom3;
    // Куда отправть пользователя после успешной оплаты.
    private String successUrl;
    // Куда отправить пользователь при ошибке об оплате.
    private String failUrl;


    private String paymentSystemUnitId;
    private String paymentSystemLimitIds;

    // Код валюты. Оплату можно производить в разных валютах
    // пока не используется вся валюту это рубль.
    @Column(nullable=false)
    private int currency;

    // Статус заказа ТЛСМ
    @Column(name = "orderstatus", nullable=false)
    private int status = PaymentOrderStatus.ORDER_STATUS_ACCEPTED;

    @Column(name = "_date", nullable=false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date date;

    @Column(insertable=false, updatable=false, nullable=false)
    private long marketId;

    @Column(insertable=false, updatable=false, nullable=false)
    private long accountId;

    @ManyToOne
    @JoinColumn(name = "marketId")
    private Market market;

    @ManyToOne
    @JoinColumn(name="accountId")
    private Account account;

    @OneToMany(mappedBy = "paymentOrder")
    private List<PaymentParameter> paymentParameters;

    @ManyToOne
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }    

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
    
    public long getMarketId() {
        return marketId;
    }

    public void setMarketId(long marketId) {
        this.marketId = marketId;
    }

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }



    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
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

    public boolean getTest() {
        return test;
    }

    public void setTest(boolean test) {
        this.test = test;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int orderStatus) {
        this.status = orderStatus;
    }

    public static List getStatusList(Locale locale){
        try {            
            ResourceBundle bundle = ResourceBundle.getBundle("PaymentOrder", locale);            
            Enumeration e = bundle.getKeys();
            List l = new ArrayList();
            while (e.hasMoreElements()) {
                PaymentOrderStatus status = new PaymentOrderStatus();
                status.setId(Integer.parseInt((String)e.nextElement()));
                status.setDescription(bundle.getString(status.getId()+""));
                l.add(status);
            }
            return l;
        } catch (Exception exception) {
            exception.printStackTrace();
            return new Vector();
        }
    }
    
}

