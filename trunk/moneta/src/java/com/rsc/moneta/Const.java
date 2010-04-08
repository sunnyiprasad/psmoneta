package com.rsc.moneta;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author sulic
 */
public class Const {

    // Типы счетов
    public static final int EURO = 1;
    public static final int USD = 2;
    public static final int RUB = 0;

    // Статус заказа ТЛСМ - возможные варианты
    // TODO: Сулик, статусы, проверь и добавь статусы если нужно, пжлст
    //  -1: неопределён
    public final static int ORDER_STATUS_UNDEFINED = -1;
    //   1: заказ от покупателя принят, сохранён в системе ТЛСМ, заказ забронирован в Интернет-Магазине,
    //      Интернет-Магазине об этом "знает", следующий шаг - оплата брони и получение документа об оплате
    //      (билета, чека и т.п.)
    public final static int ORDER_STATUS_ACCEPTED = 1;
    //   2: заказ оплачен покупателем, деньги получены в терминальной ПС и ТЛСМ об этом известно
    public final static int ORDER_STATUS_PAID_FOR_TLSM = 2;
    //   3: забронированный ранее но еще не оплаченный заказ Интернет-Магазином признан
    //      инвалидным и отменён
    public final static int ORDER_STATUS_NOT_PAID_AND_REJECTED_BY_EMARKETPLACE = 3;
    //   4: заказ оплачен покупателем, деньги получены в терминальной ПС и ТЛСМ об этом известно,
    //      системой ТЛСМ передано сообщение Интернет-магазину об оплате
    public final static int ORDER_STATUS_PAID_AND_COMPLETED = 4;
    //   5: заказ оплачен покупателем, деньги получены в терминальной ПС и ТЛСМ об этом известно,
    //      но системой ТЛСМ не передано сообщение Интернет-магазину об оплате
    public final static int ORDER_STATUS_PAID_BUT_NOT_COMPLETED = 5;
    //   6: заказ оплачен покупателем, деньги получены в терминальной ПС и ТЛСМ об этом известно,
    //      но заказ Интернет-Магазином признан инвалидным и отменён
    public final static int ORDER_STATUS_PAID_BUT_REJECTED_BY_EMARKETPLACE = 6;
}
