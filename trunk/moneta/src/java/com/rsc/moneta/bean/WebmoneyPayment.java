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
import javax.persistence.Temporal;

/**
 *
 * @author sulic
 */
@Entity
public class WebmoneyPayment implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String outAccount;
    private String wmid;
    private double amount;
    private boolean mode;
    private String wmInvoiceId;
    private String wmTransactionId;
    private String inAccount;
    private String description;
    private String secretKey;
    @Column(name="_date")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date date;
    @Column(name="_hash")
    private String hash;
    private String paymerNumber;
    private String payerEmail;
    private String euronoteNumber;
    private String euronoteEmail;
    private String atmTransactionId;
    private String terminaltype;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date wmDate;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getAtmTransactionId() {
        return atmTransactionId;
    }

    public void setAtmTransactionId(String atmTransactionId) {
        this.atmTransactionId = atmTransactionId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEuronoteEmail() {
        return euronoteEmail;
    }

    public void setEuronoteEmail(String euronoteEmail) {
        this.euronoteEmail = euronoteEmail;
    }

    public String getEuronoteNumber() {
        return euronoteNumber;
    }

    public void setEuronoteNumber(String euronoteNumber) {
        this.euronoteNumber = euronoteNumber;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getInAccount() {
        return inAccount;
    }

    public void setInAccount(String inAccount) {
        this.inAccount = inAccount;
    }

    public boolean isMode() {
        return mode;
    }

    public void setMode(boolean mode) {
        this.mode = mode;
    }

    public String getOutAccount() {
        return outAccount;
    }

    public void setOutAccount(String outAccount) {
        this.outAccount = outAccount;
    }

    public String getPayerEmail() {
        return payerEmail;
    }

    public void setPayerEmail(String payerEmail) {
        this.payerEmail = payerEmail;
    }

    public String getPaymerNumber() {
        return paymerNumber;
    }

    public void setPaymerNumber(String paymerNumber) {
        this.paymerNumber = paymerNumber;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getTerminaltype() {
        return terminaltype;
    }

    public void setTerminaltype(String terminaltype) {
        this.terminaltype = terminaltype;
    }

    public Date getWmDate() {
        return wmDate;
    }

    public void setWmDate(Date wmDate) {
        this.wmDate = wmDate;
    }

    public String getWmInvoiceId() {
        return wmInvoiceId;
    }

    public void setWmInvoiceId(String wmInvoiceId) {
        this.wmInvoiceId = wmInvoiceId;
    }

    public String getWmTransactionId() {
        return wmTransactionId;
    }

    public void setWmTransactionId(String wmTransactionId) {
        this.wmTransactionId = wmTransactionId;
    }

    public String getWmid() {
        return wmid;
    }

    public void setWmid(String wmid) {
        this.wmid = wmid;
    }

    

}
