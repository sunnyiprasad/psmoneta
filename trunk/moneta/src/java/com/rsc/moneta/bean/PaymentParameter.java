/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsc.moneta.bean;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

/**
 *
 * @author sulic
 * Это дополнительные параметры, которые могут быть присланы во время запроса чек к интернет магазину.
 */
@Entity
@SequenceGenerator(
    name="seq_payment_parameter",
    sequenceName="seq_payment_parameter"
)
public class PaymentParameter implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator="seq_payment_parameter")
    private Long id;
    private String name;
    private String val;
    @ManyToOne
    private PaymentOrder paymentOrder;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public PaymentOrder getPaymentOrder() {
        return paymentOrder;
    }

    public void setPaymentOrder(PaymentOrder paymentOrder) {
        this.paymentOrder = paymentOrder;
    }

   
    
}
