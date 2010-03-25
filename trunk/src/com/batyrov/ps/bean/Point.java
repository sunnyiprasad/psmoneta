package com.batyrov.ps.bean;

import com.batyrov.ps.Dao;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: sulic
 * Date: 14.02.2008
 * Time: 22:17:25
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "T_POINT")
public class Point {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")    
    private long id;
    @Column(insertable = false, nullable = false, updatable = false, name = "DEALERID")
    private long dealerId;

    @Column(name = "BALANCE")
    private double balance;

    @Column(name = "NAME")
    private String name;

    @Column(name = "ADDRESS")
    private String address;
    
    @Column(name = "PHONE")
    private String phone;

    @Column(name = "REGION")
    private int region;

    @Column(name = "TYPE1")
    private int type;

    @Column(name = "STATUS")
    private boolean status;

    @Column(name = "PUBKEYID", updatable = false)
    private Long publicKeyId;

    @OneToMany(mappedBy = "point", fetch = FetchType.LAZY)
    private Collection<AbonentPayment> payments;    

    @OneToMany(mappedBy = "point", fetch = FetchType.LAZY)
    private Collection<User> users;

    @ManyToOne
    @JoinColumn(name = "DEALERID")
    private Dealer dealer;
    public static final int API = 0;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<AbonentPayment> getPayments() {
        return payments;
    }

    public void setPayments(Collection<AbonentPayment> payments) {
        this.payments = payments;
    }

    public long getPublicKeyId() {
        return publicKeyId;
    }

    public void setPublicKeyId(long publicKeyId) {
        this.publicKeyId = publicKeyId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Collection<User> getUsers() {
        return users;
    }

    public void setUsers(Collection<User> users) {
        this.users = users;
    }

    public Dealer getDealer() {
        return dealer;
    }

    public void setDealer(Dealer dealer) {
        this.dealer = dealer;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDealerId() {
        return dealerId;
    }

    public void setDealerId(long dealerId) {
        this.dealerId = dealerId;
    }

    public int getRegion() {
        return region;
    }

    public void setRegion(int region) {
        this.region = region;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @PrePersist
    void prePersist() {
        if (this.type == Point.API){
            Key key = new Key();
            Dao dao = new Dao();
            dao.persist(key);
            this.publicKeyId = key.getId();
        }
    }


}
