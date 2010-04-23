/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsc.moneta.bean;

import com.rsc.moneta.module.cyberplat.AbonentPayment;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author sulic
 */
@Entity
@Table(name = "t_User")
public class User implements Serializable {    

    public static final int USER = 0;
    public static final int IMOWNER = 1;
    public static final int BUHGALTER = 2;
    public static final int ADMINISTRATOR = 3;    
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    
    @Column(unique=true, nullable=false)
    private String email;

    @Column(unique=true)
    private String phone;

    @Column(nullable=false)
    private String password;
    
    @Column(name="_role", nullable=false)
    private int role = User.USER;


    @OneToMany(mappedBy = "toUser")
    private List<PaymentTransaction> paymentTransactionsTo;
    @OneToMany(mappedBy = "user")
    private List<Account> accounts;
    @OneToMany(mappedBy = "user")
    private List<Market> markets;
    @OneToMany(mappedBy = "fromUser")
    private List<PaymentTransaction> paymentTransactionsFrom;
    @OneToMany(mappedBy = "user")
    private List<AbonentPayment> abonentPayments;
    @OneToMany(mappedBy = "user")
    private List<PaymentOrder> paymentOrders;
    @OneToMany(mappedBy = "user")
    private List<CashOrder> cashOrders;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AbonentPayment> getAbonentPayments() {
        return abonentPayments;
    }

    public void setAbonentPayments(List<AbonentPayment> abonentPayments) {
        this.abonentPayments = abonentPayments;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<PaymentOrder> getPaymentOrders() {
        return paymentOrders;
    }

    public void setPaymentOrders(List<PaymentOrder> paymentOrders) {
        this.paymentOrders = paymentOrders;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public List<CashOrder> getCashOrders() {
        return cashOrders;
    }

    public void setCashOrders(List<CashOrder> cashOrders) {
        this.cashOrders = cashOrders;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public List<Market> getMarkets() {
        return markets;
    }

    public void setMarkets(List<Market> markets) {
        this.markets = markets;
    }
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<PaymentTransaction> getPaymentTransactionsFrom() {
        return paymentTransactionsFrom;
    }

    public void setPaymentTransactionsFrom(List<PaymentTransaction> paymentTransactionsFrom) {
        this.paymentTransactionsFrom = paymentTransactionsFrom;
    }

    public List<PaymentTransaction> getPaymentTransactionsTo() {
        return paymentTransactionsTo;
    }

    public void setPaymentTransactionsTo(List<PaymentTransaction> paymentTransactionsTo) {
        this.paymentTransactionsTo = paymentTransactionsTo;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Account getAccount(int currency) {
        for (Account account : accounts) {
            if (account.getType() == currency)
                return account;
        }
        return null;
    }

    
}
