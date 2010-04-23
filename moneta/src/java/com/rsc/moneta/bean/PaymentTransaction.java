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
import javax.persistence.SequenceGenerator;
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
    private Long id;

    @Column
    private Float amount;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "_date")
    private Date date;

    @ManyToOne
    private User fromUser;

    @ManyToOne
    private User toUser;




    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    

}
