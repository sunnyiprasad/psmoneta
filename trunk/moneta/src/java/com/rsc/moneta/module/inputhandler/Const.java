/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsc.moneta.module.inputhandler;

/**
 *
 * @author Солодовников Д.А.
 */
public class Const {
    public final static String STRING_DB_ERROR = "Ошибка БД";
    public final static String STRING_UNKNOWN_ERROR = "Неизвестная ошибка";
    public final static String STRING_ENTERED_NUMBER_DOES_NOT_CONFORM_TO_ORDER_FORMAT = "Введенный номер не соответствует формату номера заказа";
    public final static String STRING_ORDER_DOES_NOT_EXIST_ERROR = "Заказ с таким номером не существует";
    public final static String STRING_ORDER_REJECTED_BY_EMARKETPLACE_BEFORE_TLSM_PAYMENT = "Интернет-Магазином заказ признан инвалидным и отменён до получения оплаты";
    public final static String STRING_ORDER_REJECTED_BY_EMARKETPLACE_AFTER_TLSM_PAYMENT = "Интернет-Магазином заказ признан инвалидным и отменён после получения оплаты";
    public final static String STRING_UNABLE_TO_REQUEST_EMARKETPLACE_FOR_ORDER_STATUS = "Нет возможности запросить статус заказа у Интернет-Магазина";
    public final static String STRING_ORDER_IS_INVALID_FOR_EMARKETPLACE = "Заказ не валиден в информационной системе Интернет-Магазина (отменён и т.д.)";
    public final static String STRING_ORDER_PAID_AND_COMPLETED = "Этот заказ уже обработан и оплачен";
    public final static String STRING_ORDER_IS_PROCESSING_IN_EMARKETPLACE = "Заказ находится в стадии обработки";
    public final static String STRING_EMARKETPLACE_UNABLE_TO_RESPOND = "Интернет-Магазин не в состоянии ответить на отправленный запрос в данный момент";
    public final static String STRING_UNKNOWN_CODE_RETURNED_BY_EMARKEPLACE = "Интернет-Магазин возвратил неизвестный код результата";
    public final static String STRING_INVALID_SIGN_RETURNED_BY_EMARKETPLACE = "Интернет-Магазин возвратил неправильную подпись";
    public final static String STRING_MARKET_ID_WAS_NOT_PROVIDED_BY_EMARKETPLACE = "Интернет-Магазин возвратил некорректный идентификатор Интернет-Магазина в ПС ТЛСМ";
    public final static String STRING_TRANSACTIONID_WAS_NOT_PROVIDED_BY_EMARKETPLACE = "Интернет-Магазин возвратил некорректный номер транзакции";
    public final static String STRING_SUM_TOO_SMALL = "Введённая сумма меньше чем сумма заказа, поэтому выполнено зачисление на счет абонента в ПС ТЛСМ вместо оплаты заказа";
    public final static String STRING_FAILED_SUM_TOO_SMALL_AND_ORDER_OWNER_UNDEFINED = "Сумма меньше чем сумма заказа, для заказа не определен владелец";
    public final static String STRING_FAILED_SUM_TOO_BIG_AND_ORDER_OWNER_UNDEFINED = "Сумма больше чем сумма заказа, для заказа не определен владелец";
}


