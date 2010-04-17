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


/**
 *
 * @author Солодовников Д.А.
 * Класс, представляющий собой запись о пополнении баланса в счёт оплаты заказа
 * посредством терминальной ПС ОСМП
 */

@Entity
@SequenceGenerator(
    name = "seq_osmppayment_order",
    sequenceName = "seq_osmppayment_order"
)
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
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_osmppayment_order")
    private long id;

    // Уникальный идентификатор платежа в ПС, работающей по протоколу ОСМП –
    // целое число длиной до 20 знаков.
    // TODO: Денис - в БД это поле должно быть Numeric 20,0
    @Column(name = "transactionId", nullable = false)
    private double transactionId;

    // Идентификатор терминальной ПС, работающей по протоколу ОСМП
    @Column(name = "paymentSystemId", nullable = false)
    private short paymentSystemId;

    // Идентификатор кода заказа ПС ТЛСМ
    @Column(name = "paymentOrderId", nullable = false)
    private long paymentOrderId;

    // Сумма платежа
    @Column(name = "amount", nullable = false)
    private double amount;

    // Дата/время выполнения платежа (поступления валидного запроса "pay",
    // обработанного ПС ТЛСМ)
    @Column(name = "paydate")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date payDate;

    // Дата/время отмены ранее выполнного платежа
    @Column(name = "rejectdate")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date rejectDate;

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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
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
