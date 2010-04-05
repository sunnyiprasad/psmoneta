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
    /*
     * Ответ содержит сумму заказа для оплаты. Данным кодом следует отвечать,
        когда в параметрах проверочного запроса не был указан параметр MNT_AMOUNT
     */
    public static final int ANSWER_CONTAINS_AMOUNT = 100;
    //Заказ оплачен. Уведомление об оплате магазину доставлено.
    public static final int PAYMENT_SUCCESS = 200;
    // Заказ находится в обработке. Точный статус оплаты заказа определить невозможно.
    public static final int UNKNOWN_STATUS = 302;
    //Заказ создан и готов к оплате. Уведомление об оплате магазину не доставлено.
    public static final int ORDER_IS_CREATE = 402;
    /*
     * Заказ не является актуальным в магазине (например, заказ отменен). При
        получении данного кода MONETA.Assistant не будет больше пытаться
        отсылать уведомление об оплате, если оно не было доставлено.
     */
    public static final int ORDER_NOT_ACTUAL = 500;



    public static final int EURO = 1;
    public static final int USD = 2;
    public static final int RUB = 0;

}
