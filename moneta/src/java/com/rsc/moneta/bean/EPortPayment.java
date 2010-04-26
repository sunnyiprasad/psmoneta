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
public class EPortPayment implements Serializable {
    
    // Уникальный идентификатор платежа, выполненного по протоколу e-port в ПС
    // ТЛСМ
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // Уникальный идентификатор платежа в ПС, работающей по протоколу e-port –
    // целое 32-битное число, от 0 до 2^32-1
    @Column(name = "transactionId", nullable = false)
    private int transactionId;

    // Идентификатор терминальной ПС, работающей по протоколу e-port
    @Column(name = "paymentSystemId", nullable = false)
    private short paymentSystemId;

    // Идентификатор кода заказа ПС ТЛСМ
    @Column(insertable = false, updatable = false, nullable = true)
    private long paymentOrderId;

    @OneToOne
    @JoinColumn(name = "paymentOrderId")
    private PaymentOrder paymentOrder;

    // Сумма платежа
    @Column(name = "amount", nullable = false)
    private double amount;

    // Дата/время принятия платежа терминальной ПС, работающей по протоколу
    // e-port
    @Column(name = "paydate")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date payDate;
    
    // Дата/время зачисления в ПС ТЛСМ успешного платежа терминальной ПС,
    // работающей по  протоколу e-port
    @Column(name = "accepteddate")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date acceptedDate;

    // Дата/время время аннулирования зачисленнного в ПС ТЛСМ успешного платежа
    // терминальной ПС, работающей по  протоколу e-port
    @Column(name = "revokeddate")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date revokedDate;

    // Статус в ПС ТЛСМ платежа терминальной ПС, работающей по протоколу e-port
    @Column(name = "status", length=2)
    private String status;
    
    // Комментарий к операции аннулирования в ПС ТЛСМ платежа терминальной ПС,
    // работающей по протоколу e-port
    @Column(name = "revokedComment", length = 255)
    private String revokedComment;

    // Колонка, добавленная мной потому что Сулик ввёл её в OSMPPayment
    private String description;


    // Геттеры-сеттеры
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getTransactionId() {
        return this.transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }
    
    public short getPaymentSystemId() {
        return this.paymentSystemId;
    }

    public void setPaymentSystemId(short paymentSystemId) {
        this.paymentSystemId = paymentSystemId;
    }

    public long getPaymentOrderId() {
        return this.paymentOrderId;
    }

    public void setPaymentOrderId(long paymentOrderId) {
        this.paymentOrderId = paymentOrderId;
    }

    public PaymentOrder getPaymentOrder() {
        return this.paymentOrder;
    }

    public void setPaymentOrder(PaymentOrder paymentOrder) {
        this.paymentOrder = paymentOrder;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getPayDate() {
        return this.payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    public Date getAcceptedDate() {
        return this.acceptedDate;
    }

    public void setAcceptedDate(Date acceptedDate) {
        this.acceptedDate = acceptedDate;
    }

    public Date getRevokedDate() {
        return this.revokedDate;
    }

    public void setRevokedDate(Date revokedDate) {
        this.revokedDate = revokedDate;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRevokedComment() {
        return this.revokedComment;
    }

    public void setRevokedComment(String revokedComment) {
        this.revokedComment = revokedComment;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
