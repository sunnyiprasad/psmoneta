/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsc.moneta.bean;

import java.io.Serializable;
import java.util.List;
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
    @OneToMany(mappedBy = "user")
    private List<PaymentKey> paymentKeys;
    @OneToMany(mappedBy = "fromUser")
    private List<PaymentTransaction> paymentTransactionsFrom;

    @OneToMany(mappedBy = "toUser")
    private List<PaymentTransaction> paymentTransactionsTo;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String phone;
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<PaymentKey> getPaymentKeys() {
        return paymentKeys;
    }

    public void setPaymentKeys(List<PaymentKey> paymentKeys) {
        this.paymentKeys = paymentKeys;
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

    
}
