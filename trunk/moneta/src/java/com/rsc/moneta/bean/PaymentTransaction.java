/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsc.moneta.bean;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

/**
 *
 * @author sulic
 * Это любая транзакция внутри системы.
 * Пока незадействовано.
 * Будет задействовано, когда пользователи будут друг другу проводить платежи.
 */
@Entity
public class PaymentTransaction implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable=false)
    private double amount;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "_date", nullable=false)
    private Date date;

    @Column(insertable=false, updatable=false, nullable=false)
    private long fromUserId;

    @Column(insertable=false, updatable=false, nullable=false)
    private long toUserId;

    @ManyToOne
    @Column(name="fromUserId")
    private User fromUser;

    @ManyToOne
    @Column(name="toUserId")
    private User toUser;




    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    

}
