/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsc.moneta.module;

/**
 * Класс хранит коды ошибок ПС ТЛСМ, возвращаемых в ответ на запросы от ПС ТЛСМ
 * к инорфмационной системе Интернет-Магазина
 * @author sulic
 */
public class ResultCode {

    // Заказ валиден в информационной системе Интернет-Магазина,
    // ответ Интернет-Магазина содержит сумму заказа для оплаты
    public static final int SUCCESS_WITH_AMOUNT = 0;

    // Заказ валиден в информационной системе Интернет-Магазина,
    // ответ Интернет-Магазина НЕ содержит сумму заказа для оплаты
    public static final int SUCCESS_WITHOUT_AMOUNT = 3;

    // Заказ считается оплаченным в информационной системе Интернет-Магазина,
    // информационная система Интернет-Магазина уведомила ПС ТЛСМ
    public static final int ORDER_ALREADY_PAID = 1;

    // Заказ валиден в информационной системе Интернет-Магазина и находится в
    // обработке
    public static final int ORDER_VALID_AND_PROCESSING = 2;

    // Заказ существует, но не валиден в информационной системе
    // Интернет-Магазина (отменён и т.д.)
    public static final int ORDER_NOT_ACTUAL = 4;

    // Необходимо повторить запрос, информационная система Интернет-Магазина
    // не в состоянии ответить на отправленный запрос в данный момент
    public static final int ERROR_TRY_AGAIN = 10;

    // Внутренная ошибка ПС ТЛСМ - нет возможности получить информацию о заказе
    // у информационной системы Интернет-Магазина
    public static final int INTERNAL_ERROR = 11;


    // В ответ на запрос информационная система Интернет-Магазина возвратила
    // код результата, не описанный в её протоколе
    public static final int UNKNOWN_CODE_RETURNED_BY_EMARKEPLACE = 12;


    // Заказ не найден в информационной системе Интернет-Магазина
    public static final int ORDER_NOT_FOUND_IN_EMARKETPLACE = 13;

    // В ответе на запрос информационная система Интернет-Магазина возвратила
    // неправильную подпись
    public static final int INVALID_SIGN_RETURNED_BY_EMARKETPLACE = 14;

    // В ответе на запрос информационная система Интернет-Магазина возвратила не
    // корректный идентификатор Интернет-Магазина в ПС ТЛСМ
    public static final int MARKET_ID_WAS_NOT_PROVIDED_BY_EMARKETPLACE = 15;

    // В ответе на запрос информационная система Интернет-Магазина не возвратила
    // корректный номер транзакции - в информационной системе Интернет-Магазина
    // же
    public static final int TRANSACTIONID_WAS_NOT_PROVIDED_BY_EMARKETPLACE = 16;
    
    // Введённая в терминальную ПС сумма меньше чем сумма заказа, поэтому 
    // выполнено зачисление на счет абонента в ПС ТЛСМ вместо оплаты заказа
    public static final int SUCCESS_BUT_AMOUNT_LESS_THAN_MUST_BE = 17;
    // Введённая в терминальную ПС сумма меньше чем сумма заказа,
    // при это у заказа не определен пользователь, остаток суммы некуда закинуть.
    // Поэтому принять платеж мы не можем.
    public static final int AMOUNT_LESS_THAN_MUST_BE = 18;
    public static final int AMOUNT_MORE_THAN_MUST_BE = 19;
}
