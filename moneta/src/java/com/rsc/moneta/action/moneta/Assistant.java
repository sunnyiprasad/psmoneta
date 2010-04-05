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
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;

/**
 *
 * @author sulic
 */
public class Assistant extends BaseAction {

    private Long MNT_ID = null;
    private String MNT_TRANSACTION_ID = null;
    private String MNT_CURRENCY_CODE = null;
    private Double MNT_AMOUNT = null;

    private Boolean MNT_TEST_MODE = false;
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

    private String locale = null;
    private String unitId = null;
    private String limitIds = null;

    public String getLimitIds() {
        return limitIds;
    }

    public void setLimitIds(String limitIds) {
        this.limitIds = limitIds;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }



    @Override
    public String execute() throws Exception {
        //  Здесь должен быть обработчик.
        // Сюда придет запрос от ТАИСа с том, что билет куплен
        // и необходио быть готовым принять платеж за него.
        // описание параметров смотрите в MONETA.Assistant
        // см.стр. 13
        // По идее после обработки у пользователя необходимо запросить номер телефона
        // И отправить пользователя на страницу с поддерживаемыми платежными системами.        
        HttpServletRequest request = ServletActionContext.getRequest();
        String _monetaLocale = request.getParameter("moneta.locale");
        if (_monetaLocale != null) {
            this.monetaLocale = _monetaLocale;
        }
        String _paymentSystemUnitId = request.getParameter("paymentSystem.unitId");
        if (_paymentSystemUnitId != null) {
            this.paymentSystemUnitId = _paymentSystemUnitId;
        }
        String _paymentSystemLimitIds = request.getParameter("paymentSystem.limitIds");
        if (_paymentSystemLimitIds != null) {
            this.paymentSystemLimitIds = _paymentSystemLimitIds;
        }
        if (MNT_ID == null) {
            addActionError(getText("MNT_ID_not_defined"));
            return Action.ERROR;
        }
        market = em.find(Market.class, MNT_ID);
        if (market == null) {
            addActionError(getText("market_not_found"));
            return Action.ERROR;
        }
        if (MNT_SIGNATURE != null && market.isSignable()) {
            if (checkSignature()) {
                addActionError(getText("invalid_signature"));
                return Action.ERROR;
            }
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

        paymentKey = new PaymentKey();
        paymentKey.setDate(new Date(System.currentTimeMillis()));
        paymentKey.setAmount(MNT_AMOUNT);
        paymentKey.setKey(MNT_TRANSACTION_ID);
        paymentKey.setMarket(market);
        paymentKey.setCustom1(MNT_CUSTOM1);
        paymentKey.setCustom2(MNT_CUSTOM2);
        paymentKey.setCustom3(MNT_CUSTOM3);
        paymentKey.setDescription(MNT_DESCRIPTION);
        paymentKey.setFailUrl(MNT_FAIL_URL);
        paymentKey.setCurrency(Utils.currencyStringToAccountType(MNT_CURRENCY_CODE));
        if (MNT_SUCCESS_URL != null)
            paymentKey.setSuccessUrl(MNT_SUCCESS_URL);
        else
            paymentKey.setSuccessUrl(market.getSuccessUrl());
        paymentKey.setMonetaLocale(_monetaLocale);
        paymentKey.setPaymentSystemLimitIds(_paymentSystemLimitIds);
        paymentKey.setPaymentSystemUnitId(_paymentSystemUnitId);
        paymentKey.setTest(MNT_TEST_MODE);
        if (!new Dao(em).persist(paymentKey)) {
            addActionError(getText("dbms_save_error"));
            return Action.ERROR;
        }
        return Action.SUCCESS;
    }

    public Market getMarket() {
        return market;
    }

    public void setMarket(Market market) {
        this.market = market;
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

    public void setMNT_TEST_MODE(int MNT_TEST_MODE) {
        this.MNT_TEST_MODE = (MNT_TEST_MODE != 0);
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
