/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsc.moneta.bean;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.SequenceGenerator;
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
 * посредством терминальной ПС ОСМП
 */

@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {"transactionId", "paymentSystemId"})})
public class OSMPPayment implements Serializable {

    // TODO: Денис - удалить после того как станет ясно, что писать в это поле
//    // Терминальные ПС, работающие по протоколу ОСМП:
//    // ПЛАТИКА
//    public final static int OSMP_PS_PLATIKA = 1;
//    // ОСМП
//    public final static int OSMP_PS_OSMP = 2;

    // Уникальный идентификатор платежа, выполненного по протоколу ОСМП в ПС
    // ТЛСМ
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // Уникальный идентификатор платежа в ПС, работающей по протоколу ОСМП –
    // целое число длиной до 20 знаков.
    // TODO: Денис - в БД это поле должно быть Numeric 20,0
    @Column(name = "transactionId", nullable = false)
    private double transactionId;

    @Column(nullable=false)
    private int resultCode;   
    private String description;

    // Идентификатор терминальной ПС, работающей по протоколу ОСМП
    @Column(name = "paymentSystemId", nullable = false)
    private short paymentSystemId;

    // Идентификатор кода заказа ПС ТЛСМ
    @Column(insertable = false, updatable = false, nullable = false)
    private long paymentOrderId;
    @OneToOne
    @JoinColumn(name = "paymentOrderId")
    private PaymentOrder paymentOrder;

    // Сумма платежа
    @Column(name = "amount", nullable = false)
    private double amount;

    // Дата/время выполнения платежа (поступления валидного запроса "pay",
    // обработанного ПС ТЛСМ)
    @Column(name = "paydate")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date payDate;

    // Дата/время отмены ранее выполнного платежа
    @Column(name = "rejectdate")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date rejectDate;

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

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }



    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getTransactionId() {
        return this.transactionId;
    }

    public void setTransactionId(double transactionId) {
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

    public Date getPayDate() {
        return this.payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    public Date getRejectDate() {
        return this.rejectDate;
    }

    public void setRejectDate(Date rejectDate) {
        this.rejectDate = rejectDate;
    }
}
