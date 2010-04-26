/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsc.moneta.bean;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.OneToOne;
import javax.persistence.JoinColumn;


/**
 *
 * @author Солодовников Д.А.
 * Класс, представляющий собой запись о пополнении баланса в счёт оплаты заказа
 * посредством терминальной ПС e-port
 */
@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {"transactionId", "paymentSystemId"})})
public class EPortPayment {
    
    // Уникальный идентификатор платежа, выполненного по протоколу e-port в ПС
    // ТЛСМ
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // Уникальный идентификатор платежа в ПС, работающей по протоколу e-port –
    // целое число длиной до 20 знаков.
    // TODO: Денис - в БД это поле должно быть Numeric 20,0
    @Column(name = "transactionId", nullable = false)
    private double transactionId;



}
