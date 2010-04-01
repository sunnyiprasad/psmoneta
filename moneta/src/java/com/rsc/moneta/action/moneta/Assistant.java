/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsc.moneta.action.moneta;

import com.rsc.moneta.bean.Market;
import com.opensymphony.xwork2.Action;
import com.rsc.moneta.action.BaseAction;
import com.rsc.moneta.dao.Dao;
import com.rsc.moneta.bean.PaymentKey;
import com.rsc.moneta.util.Utils;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 *
 * @author sulic
 */
public class Assistant extends BaseAction {

    private Long MNT_ID = null;
    private String MNT_TRANSACTION_ID = null;
    private String MNT_CURRENCY_CODE = null;
    private Double MNT_AMOUNT = null;
    private Boolean MNT_TEST_MODE = true;
    private String MNT_DESCRIPTION = null;
    private String MNT_SIGNATURE = null;
    private String MNT_CUSTOM1 = null;
    private String MNT_CUSTOM2 = null;
    private String MNT_CUSTOM3 = null;
    private String MNT_SUCCESS_URL = null;
    private String MNT_FAIL_URL = null;
    private String monetaLocale = null;
    private String paymentSystemUnitId = null;
    private String paymentSystemLimitIds = null;
    private PaymentKey paymentKey;
    private Market market;

    @Override
    public String execute() throws Exception {
        //  Здесь должен быть обработчик.
        // Сюда придет запрос от ТАИСа с том, что билет куплен
        // и необходио быть готовым принять платеж за него.
        // описание параметров смотрите в MONETA.Assistant
        // см.стр. 13
        // По идее после обработки у пользователя необходимо запросить номер телефона
        // И отправить пользователя на страницу с поддерживаемыми платежными системами.

        if (MNT_ID == null) {
            addActionError(getText("MNT_ID_not_defined"));
            return Action.ERROR;
        }
        market = em.find(Market.class, MNT_ID);
        if (market == null) {
            addActionError(getText("market_not_found"));
            return Action.ERROR;
        }
        if (checkSignature()) {
            addActionError(getText("invalid_signature"));
            return Action.ERROR;
        }
        if (MNT_TRANSACTION_ID == null) {
            addActionError(getText("MNT_TRANSACTION_ID_not_defined"));
            return Action.ERROR;
        }
        if (MNT_TRANSACTION_ID.length() > 255) {
            addActionError(getText("MNT_TRANSACTION_ID_longer_than_255"));
            return Action.ERROR;
        }
        if (MNT_CURRENCY_CODE == null) {
            addActionError(getText("MNT_CURRENCY_CODE_not_defined"));
            return Action.ERROR;
        }
        if (MNT_AMOUNT == null) {
            addActionError(getText("MNT_AMOUNT_not_defined"));
            return Action.ERROR;
        }

        PaymentKey key = new PaymentKey();
        key.setDate(new Date(System.currentTimeMillis()));
        key.setAmount(MNT_AMOUNT);
        key.setKey(MNT_TRANSACTION_ID);
        key.setMarket(market);
        key.setCustom1(MNT_CUSTOM1);
        key.setCustom2(MNT_CUSTOM2);
        key.setCustom3(MNT_CUSTOM3);
        key.setDescription(MNT_DESCRIPTION);
        key.setFailUrl(MNT_FAIL_URL);
        key.setSuccessUrl(MNT_SUCCESS_URL);
        key.setMonetaLocale(monetaLocale);
        key.setPaymentSystemLimitIds(paymentSystemLimitIds);
        key.setPaymentSystemUnitId(paymentSystemUnitId);
        key.setTest(MNT_TEST_MODE);
        if (!new Dao(em).persist(key)) {
            addActionError(getText("dbms_save_error"));
            return Action.ERROR;
        }
        return Action.SUCCESS;
    }

    public PaymentKey getPaymentKey() {
        return paymentKey;
    }

    public void setPaymentKey(PaymentKey paymentKey) {
        this.paymentKey = paymentKey;
    }

    public Double getMNT_AMOUNT() {
        return MNT_AMOUNT;
    }

    public void setMNT_AMOUNT(String MNT_AMOUNT) {
        this.MNT_AMOUNT = Double.parseDouble(MNT_AMOUNT);
    }

    public void setMNT_AMOUNT(Double MNT_AMOUNT) {
        this.MNT_AMOUNT = MNT_AMOUNT;
    }

    public String getMNT_CURRENCY_CODE() {
        return MNT_CURRENCY_CODE;
    }

    public void setMNT_CURRENCY_CODE(String MNT_CURRENCY_CODE) {
        this.MNT_CURRENCY_CODE = MNT_CURRENCY_CODE;
    }

    public String getMNT_CUSTOM1() {
        return MNT_CUSTOM1;
    }

    public void setMNT_CUSTOM1(String MNT_CUSTOM1) {
        this.MNT_CUSTOM1 = MNT_CUSTOM1;
    }

    public String getMNT_CUSTOM2() {
        return MNT_CUSTOM2;
    }

    public void setMNT_CUSTOM2(String MNT_CUSTOM2) {
        this.MNT_CUSTOM2 = MNT_CUSTOM2;
    }

    public String getMNT_CUSTOM3() {
        return MNT_CUSTOM3;
    }

    public void setMNT_CUSTOM3(String MNT_CUSTOM3) {
        this.MNT_CUSTOM3 = MNT_CUSTOM3;
    }

    public String getMNT_DESCRIPTION() {
        return MNT_DESCRIPTION;
    }

    public void setMNT_DESCRIPTION(String MNT_DESCRIPTION) {
        this.MNT_DESCRIPTION = MNT_DESCRIPTION;
    }

    public String getMNT_FAIL_URL() {
        return MNT_FAIL_URL;
    }

    public void setMNT_FAIL_URL(String MNT_FAIL_URL) {
        this.MNT_FAIL_URL = MNT_FAIL_URL;
    }

    public Long getMNT_ID() {
        return MNT_ID;
    }

    public void setMNT_ID(Long MNT_ID) {
        this.MNT_ID = MNT_ID;
    }

    public String getMNT_SIGNATURE() {
        return MNT_SIGNATURE;
    }

    public void setMNT_SIGNATURE(String MNT_SIGNATURE) {
        this.MNT_SIGNATURE = MNT_SIGNATURE;
    }

    public String getMNT_SUCCESS_URL() {
        return MNT_SUCCESS_URL;
    }

    public void setMNT_SUCCESS_URL(String MNT_SUCCESS_URL) {
        this.MNT_SUCCESS_URL = MNT_SUCCESS_URL;
    }

    public Boolean getMNT_TEST_MODE() {
        return MNT_TEST_MODE;
    }

    public void setMNT_TEST_MODE(Boolean MNT_TEST_MODE) {
        this.MNT_TEST_MODE = MNT_TEST_MODE;
    }

    public String getMNT_TRANSACTION_ID() {
        return MNT_TRANSACTION_ID;
    }

    public void setMNT_TRANSACTION_ID(String MNT_TRANSACTION_ID) {
        this.MNT_TRANSACTION_ID = MNT_TRANSACTION_ID;
    }

    public String getMonetaLocale() {
        return monetaLocale;
    }

    public void setMonetaLocale(String monetaLocale) {
        this.monetaLocale = monetaLocale;
    }

    public String getPaymentSystemLimitIds() {
        return paymentSystemLimitIds;
    }

    public void setPaymentSystemLimitIds(String paymentSystemLimitIds) {
        this.paymentSystemLimitIds = paymentSystemLimitIds;
    }

    public String getPaymentSystemUnitId() {
        return paymentSystemUnitId;
    }

    public void setPaymentSystemUnitId(String paymentSystemUnitId) {
        this.paymentSystemUnitId = paymentSystemUnitId;
    }

    private boolean checkSignature() throws NoSuchAlgorithmException {
        int test = (MNT_TEST_MODE) ? 0 : 1;
        String all = MNT_ID + MNT_TRANSACTION_ID + MNT_AMOUNT + MNT_CURRENCY_CODE + test + market.getPassword();
        return (MNT_SIGNATURE.equalsIgnoreCase(Utils.byteArrayToHexString(Utils.md5(all))));
    }
}
