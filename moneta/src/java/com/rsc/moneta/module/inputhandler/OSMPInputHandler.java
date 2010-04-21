/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsc.moneta.module.inputhandler;

//import com.opensymphony.xwork2.Result;
import java.text.ParseException;
import java.util.Map;
import javax.persistence.EntityManager;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Date;
import java.util.GregorianCalendar;
//import java.text.NumberFormat;
//import java.util.Locale;

import com.rsc.moneta.dao.EMF;
import com.rsc.moneta.bean.PaymentOrder;
import com.rsc.moneta.bean.OSMPPayment;
import com.rsc.moneta.module.ResultCode;
//import com.rsc.moneta.Currency;
import com.rsc.moneta.module.CheckResponse;
import com.rsc.moneta.module.InputHandler;
import com.rsc.moneta.module.MainPaymentHandler;
import com.rsc.moneta.dao.Dao;
import com.rsc.moneta.dao.OSMPPaymentDao;
import java.text.SimpleDateFormat;

/**
 * Класс представляет собой класс - обработчик запросов, поступающих от терминала ОСМП
 * @author Солодовников Д.А.
 */
public class OSMPInputHandler implements InputHandler {

    // Коды ответа на запросу ПС ОСМП согласно протоколу (providers_protocol_original.pdf)
    public final static int OSMP_RETURN_CODE_OK = 0; // ОК
    public final static int OSMP_RETURN_CODE_TEMPORARY_ERROR = 1; // Временная ошибка. Повторите запрос позже
    public final static int OSMP_RETURN_CODE_ACCOUNT_ILLEGAL_FORMAT = 4; // Неверный формат идентификатора абонента
    public final static int OSMP_RETURN_CODE_ACCOUNT_NOT_FOUND = 5; // Идентификатор абонента не найден (Ошиблись номером)
    public final static int OSMP_RETURN_CODE_PAY_SUPPRESS = 7; // Прием платежа запрещен провайдером
    public final static int OSMP_RETURN_CODE_PAY_SUPPRESS_ON_TECHNICAL_REASONS = 8; // Прием платежа запрещен по техническим причинам
    public final static int OSMP_RETURN_CODE_ACCOUNT_DISABLED = 79; // Счет абонента не активен
    public final static int OSMP_RETURN_CODE_SUM_TOO_SMALL = 241; // Сумма слишком мала
//    public final static int OSMP_RETURN_CODE_SUM_TOO_BIG = 242; // Сумма слишком велика
    public final static int OSMP_RETURN_CODE_OTHER_ERROR = 300; // Другая ошибка провайдера
    // Комментарии к кодам ответа ПС ОСМП
    public final static String STRING_COMMAND_PARAMETER_ERROR = "Отсутствует или неправильный параметр 'command'";
    public final static String STRING_TXN_ID_PARAMETER_ERROR = "Отсутствует или неправильный параметр 'txn_id'";
    public final static String STRING_ACCOUNT_PARAMETER_ERROR = "Отсутствует или неправильный параметр 'account'";
    public final static String STRING_SUM_PARAMETER_ERROR = "Отсутствует или неправильный параметр 'sum'";
    public final static String STRING_TXN_DATE_PARAMETER_ERROR = "Отсутствует или неправильный параметр 'txn_date'";
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
    private InputHandlerConfig config;
//    public final static String STRING_SUM_TOO_BIG = "Сумма слишком велика";
    // Члены класса-эмуляторы различных свойтсв класса, применяемые для
    // тестирования
    private final static int EMULATOR_NOT_SET_INT = -1;
    private final static double EMULATOR_NOT_SET_DOUBLE = -1.0;
    private int TLSMResultCodeEmulator = EMULATOR_NOT_SET_INT;
    private double amountEmulator = EMULATOR_NOT_SET_DOUBLE;

    // Данный метод проверяет возможность платежа.
    // См. ОПИСАНИЕ ИНТЕРФЕЙСА ОСМП стр. 6.
    public String check(Map inputData) {

        // внутренний номер платежа в системе ОСМП (параметр из ОСМП-запроса)
        String txn_id = "";

        // внутренний номер платежа в системе ОСМП
        double transactionId = -1.0;

        // уникальный номер операции пополнения баланса абонента (в базе
        // провайдера), целое число длиной до 20 знаков - номер заказа ПС ТЛСМ
        // - исходим из того, что соотношение между транзакцией ПС ОСМП и
        // заказом ПС ТЛСМ - один к одному
        long prv_txn = -1;

        // сумма платежа
        double amount = -1.0;

        // идентификатор абонента в информационной системе провайдера (параметр из ОСМП-запроса)
        String account = "";

        // Идентификатор кода заказа ТЛСМ
        long paymentOrderId = -1;

        // command=check – запрос на проверку состояния абонента
        String command = "";

        int result = OSMP_RETURN_CODE_OTHER_ERROR;
        String comment = "";

        try {
            command = ((String[]) inputData.get("command"))[0];
            try {
                if (command.equals("check")) {
                    try {
                        txn_id = ((String[]) inputData.get("txn_id"))[0];
                        transactionId = Double.parseDouble(txn_id);
                        if (!this.regexMatch("^[0-9]{1,20}$", txn_id)) {
                            result = OSMP_RETURN_CODE_OTHER_ERROR;
                            comment = STRING_TXN_ID_PARAMETER_ERROR;
                        } else {
                            try {
                                account = ((String[]) inputData.get("account"))[0];
                                if (!this.regexMatch("^[0-9]{19}$", account)) {
                                    result = OSMP_RETURN_CODE_ACCOUNT_ILLEGAL_FORMAT;
                                    comment = STRING_ENTERED_NUMBER_DOES_NOT_CONFORM_TO_ORDER_FORMAT;
                                } else {
                                    try {
                                        paymentOrderId = Long.parseLong(account);
                                        // TODO: Денис - с Суликом - параметр sum может быть прислан в запросе "check"
                                        // выяснить, что делать в этом случае
                                        try {
                                            EntityManager em = EMF.getEntityManager();

                                            //Поиск по первичному ключу для всех объектов уже реализован JPA
                                            PaymentOrder paymentOrder = em.find(PaymentOrder.class, paymentOrderId);

                                            if (paymentOrder == null) {
                                                // Заказ не найден. Введен несуществующий код заказа.
                                                result = OSMP_RETURN_CODE_ACCOUNT_NOT_FOUND;
                                                comment = STRING_ORDER_DOES_NOT_EXIST_ERROR;
                                            } else {
                                                MainPaymentHandler handler = null;
                                                CheckResponse checkResponse = null;
                                                if (this.TLSMResultCodeEmulator == EMULATOR_NOT_SET_INT) {
                                                    // Заказ в ПС ТЛСМ найден.
                                                    handler = new MainPaymentHandler(em);

                                                    // Проверка его статуса в ИМ - отправка запроса check
                                                    checkResponse = handler.check(paymentOrder);
                                                }

                                                if (checkResponse != null || this.TLSMResultCodeEmulator != EMULATOR_NOT_SET_INT) {
                                                    int TLSMResultCode = -1;
                                                    if (this.TLSMResultCodeEmulator == EMULATOR_NOT_SET_INT) {
                                                        TLSMResultCode = checkResponse.getResultCode();
                                                    } else {
                                                        TLSMResultCode = this.TLSMResultCodeEmulator;
                                                    }
                                                    if (TLSMResultCode == ResultCode.SUCCESS_WITH_AMOUNT) {
                                                        // TODO: Денис, с Суликом - тут должна быть проверка суммы заказа - 
                                                        // для каждого ИМ-а надо проверять на вхождение в рамки мин. и макс.
                                                        // суммы заказа
                                                        if (checkResponse != null) {
                                                            amount = checkResponse.getAmount();
                                                        } else {
                                                            if (amountEmulator != EMULATOR_NOT_SET_DOUBLE) {
                                                                amount = amountEmulator;
                                                            }
                                                        }
                                                        result = OSMP_RETURN_CODE_OK;
                                                        comment = "";
                                                    } else {
                                                        if (TLSMResultCode == ResultCode.SUCCESS_WITHOUT_AMOUNT) {
                                                            // TODO: Денис, с Суликом - тут должна быть проверка суммы заказа -
                                                            // для каждого ИМ-а надо проверять на вхождение в рамки мин. и макс.
                                                            // суммы заказа
                                                            amount = paymentOrder.getAmount();
                                                            result = OSMP_RETURN_CODE_OK;
                                                            comment = "";
                                                        } else {
                                                            if (TLSMResultCode == ResultCode.ORDER_ALREADY_PAID) {
                                                                prv_txn = paymentOrder.getId();
                                                                result = OSMP_RETURN_CODE_OK;
                                                                comment = STRING_ORDER_PAID_AND_COMPLETED;
                                                            } else {
                                                                if (TLSMResultCode == ResultCode.ORDER_VALID_AND_PROCESSING) {
                                                                    result = OSMP_RETURN_CODE_TEMPORARY_ERROR;
                                                                    comment = STRING_ORDER_IS_PROCESSING_IN_EMARKETPLACE;
                                                                } else {
                                                                    if (TLSMResultCode == ResultCode.ORDER_NOT_ACTUAL) {
                                                                        result = OSMP_RETURN_CODE_ACCOUNT_DISABLED;
                                                                        comment = STRING_ORDER_IS_INVALID_FOR_EMARKETPLACE;
                                                                    } else {
                                                                        if (TLSMResultCode == ResultCode.ERROR_TRY_AGAIN) {
                                                                            result = OSMP_RETURN_CODE_TEMPORARY_ERROR;
                                                                            comment = STRING_EMARKETPLACE_UNABLE_TO_RESPOND;
                                                                        } else {
                                                                            if (TLSMResultCode == ResultCode.INTERNAL_ERROR) {
                                                                                result = OSMP_RETURN_CODE_OTHER_ERROR;
                                                                                comment = STRING_UNKNOWN_ERROR;
                                                                            } else {
                                                                                if (TLSMResultCode == ResultCode.UNKNOWN_CODE_RETURNED_BY_EMARKEPLACE) {
                                                                                    result = OSMP_RETURN_CODE_OTHER_ERROR;
                                                                                    comment = STRING_UNKNOWN_CODE_RETURNED_BY_EMARKEPLACE;
                                                                                } else {
                                                                                    if (TLSMResultCode == ResultCode.ORDER_NOT_FOUND_IN_EMARKETPLACE) {
                                                                                        // TODO: Денис, с Суликом - правильный ли код я возвращаю ОСМП в этом случае?
                                                                                        result = OSMP_RETURN_CODE_ACCOUNT_NOT_FOUND;
                                                                                        comment = STRING_ORDER_DOES_NOT_EXIST_ERROR;
                                                                                    } else {
                                                                                        if (TLSMResultCode == ResultCode.INVALID_SIGN_RETURNED_BY_EMARKETPLACE) {
                                                                                            result = OSMP_RETURN_CODE_PAY_SUPPRESS_ON_TECHNICAL_REASONS;
                                                                                            comment = STRING_INVALID_SIGN_RETURNED_BY_EMARKETPLACE;
                                                                                        } else {
                                                                                            if (TLSMResultCode == ResultCode.MARKET_ID_WAS_NOT_PROVIDED_BY_EMARKETPLACE) {
                                                                                                result = OSMP_RETURN_CODE_PAY_SUPPRESS_ON_TECHNICAL_REASONS;
                                                                                                comment = STRING_MARKET_ID_WAS_NOT_PROVIDED_BY_EMARKETPLACE;
                                                                                            } else {
                                                                                                if (TLSMResultCode == ResultCode.TRANSACTIONID_WAS_NOT_PROVIDED_BY_EMARKETPLACE) {
                                                                                                    result = OSMP_RETURN_CODE_PAY_SUPPRESS_ON_TECHNICAL_REASONS;
                                                                                                    comment = STRING_TRANSACTIONID_WAS_NOT_PROVIDED_BY_EMARKETPLACE;
                                                                                                } else {
                                                                                                    if (TLSMResultCode == ResultCode.SUCCESS_BUT_AMOUNT_LESS_THAN_MUST_BE) {
                                                                                                        result = OSMP_RETURN_CODE_SUM_TOO_SMALL;
                                                                                                        comment = STRING_SUM_TOO_SMALL;
                                                                                                    } else {
                                                                                                        // TODO: Денис, с Суликом - что еще сделать в таком случае?
                                                                                                        result = OSMP_RETURN_CODE_OTHER_ERROR;
                                                                                                        comment = STRING_UNKNOWN_ERROR;
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    // Не удалось получить ответ от ИМ на запрос
                                                    result = OSMP_RETURN_CODE_TEMPORARY_ERROR;
                                                    comment = STRING_UNABLE_TO_REQUEST_EMARKETPLACE_FOR_ORDER_STATUS;
                                                }
                                            }
                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                            result = OSMP_RETURN_CODE_OTHER_ERROR;
                                            comment = STRING_DB_ERROR;
                                        }
                                    } catch (Exception ex) {
                                        result = OSMP_RETURN_CODE_ACCOUNT_ILLEGAL_FORMAT;
                                        comment = STRING_ENTERED_NUMBER_DOES_NOT_CONFORM_TO_ORDER_FORMAT;
                                    }
                                }
                            } catch (Exception ex) {
                                result = OSMP_RETURN_CODE_OTHER_ERROR;
                                comment = STRING_ACCOUNT_PARAMETER_ERROR;
                            }
                        }
                    } catch (Exception ex) {
                        result = OSMP_RETURN_CODE_OTHER_ERROR;
                        comment = STRING_TXN_ID_PARAMETER_ERROR;
                    }
                } else {
                    result = OSMP_RETURN_CODE_OTHER_ERROR;
                    comment = STRING_COMMAND_PARAMETER_ERROR;
                }
            } catch (Exception ex) {
                result = OSMP_RETURN_CODE_OTHER_ERROR;
                comment = STRING_UNKNOWN_ERROR;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            result = OSMP_RETURN_CODE_OTHER_ERROR;
            comment = STRING_COMMAND_PARAMETER_ERROR;
        } finally {
            try {
                return this.makeUpResponse(txn_id, prv_txn, amount, result, comment);
            } catch (Exception ex) {
                return null;
            }
        }
    }

    // Данный метод проводит платеж
    // См. ОПИСАНИЕ ИНТЕРФЕЙСА ОСМП стр. 7.
    public String pay(Map inputData) {
        // внутренний номер платежа в системе ОСМП (параметр из ОСМП-запроса)
        String txn_id = "";

        // внутренний номер платежа в системе ОСМП
        double transactionId = -1.0;

        // уникальный номер операции пополнения баланса абонента (в базе
        // провайдера), целое число длиной до 20 знаков - номер заказа ПС ТЛСМ
        // - исходим из того, что соотношение между транзакцией ПС ОСМП и
        // заказом ПС ТЛСМ - один к одному
        long prv_txn = -1;

        // сумма платежа (параметр из ОСМП-запроса)
        String sum = "";

        // сумма платежа
        double amount = -1.0;

        // идентификатор абонента в информационной системе провайдера (параметр из ОСМП-запроса)
        String account = "";

        // Идентификатор кода заказа ТЛСМ
        long paymentOrderId = -1;

        // дата учета платежа в системе ОСМП (параметр из ОСМП-запроса)
        String txn_date = "";

        // дата учета платежа в системе ОСМП
        Date payDate = null;

        // command=check – запрос на проверку состояния абонента
        String command = "";

        int result = OSMP_RETURN_CODE_OTHER_ERROR;
        String comment = "";

        try {
            command = ((String[]) inputData.get("command"))[0];
            try {
                if (command.equals("pay")) {
                    try {
                        txn_id = ((String[]) inputData.get("txn_id"))[0];
                        transactionId = Double.parseDouble(txn_id);
                        if (!this.regexMatch("^[0-9]{1,20}$", txn_id)) {
                            result = OSMP_RETURN_CODE_OTHER_ERROR;
                            comment = STRING_TXN_ID_PARAMETER_ERROR;
                        } else {
                            try {
                                transactionId = Double.parseDouble(txn_id);
                                account = ((String[]) inputData.get("account"))[0];
                                if (!this.regexMatch("^[0-9]{19}$", account)) {
                                    result = OSMP_RETURN_CODE_ACCOUNT_ILLEGAL_FORMAT;
                                    comment = STRING_ENTERED_NUMBER_DOES_NOT_CONFORM_TO_ORDER_FORMAT;
                                } else {
                                    try {
                                        paymentOrderId = Long.parseLong(account);
                                        try {
                                            sum = ((String[]) inputData.get("sum"))[0];
                                            // TODO: Денис - проверить - насколько я понимаю, Java "поймёт" что это число типа double
                                            // только если там точка будет, на запятую будет Exception сразу
                                            amount = Double.parseDouble(sum);
                                            try {
                                                txn_date = ((String[]) inputData.get("txn_date"))[0];
                                                payDate = this.getDateFromOSMPTransactionDate(txn_date);
                                                try {
                                                    EntityManager em = EMF.getEntityManager();

                                                    //Поиск по первичному ключу для всех объектов уже реализован JPA
                                                    PaymentOrder paymentOrder = em.find(PaymentOrder.class, paymentOrderId);

                                                    if (paymentOrder == null) {
                                                        // Заказ не найден. Введен несуществующий код заказа.
                                                        result = OSMP_RETURN_CODE_ACCOUNT_NOT_FOUND;
                                                        comment = STRING_ORDER_DOES_NOT_EXIST_ERROR;
                                                    } else {
                                                        // Заказ в ПС ТЛСМ найден.
                                                        OSMPPayment payment = new OSMPPaymentDao(em).getPaymentByTransactionIdAndPaymentSystemId(transactionId, this.config.getId());
                                                        if (payment != null) {
                                                            // Ранее этот платеж проводился, возвращаем предыдущий результат
                                                            result = payment.getResultCode();
                                                            comment = payment.getDescription();
                                                            amount = payment.getAmount();
                                                        } else {
                                                            // Ранее платеж не проводился
                                                            MainPaymentHandler handler = new MainPaymentHandler(em);

                                                            // Проверка его статуса в ИМ - отправка запроса check
                                                            CheckResponse checkResponse = handler.pay(paymentOrder, amount);

                                                            if (checkResponse != null) {
                                                                int TLSMResultCode = checkResponse.getResultCode();
                                                                if (TLSMResultCode == ResultCode.SUCCESS_WITH_AMOUNT) {
                                                                    // TODO: Денис, с Суликом - тут должна быть проверка суммы заказа -
                                                                    // для каждого ИМ-а надо проверять на вхождение в рамки мин. и макс.
                                                                    // суммы заказа
                                                                    this.saveOSMPPayment(amount, payDate, paymentOrder, transactionId);
                                                                    amount = checkResponse.getAmount();
                                                                    result = OSMP_RETURN_CODE_OK;
                                                                    comment = "success";
                                                                } else {
                                                                    if (TLSMResultCode == ResultCode.SUCCESS_WITHOUT_AMOUNT) {
                                                                        this.saveOSMPPayment(amount, payDate, paymentOrder, transactionId);
                                                                        // TODO: Денис, с Суликом - тут должна быть проверка суммы заказа -
                                                                        // для каждого ИМ-а надо проверять на вхождение в рамки мин. и макс.
                                                                        // суммы заказа
                                                                        amount = paymentOrder.getAmount();
                                                                        result = OSMP_RETURN_CODE_OK;
                                                                        comment = "success";
                                                                    } else {
                                                                        if (TLSMResultCode == ResultCode.ORDER_ALREADY_PAID) {
                                                                            prv_txn = paymentOrder.getId();
                                                                            result = OSMP_RETURN_CODE_OK;
                                                                            comment = STRING_ORDER_PAID_AND_COMPLETED;
                                                                        } else {
                                                                            if (TLSMResultCode == ResultCode.ORDER_VALID_AND_PROCESSING) {
                                                                                result = OSMP_RETURN_CODE_TEMPORARY_ERROR;
                                                                                comment = STRING_ORDER_IS_PROCESSING_IN_EMARKETPLACE;
                                                                            } else {
                                                                                if (TLSMResultCode == ResultCode.ORDER_NOT_ACTUAL) {
                                                                                    result = OSMP_RETURN_CODE_ACCOUNT_DISABLED;
                                                                                    comment = STRING_ORDER_IS_INVALID_FOR_EMARKETPLACE;
                                                                                } else {
                                                                                    if (TLSMResultCode == ResultCode.ERROR_TRY_AGAIN) {
                                                                                        result = OSMP_RETURN_CODE_TEMPORARY_ERROR;
                                                                                        comment = STRING_EMARKETPLACE_UNABLE_TO_RESPOND;
                                                                                    } else {
                                                                                        if (TLSMResultCode == ResultCode.INTERNAL_ERROR) {
                                                                                            result = OSMP_RETURN_CODE_OTHER_ERROR;
                                                                                            comment = STRING_UNKNOWN_ERROR;
                                                                                        } else {
                                                                                            if (TLSMResultCode == ResultCode.UNKNOWN_CODE_RETURNED_BY_EMARKEPLACE) {
                                                                                                result = OSMP_RETURN_CODE_OTHER_ERROR;
                                                                                                comment = STRING_UNKNOWN_CODE_RETURNED_BY_EMARKEPLACE;
                                                                                            } else {
                                                                                                if (TLSMResultCode == ResultCode.ORDER_NOT_FOUND_IN_EMARKETPLACE) {
                                                                                                    // TODO: Денис, с Суликом - правильный ли код я возвращаю ОСМП в этом случае?
                                                                                                    result = OSMP_RETURN_CODE_ACCOUNT_NOT_FOUND;
                                                                                                    comment = STRING_ORDER_DOES_NOT_EXIST_ERROR;
                                                                                                } else {
                                                                                                    if (TLSMResultCode == ResultCode.INVALID_SIGN_RETURNED_BY_EMARKETPLACE) {
                                                                                                        result = OSMP_RETURN_CODE_PAY_SUPPRESS_ON_TECHNICAL_REASONS;
                                                                                                        comment = STRING_INVALID_SIGN_RETURNED_BY_EMARKETPLACE;
                                                                                                    } else {
                                                                                                        if (TLSMResultCode == ResultCode.MARKET_ID_WAS_NOT_PROVIDED_BY_EMARKETPLACE) {
                                                                                                            result = OSMP_RETURN_CODE_PAY_SUPPRESS_ON_TECHNICAL_REASONS;
                                                                                                            comment = STRING_MARKET_ID_WAS_NOT_PROVIDED_BY_EMARKETPLACE;
                                                                                                        } else {
                                                                                                            if (TLSMResultCode == ResultCode.TRANSACTIONID_WAS_NOT_PROVIDED_BY_EMARKETPLACE) {
                                                                                                                result = OSMP_RETURN_CODE_PAY_SUPPRESS_ON_TECHNICAL_REASONS;
                                                                                                                comment = STRING_TRANSACTIONID_WAS_NOT_PROVIDED_BY_EMARKETPLACE;
                                                                                                            } else {
                                                                                                                if (TLSMResultCode == ResultCode.SUCCESS_BUT_AMOUNT_LESS_THAN_MUST_BE) {
                                                                                                                    result = OSMP_RETURN_CODE_SUM_TOO_SMALL;
                                                                                                                    comment = STRING_SUM_TOO_SMALL;
                                                                                                                } else {
                                                                                                                    // TODO: Денис, с Суликом - что еще сделать в таком случае?
                                                                                                                    result = OSMP_RETURN_CODE_OTHER_ERROR;
                                                                                                                    comment = STRING_UNKNOWN_ERROR;
                                                                                                                }
                                                                                                            }
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            } else {
                                                                // Не удалось получить ответ от ИМ на запрос
                                                                result = OSMP_RETURN_CODE_TEMPORARY_ERROR;
                                                                comment = STRING_UNABLE_TO_REQUEST_EMARKETPLACE_FOR_ORDER_STATUS;
                                                            }
                                                        }
                                                    }
                                                } catch (Exception ex) {
                                                    result = OSMP_RETURN_CODE_OTHER_ERROR;
                                                    comment = STRING_DB_ERROR;
                                                }
                                            } catch (Exception ex) {
                                                result = OSMP_RETURN_CODE_OTHER_ERROR;
                                                comment = STRING_TXN_DATE_PARAMETER_ERROR;
                                            }
                                        } catch (Exception ex) {
                                            result = OSMP_RETURN_CODE_OTHER_ERROR;
                                            comment = STRING_SUM_PARAMETER_ERROR;
                                        }
                                    } catch (Exception ex) {
                                        result = OSMP_RETURN_CODE_ACCOUNT_ILLEGAL_FORMAT;
                                        comment = STRING_ENTERED_NUMBER_DOES_NOT_CONFORM_TO_ORDER_FORMAT;
                                    }
                                }
                            } catch (Exception ex) {
                                result = OSMP_RETURN_CODE_OTHER_ERROR;
                                comment = STRING_ACCOUNT_PARAMETER_ERROR;
                            }
                        }
                    } catch (Exception ex) {
                        result = OSMP_RETURN_CODE_OTHER_ERROR;
                        comment = STRING_TXN_ID_PARAMETER_ERROR;
                    }
                } else {
                    result = OSMP_RETURN_CODE_OTHER_ERROR;
                    comment = STRING_COMMAND_PARAMETER_ERROR;
                }
            } catch (Exception ex) {
                result = OSMP_RETURN_CODE_OTHER_ERROR;
                comment = STRING_UNKNOWN_ERROR;
            }
        } catch (Exception ex) {
            result = OSMP_RETURN_CODE_OTHER_ERROR;
            comment = STRING_COMMAND_PARAMETER_ERROR;
        } finally {
            try {
                return this.makeUpResponse(txn_id, prv_txn, amount, result, comment);
            } catch (Exception ex) {
                return null;
            }
        }
    }

    // На данный момент не реализуется
    public String getStatus(Map inputData) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    // На данный момент не реализуется
    public String cancel(Map inputData) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private boolean regexMatch(String regex, String string) {
        Pattern pattern = null;
        pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(string);
        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }

    private String makeUpResponse(String txn_id, long prv_txn, double amount, int result, String comment) {

        String response = "<?xml version='1.0' encoding='UTF-8'?>"
                + "<response>";
        if (txn_id.equals("")) {
            response +=
                    "<osmp_txn_id/>";
        } else {
            response +=
                    "<osmp_txn_id>" + txn_id + "</osmp_txn_id>";
        }
        if (prv_txn != -1) {
            response +=
                    "<prv_txn>" + String.format("%019d", prv_txn) + "</prv_txn>";
        }
        if (amount != -1.00) {
            // TODO: Денис - вместо replace(',', '.') должно быть создание
            // NumberFormat или чего-либо ему подобного и его применение
            response +=
                    "<sum>" + String.format("%.2f", amount).replace(',', '.') + "</sum>";
        }
        response +=
                "<result>" + String.format("%d", result) + "</result>";
        response +=
                "<comment>" + comment + "</comment>";
        response +=
                "</response>";
        return response;
    }

    /**
     * Creates a record for OSMP pay transaction
     * @param paymentOrder
     */
    private void saveOSMPPayment(double amount, Date payDate, PaymentOrder paymentOrder, double transactionId) {
        EntityManager em = EMF.getEntityManager();
        OSMPPayment payment = new OSMPPayment();
        payment.setAmount(amount);
        payment.setPayDate(payDate);
        payment.setPaymentOrder(paymentOrder);
        payment.setPaymentSystemId(this.config.getId());
        payment.setRejectDate(null);
        payment.setTransactionId(transactionId);
        new Dao(em).persist(payment);
        em.close();
    }

    public Date getDateFromOSMPTransactionDate(String OSMPTransactionDate) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        return df.parse(OSMPTransactionDate);
        /*return new GregorianCalendar(
        Integer.parseInt(OSMPTransactionDate.substring(0, 4)),
        Integer.parseInt(OSMPTransactionDate.substring(4, 2)),
        Integer.parseInt(OSMPTransactionDate.substring(6, 2)),
        Integer.parseInt(OSMPTransactionDate.substring(8, 2)),
        Integer.parseInt(OSMPTransactionDate.substring(10, 2)),
        Integer.parseInt(OSMPTransactionDate.substring(12, 2))).getTime();*/
    }

    public void setConfig(InputHandlerConfig config) {
        this.config = config;
    }

    public String checkWithEmulating(Map inutData,
            int TLSMResultCodeEmulator) {
        this.TLSMResultCodeEmulator = TLSMResultCodeEmulator;
        String xml = this.check(inutData);
        this.TLSMResultCodeEmulator = EMULATOR_NOT_SET_INT;
        return xml;
    }

    public String checkWithEmulating(Map inutData,
            int TLSMResultCodeEmulator,
            double amountEmulator) {
        this.TLSMResultCodeEmulator = TLSMResultCodeEmulator;
        this.amountEmulator = amountEmulator;
        String xml = this.check(inutData);
        this.TLSMResultCodeEmulator = EMULATOR_NOT_SET_INT;
        this.amountEmulator = EMULATOR_NOT_SET_DOUBLE;
        return xml;
    }
}
