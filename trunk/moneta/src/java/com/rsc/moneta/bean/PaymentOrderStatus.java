/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsc.moneta.bean;

/**
 *
 * @author sulic
 */
public class PaymentOrderStatus {

    // Статус заказа ТЛСМ - возможные варианты
    //  -1: неопределён
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
    public final static int ORDER_STATUS_PAID_BUT_NOT_COMPLETED_AND_MONEY_ADDED_ON_ACCOUNT_BALANCE = 9;

    private int id;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}

