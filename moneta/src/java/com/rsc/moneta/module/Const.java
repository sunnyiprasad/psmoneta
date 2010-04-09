/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsc.moneta.module;

/**
 *
 * @author Солодовников Д.А.
 */
public class Const {

    // Коды ответа на запросу ПС ОСМП согласно протоколу (providers_protocol_original.pdf)
    public final static int OSMP_RETURN_CODE_OK = 0; // ОК
    public final static int OSMP_RETURN_CODE_TEMPORARY_ERROR = 1; // Временная ошибка. Повторите запрос позже
    public final static int OSMP_RETURN_CODE_ACCOUNT_ILLEGAL_FORMAT = 4; // Неверный формат идентификатора абонента
    public final static int OSMP_RETURN_CODE_ACCOUNT_NOT_FOUND = 5; // Идентификатор абонента не найден (Ошиблись номером)
    public final static int OSMP_RETURN_CODE_PAY_SUPPRESS = 7; // Прием платежа запрещен провайдером
    public final static int OSMP_RETURN_CODE_PAY_SUPPRESS_ON_TECHNICAL_REASONS = 8; // Прием платежа запрещен по техническим причинам
    public final static int OSMP_RETURN_CODE_ACCOUNT_DISABLED = 79; // Счет абонента не активен
//    public final static int OSMP_RETURN_CODE_SUM_TOO_SMALL = 241; // Сумма слишком мала
//    public final static int OSMP_RETURN_CODE_SUM_TOO_BIG = 242; // Сумма слишком велика
    public final static int OSMP_RETURN_CODE_OTHER_ERROR = 300; // Другая ошибка провайдера
    
    // Комментарии к кодам ответа ПС ОСМП
    public final static String STRING_COMMAND_PARAMETER_ERROR = "Отсутствует или неправильный параметр 'command'";
    public final static String STRING_TXN_ID_PARAMETER_ERROR = "Отсутствует или неправильный параметр 'txn_id'";
    public final static String STRING_ACCOUNT_PARAMETER_ERROR = "Отсутствует или неправильный параметр 'account'";
    public final static String STRING_SUM_PARAMETER_ERROR = "Отсутствует или неправильный параметр 'sum'";
    public final static String STRING_DB_ERROR = "Ошибка БД";
    public final static String STRING_UNKNOWN_ERROR = "Неизвестная ошибка";
    public final static String STRING_ENTERED_NUMBER_DOES_NOT_CONFORM_TO_ORDER_FORMAT = "Введенный номер не соответствует формату номера заказа";
    public final static String STRING_ORDER_DOES_NOT_EXIST_ERROR = "Заказ с таким номером не существует";
    public final static String STRING_ORDER_REJECTED_BY_EMARKETPLACE_BEFORE_TLSM_PAYMENT = "Интернет-Магазином признан инвалидным и отменён до получения оплаты";
    public final static String STRING_ORDER_REJECTED_BY_EMARKETPLACE_AFTER_TLSM_PAYMENT = "Интернет-Магазином признан инвалидным и отменён после получения оплаты";
    public final static String STRING_UNABLE_TO_REQUEST_EMARKETPLACE_FOR_ORDER_STATUS = "Нет возможности запросить статус заказа у Интернет-Магазина";
    public final static String STRING_ORDER_IS_INVALID_FOR_EMARKETPLACE = "Заказ не валиден в информационной системе Интернет-Магазина (отменён и т.д.)";
    public final static String STRING_ORDER_PAID_AND_COMPLETED = "Этот заказ уже обработан и оплачен";
    public final static String STRING_ORDER_IS_PROCESSING_IN_EMARKETPLACE = "Заказ находится в стадии обработки";

//    public final static String STRING_TXN_DATE_PARAMETER_ERROR = "Отсутствует или неправильный параметр 'txn_date'";
//    public final static String STRING_DB_CONNECTION_ERROR = "Отсутствует соединение с БД";
//    public final static String STRING_CARD_IS_NOT_ASSIGNED_TO_USER = "Карта не активирована (не принадлежит абоненту)";
//    public final static String STRING_ABONENT_LOCKEDOUT = "Абонент заблокирован";
//    public final static String STRING_SUM_TOO_SMALL = "Сумма слишком мала";
//    public final static String STRING_SUM_TOO_BIG = "Сумма слишком велика";
    
}

