/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsc.moneta.bean;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

/**
 *
 * @author sulic
 * Это любая транзакция внутри системы.
 * Пока незадействовано.
 * Будет задействовано, когда пользователи будут друг другу проводить платежи.
 */
@Entity
public class PaymentTransaction implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable=false)
    private double amount;

    @Column
    private String description;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "_date", nullable=false)
    private Date date;

    @Column(insertable=false, updatable=false, nullable=true)
    private Long fromAccountId;

    @Column(insertable=false, updatable=false, nullable=false)
    private long toAccountId;

    @ManyToOne
    @JoinColumn(name="fromAccountId")
    private User fromAccount;

    @ManyToOne
    @JoinColumn(name="toAccountId")
    private User toAccount;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public User getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(User fromAccount) {
        this.fromAccount = fromAccount;
    }

    public Long getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(Long fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public User getToAccount() {
        return toAccount;
    }

    public void setToAccount(User toAccount) {
        this.toAccount = toAccount;
    }

    public long getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(long toAccountId) {
        this.toAccountId = toAccountId;
    }

    @Override
    public String toString() {
        return this.id+"\t"+this.amount+"\t"+this.fromAccountId+"\t"+this.toAccountId;
    }

}
