package com.batyrov.ps.bean;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: sulic
 * Date: 14.02.2008
 * Time: 22:02:58
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "T_PROVIDER")
public class Provider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "CHECKURL")
    private String checkUrl;
    @Column(name = "PAYMENTURL")
    private String paymentUrl;
    @Column(name = "GETSTATUSURL")
    private String getStatusUrl;
    @Column(name = "CLASSNAME")
    private String className;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getCheckUrl() {
        return checkUrl;
    }

    public void setCheckUrl(String checkUrl) {
        this.checkUrl = checkUrl;
    }

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }

    public String getGetStatusUrl() {
        return getStatusUrl;
    }

    public void setGetStatusUrl(String getStatusUrl) {
        this.getStatusUrl = getStatusUrl;
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

    @OneToMany(mappedBy = "provider", fetch = FetchType.LAZY)
    private Collection<AbonentPayment> payments;

    public Collection<AbonentPayment> getPayments() {
        return payments;
    }

    public void setPayments(Collection<AbonentPayment> payments) {
        this.payments = payments;
    }
}
