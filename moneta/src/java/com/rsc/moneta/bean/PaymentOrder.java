/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsc.moneta.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author sulic
 * PaymentOrder это короче говоря один заказ.
 * Он генерится когда присылает ТАИС мне запрос
 * что пользователь заказ билет и хочет оплатить
 */
@Entity
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"transactionId", "marketId"})})
public class PaymentOrder implements Serializable {

    // Статус заказа ТЛСМ - возможные варианты
    // TODO: Сулик, статусы, проверь и добавь статусы если нужно, пжлст
    //  -1: неопределён
    //у нас не должно быть не определенного статуса!!!
    // Денис: на пока всё-таки раскомментирую и попробую объяснить зачем этот статус Сулику :-)
    public final static int ORDER_STATUS_UNDEFINED = -1;

    //   1: заказ от покупателя принят, сохранён в системе ТЛСМ, заказ забронирован в Интернет-Магазине,
    //      Интернет-Магазине об этом "знает", следующий шаг - оплата брони и получение документа об оплате
    //      (билета, чека и т.п.)
    public final static int ORDER_STATUS_ACCEPTED = 1;

    //   2: забронированный ранее но еще не оплаченный заказ Интернет-Магазином признан
    //      инвалидным и отменён
    public final static int ORDER_STATUS_NOT_PAID_AND_REJECTED_BY_EMARKETPLACE = 2;

    //   3: заказ оплачен покупателем, деньги получены в терминальной ПС и ТЛСМ об этом известно,
    //      системой ТЛСМ передано сообщение Интернет-магазину об оплате
    //      В данном случае средства зачисляются на счет ИМ в ТЛСМ.
    public final static int ORDER_STATUS_PAID_AND_COMPLETED = 3;

    //   4: заказ оплачен покупателем, деньги получены в терминальной ПС и ТЛСМ об этом известно,
    //      но системой ТЛСМ не передано сообщение Интернет-магазину об оплате
    //      при этом продолжается обработка платежа. т.е. платеж не закрыт, и должен быть
    //      передан в ИМ.
    //      В данном случае средства не будут зачислены на счет ИМ в ТЛСМ.
    public final static int ORDER_STATUS_PAID_BUT_NOT_COMPLETED_AND_STILL_PROCESSING = 4;

    //   5: заказ оплачен покупателем, деньги получены в терминальной ПС и ТЛСМ об этом известно,
    //      но заказ Интернет-Магазином признан инвалидным и отменён
    //      В данном случае средства не будут зачислены на счет ИМ в ТЛСМ.
    public final static int ORDER_STATUS_PAID_BUT_REJECTED_BY_EMARKETPLACE = 5;
    //   6: заказ успешно оплачен, однако зачислить эти деньги на счет не удалось
    //      по причине отсуствия кошелька у ИМ в нашей системе с данным типом валюты.
    //      Данная ситуация по идее не может быть, однако на всякий случай код введен.
    //      В данном случае средства не будут зачислены на счет ИМ в ТЛСМ.
    public static int ORDER_STATUS_PAID_AND_COMPLETED_BUT_NOT_FOUND_MARKET_ACCOUNT = 6;
    //   7: данный код по сути наврятли возможен. Эта возможно может быть только в ситуации
    //      если запрос пей был отправлен без предварительного чека, либо при чеке ИМ вернул
    //      нормальный ответ, а после чека вернул код не найден заказ
    //      В данном случае средства не будут зачислены на счет ИМ в ТЛСМ.
    public static int ORDER_STATUS_PAID_BUT_ORDER_NOT_FOUND = 7;
    //   8: Данный код маловероятен. Он будет получен в случае если ИМ не нормально отрабатывает запросы нашей системы.
    //      Например в ситуации когда ответ присланный ИМ не соотвествует протоколу
    //      В данном случае средства не будут зачислены на счет ИМ в ТЛСМ.
    //
    public static int ORDER_STATUS_PAID_BUT_EMARKETPLACE_CANNOT_PROCESS_IT = 8;
    //   9: Данный код будет выставлен если платеж прошел успешно но до ИМ не дошел.
    //      Денег было слишком мало и их зачислили на счет абонента в ТЛСМ
    public static int ORDER_STATUS_PAID_AND_COMPLETED_BUT_MONEY_ADDED_ON_ACCOUNT_BALANCE = 9;


    @OneToMany(mappedBy = "key")
    private List<PaymentParameter> paymentParameters;
    private static final long serialVersionUID = 1L;

    // TODO: изменить ххх на нормальное название проекта
    // Идентификатор кода заказа ПС ххх
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    // Это номер заказа в системе интернет магазина.
    @Column(name = "transactionId")
    private String transactionId;

    // Сумма заказа
    private Double amount;
    // Это тестовый платеж?
    private Boolean test;
    // Описание
    private String description;
    private String custom1;
    private String custom2;
    private String custom3;
    // Куда отправть пользователя после успешной оплаты.
    private String successUrl;
    // Куда отправить пользователь при ошибке об оплате.
    private String failUrl;


    private String paymentSystemUnitId;
    private String paymentSystemLimitIds;
    // Код валюты. Оплату можно производить в разных валютах
    // пока не используется вся валюту это рубль.
    private int currency;

    // Статус заказа ТЛСМ
    @Column(name = "orderstatus")
    private int status = ORDER_STATUS_ACCEPTED;

    @Column(name = "_date")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date date;

    @Column(insertable=false, updatable=false, nullable=false)
    private long marketId;
    @ManyToOne
    @JoinColumn(name = "marketId")
    private Market market;

    @ManyToOne
    private Account account;

    @ManyToOne
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
    
    public long getMarketId() {
        return marketId;
    }

    public void setMarketId(long marketId) {
        this.marketId = marketId;
    }

    public int getCurrency() {
        return currency;
    }

    public void setCurrency(int currency) {
        this.currency = currency;
    }

    public List<PaymentParameter> getPaymentParameters() {
        return paymentParameters;
    }

    public void setPaymentParameters(List<PaymentParameter> paymentParameters) {
        this.paymentParameters = paymentParameters;
    }

    public Market getMarket() {
        return market;
    }

    public void setMarket(Market market) {
        this.market = market;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }



    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getCustom1() {
        return custom1;
    }

    public void setCustom1(String custom1) {
        this.custom1 = custom1;
    }

    public String getCustom2() {
        return custom2;
    }

    public void setCustom2(String custom2) {
        this.custom2 = custom2;
    }

    public String getCustom3() {
        return custom3;
    }

    public void setCustom3(String custom3) {
        this.custom3 = custom3;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFailUrl() {
        return failUrl;
    }

    public void setFailUrl(String failUrl) {
        this.failUrl = failUrl;
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

    public String getSuccessUrl() {
        return successUrl;
    }

    public void setSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
    }

    public Boolean getTest() {
        return test;
    }

    public void setTest(Boolean test) {
        this.test = test;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int orderStatus) {
        this.status = orderStatus;
    }
    
    
}
