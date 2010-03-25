package com.batyrov.ps.bean;

import com.batyrov.ps.Dao;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: sulic
 * Date: 14.02.2008
 * Time: 22:30:16
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table (name = "T_POINTUSER")
public class User {

    public static final int TERMINAL_ADMIN = 0;
    public static final int TERMINAL_OPERATOR = 1;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Column(name = "STATUS")
    private boolean status;

    @Column(name = "TYPE1")
    private int type;

    @Column(name = "NAME")
    private String name;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "BALANCE")
    private long balance;

    @Column(name = "PUBKEYID", nullable = true, updatable = false)
    private Long publicKeyId;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Collection<AbonentPayment> payments;

    @Column(insertable = false, nullable = false, updatable = false, name = "POINTID")
    private long pointId;

    @ManyToOne
    @JoinColumn(name = "POINTID")
    private Point point;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public long getPointId() {
        return pointId;
    }

    public void setPointId(long pointId) {
        this.pointId = pointId;
    }

    public long getPublicKeyId() {
        return publicKeyId;
    }

    public void setPublicKeyId(long publicKeyId) {
        this.publicKeyId = publicKeyId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Collection<AbonentPayment> getPayments() {
        return payments;
    }

    public void setPayments(Collection<AbonentPayment> payments) {
        this.payments = payments;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @PrePersist
    void prePersist() {
        Dao dao = new Dao();
        Point point = dao.getEm().find(Point.class, this.pointId);
        if (point != null){
            if (point.getType() != Point.API){
                Key key = new Key();
                dao.persist(key);
                this.publicKeyId = key.getId();
            }
        }
    }


}
