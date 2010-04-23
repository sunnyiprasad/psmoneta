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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

/**
 *
 * @author sulic
 * Данный класс описывает счет клиента в нашей системе.
 * Клиент может иметь несколько счетов, которые могут отличаться между собой типом счета:
 * евро, доллар, рублевый и т.д.
 */
@Entity
public class Account implements Serializable{
    
    // Типы возможного периода обналичивания средств.
    public static int EVERYDAY = 0;
    public static int EVERYWEEK = 1;
    public static int EVERYMONTH = 2;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Это тип валюты счета
    @Column(nullable=false, name="_type")
    private int type;

    // Это баланс счета.
    @Column(nullable=false)
    private double balance;

    
    // Обналичивать деньги со счета автоматически.
    // Если стоит нуль тогда автоматически деньги со счеты не обналичиваются.
    // Что бы обналичивать деньги автоматически у клиента должен быть задан
    // счет в банке, куда переводить средства.
    private Integer autoCheckoutMoney;

    @OneToMany(mappedBy = "account")
    private List<PaymentOrder> paymentOrders;

    // Это пользователь владелец счета.
    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "account")
    private List<CashOrder> cashOrders;
    
    // Каждый магазин должен быть привязан к счету куда его средства переводятся.
    // Каждый магазин должен быть привязан только к одному счету по типу валюты.,
    // т.е. он не может быть привязан одновремменно к 2 рублевым счетам. Он 
    // может быть привязан только к 1 рублевому, к 1 долларовому и к 1 евро счету.
    @ManyToMany(mappedBy = "accounts")
    private List<Market> markets;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Market> getMarkets() {
        return markets;
    }

    public void setMarkets(List<Market> markets) {
        this.markets = markets;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }



}
