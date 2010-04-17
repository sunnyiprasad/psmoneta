package com.rsc.moneta.module.cyberplat;

import com.rsc.moneta.bean.User;
import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: sulic
 * Date: 14.02.2008
 * Time: 22:08:38
 * Данный класс описывает абонентский платеж.
 * Абонентский платеж это платеж самого обычного пользователя, который оплачивает услугу транзитом через нашу систему.
 * Транзитом это означает, что платеж производится из одной платежной системы в другую
 *
 */
@Entity
@SequenceGenerator(
    name="seq_payment",
    sequenceName="seq_payment"
)
public class AbonentPayment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator="seq_payment")
    private long id;
    private String number;
    private String account;
    private String comment;   
    private String authCode;    
    private String transId;
    private int status;
    private int errorMessage;    
    @ManyToOne
    private User user;
    @ManyToOne
    private Provider provider;
    @Column(name = "_session")
    private String session;
    private double amount;
    private double amountAll;
    @Column(name = "_date")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date date;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date startDate;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date endDate;

    public final static int SUCCESS = 0;
    public static final int NEW = 1;
    public static final int CHECKED = 2;
    public static final int PAYMENT = 3;
    public static final int CHECKED_ERROR = 4;
    public static final int PAYMENT_ERROR = 5;
    public static final int ERROR = 6;
    public static final int UNKNOWN = 7;
    public static final int PROCESSING = 8;

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }



    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getAmountAll() {
        return amountAll;
    }

    public void setAmountAll(double amountAll) {
        this.amountAll = amountAll;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(int errorMessage) {
        this.errorMessage = errorMessage;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

   

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

  
}
