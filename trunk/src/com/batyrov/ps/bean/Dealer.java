package com.batyrov.ps.bean;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: sulic
 * Date: 14.02.2008
 * Time: 22:13:51
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "T_DEALER")
public class Dealer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;
    @Column(name = "NAME")
    private String name;

    @Column(name = "STATUS")
    private boolean status;

    @Column(name = "EMAIL")
    private String email;
    @Column(name = "PHONE")
    private String phone;
    @Column(name = "BALANCE")
    private double balance;
    @Column(insertable = false, nullable = true, updatable = false, name = "OWNERDEALERID")
    private Long ownerDealerId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @OneToMany(mappedBy = "dealer")
    private Collection<DealerUser> users;

    public Collection<DealerUser> getUsers() {
        return users;
    }

    public void setUsers(Collection<DealerUser> users) {
        this.users = users;
    }

    @OneToMany(mappedBy = "dealer", fetch = FetchType.LAZY)
    private Collection<AbonentPayment> payments;

    public Collection<AbonentPayment> getPayments() {
        return payments;
    }

    public void setPayments(Collection<AbonentPayment> payments) {
        this.payments = payments;
    }

    @OneToMany(mappedBy = "dealer", fetch = FetchType.LAZY)
    private Collection<Point> points;

    public Collection<Point> getPoints() {
        return points;
    }

    public void setPoints(Collection<Point> points) {
        this.points = points;
    }

    @OneToMany(mappedBy = "fromDealer", fetch = FetchType.LAZY)
    private Collection<DealerPayment> balancePayments;

    @OneToMany(mappedBy = "toDealer", fetch = FetchType.LAZY)
    private Collection<DealerPayment> dealerPayments;

    public long getOwnerDealerId() {
        return ownerDealerId;
    }

    public void setOwnerDealerId(long ownerDealerId) {
        this.ownerDealerId = ownerDealerId;
    }

    public Collection<DealerPayment> getBalancePayments() {
        return balancePayments;
    }

    public void setBalancePayments(Collection<DealerPayment> balancePayments) {
        this.balancePayments = balancePayments;
    }

    public Collection<DealerPayment> getDealerPayments() {
        return dealerPayments;
    }

    public void setDealerPayments(Collection<DealerPayment> dealerPayments) {
        this.dealerPayments = dealerPayments;
    }

    @ManyToOne
    @JoinColumn(name = "OWNERDEALERID")
    private Dealer ownerDealer;

    public Dealer getOwnerDealer() {
        return ownerDealer;
    }

    public void setOwnerDealer(Dealer ownerDealer) {
        this.ownerDealer = ownerDealer;
    }

    @OneToMany(mappedBy = "ownerDealer")
    private Collection<Dealer> dealers;

    public Collection<Dealer> getDealers() {
        return dealers;
    }

    public void setDealers(Collection<Dealer> dealers) {
        this.dealers = dealers;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
