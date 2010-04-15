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
import javax.persistence.SequenceGenerator;

/**
 *
 * @author sulic
 * Это очередь смс сообщений для отправки
 */
@Entity
@SequenceGenerator(
    name="seq_sms",
    sequenceName="seq_sms"
)
public class Sms implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator="seq_sms")
    private Long id;
    private String phone;
    private String message;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    
}
