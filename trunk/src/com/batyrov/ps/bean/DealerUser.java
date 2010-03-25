package com.batyrov.ps.bean;

import com.batyrov.ps.Dao;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: sulic
 * Date: 16.03.2008
 * Time: 16:15:49
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "T_DEALERUSER")
public class DealerUser {

    public static final int DEALER_ADMIN = 0;
    public static final int DEALER_ADMIN_CANCEL = 1;    

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Column(name = "NAME")
    private String name;
    @Column(name = "PHONE")
    private String phone;
    @Column(name = "STATUS")
    private boolean status;
    @Column(name = "TYPE1")
    private int type;
    @Column(name = "DEALERID", insertable = false, nullable = false, updatable = false)
    private long dealerId;
    @Column(name = "PUBKEYID", nullable = true, updatable = false)
    private long publicKeyId;

    @ManyToOne
    @JoinColumn(name = "DEALERID")
    private Dealer dealer;

    public Dealer getDealer() {
        return dealer;
    }

    public void setDealer(Dealer dealer) {
        this.dealer = dealer;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

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

    public long getDealerId() {
        return dealerId;
    }

    public void setDealerId(long dealerId) {
        this.dealerId = dealerId;
    }

    public long getPublicKeyId() {
        return publicKeyId;
    }

    public void setPublicKeyId(long publicKeyId) {
        this.publicKeyId = publicKeyId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    

    @PrePersist
    void prePersist() {
        Key key = new Key();
        Dao dao = new Dao();
        dao.persist(key);
        this.publicKeyId = key.getId();
    }
}
