package com.rsc.moneta.bean;

import com.rsc.moneta.bean.CyberplatPayment;
import java.io.Serializable;
import java.util.List;
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
public class Provider implements Serializable {
    @OneToMany(mappedBy = "provider")
    private List<PSPayment> psPayments;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String checkUrl;
    private String paymentUrl;
    private String getStatusUrl;
    private String className;


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
    
}
