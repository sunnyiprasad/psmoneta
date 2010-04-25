/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsc.moneta.module;

import java.util.Properties;

/**
 *
 * @author sulic
 */
public class CheckResponse {
    private Long marketId;
    private String transactionId;
    // private ResultCode resultCode;
    private int resultCode;
    private Double amount;
    private Long operationId;
    private String name;
    private String signature;
    private String description;
    private Properties attributes = new Properties();

    public Properties getAttributes() {
        return attributes;
    }

    public void setAttributes(Properties attributes) {
        this.attributes = attributes;
    }

    

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Long getMarketId() {
        return marketId;
    }

    public void setMarketId(Long marketId) {
        this.marketId = marketId;
    }

   

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getOperationId() {
        return operationId;
    }

    public void setOperationId(Long operationId) {
        this.operationId = operationId;
    }

//    public ResultCode getResultCode() {
//        return resultCode;
//    }
//
//    public void setResultCode(ResultCode resultCode) {
//        this.resultCode = resultCode;
//    }
    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }


    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void copyFrom(CheckResponse response) {
        amount = response.getAmount();
        description = response.getDescription();
        marketId = response.getMarketId();
        name = response.getName();
        operationId = response.getOperationId();
        resultCode = response.getResultCode();
        transactionId = response.getTransactionId();
    }


}
