/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsc.moneta.module;

/**
 *
 * @author sulic
 */
public class MonetaReponse {

    Long mntId  = null;
    String mntTransactionId;
    Long mntResultCode;
    Double amount;
    String signature;
    Long operationId;

    public Long getOperationId() {
        return operationId;
    }

    public void setOperationId(Long operationId) {
        this.operationId = operationId;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }



    public Long getMntResultCode() {
        return mntResultCode;
    }

    public void setMntResultCode(Long mntResultCode) {
        this.mntResultCode = mntResultCode;
    }



    public String getMntTransactionId() {
        return mntTransactionId;
    }

    public void setMntTransactionId(String mntTransactionId) {
        this.mntTransactionId = mntTransactionId;
    }



    public Long getMntId() {
        return mntId;
    }

    public void setMntId(Long mntId) {
        this.mntId = mntId;
    }




}
