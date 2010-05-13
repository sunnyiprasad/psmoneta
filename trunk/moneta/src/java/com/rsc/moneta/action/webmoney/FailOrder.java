/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsc.moneta.action.webmoney;

import com.rsc.moneta.action.BaseAction;
import com.rsc.moneta.bean.PaymentOrder;
import com.rsc.moneta.bean.WebMoneyPayment;
import com.rsc.moneta.dao.WebMoneyPaymentDao;

/**
 *
 * @author sulic
 */
public class FailOrder extends BaseAction {
    // WEBMONEY
    private Integer LMI_PREREQUEST;
    private Double LMI_PAYMENT_AMOUNT;
    private String LMI_PAYMENT_DESC;
    private Long LMI_PAYMENT_NO;
    private String LMI_PAYEE_PURSE;
    private Integer LMI_MODE;
    private String LMI_SYS_TRANS_DATE;
    private String LMI_SYS_INVS_NO;
    private String LMI_SYS_TRANS_NO;
    private String LMI_SECRET_KEY;
    private String LMI_PAYER_PURSE;
    private String LMI_PAYER_WM;
    private String message;
    private String LMI_HASH;
    private String LMI_EURONOTE_EMAIL;
    private String LMI_CASHIER_ATMNUMBERINSIDE;
    private String LMI_EURONOTE_NUMBER;
    private String LMI_PAYMER_EMAIL;
    private String LMI_ATM_WMTRANSID;
    private String LMI_PAYMER_NUMBER;
    private String LMI_CAPITALLER_WMID;

    @Override
    public String execute() throws Exception {
         if (LMI_PAYMENT_NO == null) {
            message = "invalid_payment_number";
        } else {
            PaymentOrder paymentOrder = em.find(PaymentOrder.class, LMI_PAYMENT_NO);
            if (paymentOrder == null) {
                message = getText("payment_order_id_not_found");
            } else {
                WebMoneyPaymentDao dao = new WebMoneyPaymentDao(em);
                WebMoneyPayment payment = dao.getPaymentByWmTransactionId(LMI_SYS_TRANS_NO);
                if (payment != null){
                    payment.setStatus(WebMoneyPayment.FAIL);
                    dao.persist(payment);
                }
            }
        }
        return super.execute();
    }

    public String getLMI_ATM_WMTRANSID() {
        return LMI_ATM_WMTRANSID;
    }

    public void setLMI_ATM_WMTRANSID(String LMI_ATM_WMTRANSID) {
        this.LMI_ATM_WMTRANSID = LMI_ATM_WMTRANSID;
    }

    public String getLMI_CAPITALLER_WMID() {
        return LMI_CAPITALLER_WMID;
    }

    public void setLMI_CAPITALLER_WMID(String LMI_CAPITALLER_WMID) {
        this.LMI_CAPITALLER_WMID = LMI_CAPITALLER_WMID;
    }

    public String getLMI_CASHIER_ATMNUMBERINSIDE() {
        return LMI_CASHIER_ATMNUMBERINSIDE;
    }

    public void setLMI_CASHIER_ATMNUMBERINSIDE(String LMI_CASHIER_ATMNUMBERINSIDE) {
        this.LMI_CASHIER_ATMNUMBERINSIDE = LMI_CASHIER_ATMNUMBERINSIDE;
    }

    public String getLMI_EURONOTE_EMAIL() {
        return LMI_EURONOTE_EMAIL;
    }

    public void setLMI_EURONOTE_EMAIL(String LMI_EURONOTE_EMAIL) {
        this.LMI_EURONOTE_EMAIL = LMI_EURONOTE_EMAIL;
    }

    public String getLMI_EURONOTE_NUMBER() {
        return LMI_EURONOTE_NUMBER;
    }

    public void setLMI_EURONOTE_NUMBER(String LMI_EURONOTE_NUMBER) {
        this.LMI_EURONOTE_NUMBER = LMI_EURONOTE_NUMBER;
    }

    public String getLMI_HASH() {
        return LMI_HASH;
    }

    public void setLMI_HASH(String LMI_HASH) {
        this.LMI_HASH = LMI_HASH;
    }

    public Integer getLMI_MODE() {
        return LMI_MODE;
    }

    public void setLMI_MODE(Integer LMI_MODE) {
        this.LMI_MODE = LMI_MODE;
    }

    public String getLMI_PAYEE_PURSE() {
        return LMI_PAYEE_PURSE;
    }

    public void setLMI_PAYEE_PURSE(String LMI_PAYEE_PURSE) {
        this.LMI_PAYEE_PURSE = LMI_PAYEE_PURSE;
    }

    public String getLMI_PAYER_PURSE() {
        return LMI_PAYER_PURSE;
    }

    public void setLMI_PAYER_PURSE(String LMI_PAYER_PURSE) {
        this.LMI_PAYER_PURSE = LMI_PAYER_PURSE;
    }

    public String getLMI_PAYER_WM() {
        return LMI_PAYER_WM;
    }

    public void setLMI_PAYER_WM(String LMI_PAYER_WM) {
        this.LMI_PAYER_WM = LMI_PAYER_WM;
    }

    public Double getLMI_PAYMENT_AMOUNT() {
        return LMI_PAYMENT_AMOUNT;
    }

    public void setLMI_PAYMENT_AMOUNT(Double LMI_PAYMENT_AMOUNT) {
        this.LMI_PAYMENT_AMOUNT = LMI_PAYMENT_AMOUNT;
    }

    public String getLMI_PAYMENT_DESC() {
        return LMI_PAYMENT_DESC;
    }

    public void setLMI_PAYMENT_DESC(String LMI_PAYMENT_DESC) {
        this.LMI_PAYMENT_DESC = LMI_PAYMENT_DESC;
    }

    public Long getLMI_PAYMENT_NO() {
        return LMI_PAYMENT_NO;
    }

    public void setLMI_PAYMENT_NO(Long LMI_PAYMENT_NO) {
        this.LMI_PAYMENT_NO = LMI_PAYMENT_NO;
    }

    public String getLMI_PAYMER_EMAIL() {
        return LMI_PAYMER_EMAIL;
    }

    public void setLMI_PAYMER_EMAIL(String LMI_PAYMER_EMAIL) {
        this.LMI_PAYMER_EMAIL = LMI_PAYMER_EMAIL;
    }

    public String getLMI_PAYMER_NUMBER() {
        return LMI_PAYMER_NUMBER;
    }

    public void setLMI_PAYMER_NUMBER(String LMI_PAYMER_NUMBER) {
        this.LMI_PAYMER_NUMBER = LMI_PAYMER_NUMBER;
    }

    public Integer getLMI_PREREQUEST() {
        return LMI_PREREQUEST;
    }

    public void setLMI_PREREQUEST(Integer LMI_PREREQUEST) {
        this.LMI_PREREQUEST = LMI_PREREQUEST;
    }

    public String getLMI_SECRET_KEY() {
        return LMI_SECRET_KEY;
    }

    public void setLMI_SECRET_KEY(String LMI_SECRET_KEY) {
        this.LMI_SECRET_KEY = LMI_SECRET_KEY;
    }

    public String getLMI_SYS_INVS_NO() {
        return LMI_SYS_INVS_NO;
    }

    public void setLMI_SYS_INVS_NO(String LMI_SYS_INVS_NO) {
        this.LMI_SYS_INVS_NO = LMI_SYS_INVS_NO;
    }

    public String getLMI_SYS_TRANS_DATE() {
        return LMI_SYS_TRANS_DATE;
    }

    public void setLMI_SYS_TRANS_DATE(String LMI_SYS_TRANS_DATE) {
        this.LMI_SYS_TRANS_DATE = LMI_SYS_TRANS_DATE;
    }

    public String getLMI_SYS_TRANS_NO() {
        return LMI_SYS_TRANS_NO;
    }

    public void setLMI_SYS_TRANS_NO(String LMI_SYS_TRANS_NO) {
        this.LMI_SYS_TRANS_NO = LMI_SYS_TRANS_NO;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
