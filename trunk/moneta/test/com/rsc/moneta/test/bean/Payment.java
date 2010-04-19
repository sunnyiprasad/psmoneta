/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsc.moneta.test.bean;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author sulic
 */
@Entity
@Table(name="TestPayment")
public class Payment implements Serializable {    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name="_date")
    private Date date;
    private double amount;

    @OneToOne
    private Order order;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    
}
