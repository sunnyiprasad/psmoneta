/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsc.moneta.bean;

import java.io.Serializable;
import javax.persistence.Column;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Temporal;


/**
 *
 * @author Солодовников Д.А.
 * Класс, представляющий собой запись о пополнении баланса в счёт оплаты заказа
 * посредством терминальной ПС ОСМП
 */
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {"osmptxnid"})})
public class OSMPPayment implements Serializable {
    //userid - ид юзера в ПС ТЛСМ - НЕ НУЖЕН строго говоря
    //account приходящий в запросе от осмп - и есть paymentOrderId
    //amount - сумма платежа
    //paydate - датавремя принятия платежа
    //rejectdate - датавремя отмены платежа
    //osmptxnid - № тр-ции осмп

    @Column(name = "osmptxnid", nullable = false)
    private double OSMPTxnId;


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

    public double getOSMPTxnId() {
        return this.OSMPTxnId;
    }

    public void setOSMPTxnId(double OSMPTxnId) {
        this.OSMPTxnId = OSMPTxnId;
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
