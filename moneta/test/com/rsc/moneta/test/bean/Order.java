/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsc.moneta.test.bean;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author sulic
 */
@Entity
@Table(name="TestOrder")
public class Order implements Serializable {
    static int STATUS_PAID;
    @OneToOne(mappedBy = "order")
    private Payment payment;
    static int STATUS_INACTIVE;
    static int STATUS_ACTIVE;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;    
    private String transactionId;
    private double amount;
    private String description;
    private int status;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

   

}
