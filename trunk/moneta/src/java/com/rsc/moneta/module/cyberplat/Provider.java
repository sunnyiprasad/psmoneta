package com.rsc.moneta.module.cyberplat;

import java.io.Serializable;
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
@SequenceGenerator(
    name="seq_provider",
    sequenceName="seq_provider"
)
public class Provider implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator="seq_provider")
    private long id;
    private String name;
    private String checkUrl;
    private String paymentUrl;
    private String getStatusUrl;
    private String className;
    @OneToMany(mappedBy="provider")
    private Collection<AbonentPayment> payments;

    public String getCheckUrl() {
        return checkUrl;
    }

    public void setCheckUrl(String checkUrl) {
        this.checkUrl = checkUrl;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
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

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }

    public Collection<AbonentPayment> getPayments() {
        return payments;
    }

    public void setPayments(Collection<AbonentPayment> payments) {
        this.payments = payments;
    }
    
}
