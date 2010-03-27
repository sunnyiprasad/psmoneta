/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsc.moneta.action.moneta;

import com.rsc.moneta.action.BaseAction;



/**
 *
 * @author sulic
 */
public class Assistant extends BaseAction{
    
    private Long MNT_ID;
    private Long MNT_TRANSACTION_ID;
    private String MNT_CURRENCY_CODE;
    private Double MNT_AMOUNT;
    private Boolean MNT_TEST_MODE;
    private String MNT_DESCRIPTION;
    private String MNT_SIGNATURE;
    private String MNT_CUSTOM1;
    private String MNT_CUSTOM2;
    private String MNT_CUSTOM3;
    private String MNT_SUCCESS_URL;
    private String MNT_FAIL_URL;
    private String monetaLocale;
    private String paymentSystemUnitId;
    private String paymentSystemLimitIds;

    @Override
    public String execute() throws Exception {
        // TODO: Здесь должен быть обработчик.
        // Сюда придет запрос от ТАИСа с том, что билет куплен
        // и необходио быть готовым принять платеж за него.
        // описание параметров смотрите в MONETA.Assistant
        // см.стр. 13
        // По идее после обработки у пользователя необходимо запросить номер телефона
        // И отправить пользователя на страницу с поддерживаемыми платежными системами.
        return super.execute();
    }

    public Double getMNT_AMOUNT() {
        return MNT_AMOUNT;
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

    public Long getMNT_TRANSACTION_ID() {
        return MNT_TRANSACTION_ID;
    }

    public void setMNT_TRANSACTION_ID(Long MNT_TRANSACTION_ID) {
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



}
