package com.batyrov.ps.bean;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by IntelliJ IDEA.
 * User: sulic
 * Date: 14.02.2008
 * Time: 22:08:58
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(name = "T_DEALERPAYMENT")
public class DealerPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")    
    private long id;
    @Column(insertable = false, nullable = false, updatable = false, name = "FROMDEALERID")
    private long fromDealerId;
    @Column(insertable = false, nullable = false, updatable = false, name = "TODEALERID")
    private long toDealerId;

    @Column(name = "AMOUNT")
    private double amount;

    @Column(name = "DATE1")
    private Timestamp date;

    public long getFromDealerId() {
        return fromDealerId;
    }

    public void setFromDealerId(long fromDealerId) {
        this.fromDealerId = fromDealerId;
    }

    public long getToDealerId() {
        return toDealerId;
    }

    public void setToDealerId(long toDealerId) {
        this.toDealerId = toDealerId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }


    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @ManyToOne
    @JoinColumn(name = "fromDealerId")
    private Dealer fromDealer;

    public Dealer getFromDealer() {
        return fromDealer;
    }

    public void setFromDealer(Dealer fromDealer) {
        this.fromDealer = fromDealer;
    }

    @ManyToOne
    @JoinColumn(name = "toDealerId")
    private Dealer toDealer;

    public Dealer getToDealer() {
        return toDealer;
    }

    public void setToDealer(Dealer toDealer) {
        this.toDealer = toDealer;
    }

    public DealerPayment() {
    }

    public DealerPayment(long fromDealerId, long toDealerId, double amount, Timestamp date) {
        this.fromDealerId = fromDealerId;
        this.toDealerId = toDealerId;
        this.amount = amount;
        this.date = date;
    }
}
