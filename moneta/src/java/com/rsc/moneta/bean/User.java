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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    
}
