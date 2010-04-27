/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsc.moneta.module.inputhandler;

import java.util.Map;
import java.util.Date;
import javax.persistence.EntityManager;


import com.rsc.moneta.util.Utils;
import com.rsc.moneta.module.InputHandler;
import com.rsc.moneta.dao.EMF;
import com.rsc.moneta.bean.PaymentOrder;
import com.rsc.moneta.module.MainPaymentHandler;
import com.rsc.moneta.module.CheckResponse;
import com.rsc.moneta.module.ResultCode;


/**
 * Класс представляет собой класс - обработчик запросов, поступающих от терминала e-port
 * @author Солодовников Д.А.
 */

public class EPORTInputHandler implements InputHandler{

    private final static String EPORT_RETURN_CODE_E1 = "E1"; // E1 - запрос не распознан
    private final static String EPORT_RETURN_CODE_E2 = "E2"; // E2 - технические трудности обработки запроса
    private final static String EPORT_RETURN_CODE_E3 = "E3"; // E3 - подпись запроса неверна
    private final static String EPORT_RETURN_CODE_E4 = "E4"; // E4 - результат операции еще не известен
    private final static String EPORT_RETURN_CODE_S1 = "S1"; // S1 - успешно, платеж считается зачисленным
    private final static String EPORT_RETURN_CODE_S2 = "S2"; // S2 - успешно, платеж считается аннулированным
    private final static String EPORT_RETURN_CODE_F1 = "F1"; // F1 - отказ, сумма меньше минимальной допустимой
    private final static String EPORT_RETURN_CODE_F2 = "F2"; // F2 - отказ, сумма больше максимальной допустимой
    private final static String EPORT_RETURN_CODE_F3 = "F3"; // F3 - отказ, клиент с указанными реквизитами не найден
    private final static String EPORT_RETURN_CODE_F4 = "F4"; // F4 - отказ, платеж невозможен (но клиент с указкнными реквизитами найден)
    private final static String EPORT_RETURN_CODE_F5 = "F5"; // F5 - отказ, платеж с указанным id уже проведен
    private final static String EPORT_RETURN_CODE_F6 = "F6"; // F6 - отказ, платеж с указанным id не найден
    private final static String EPORT_RETURN_CODE_F7 = "F7"; // F7 - отказ, платеж с указанным id уже аннулирован
    private final static String EPORT_RETURN_CODE_F8 = "F8"; // F8 - отказ, аннулирование не возможно

    // Комментарии к кодам ответа ПС e-port
    private final static String STRING_UNDEFINED_REQUEST = "Запрос не распознан";
    private final static String STRING_ACCOUNT_PARAMETER_ERROR = "Отсутствует или неправильный параметр 'account'";
    private final static String STRING_TIMESTAMP_PARAMETER_ERROR = "Отсутствует или неправильный параметр 'timestamp'";

    //    private final static String STRING_ABONENT_LOCKEDOUT = "Абонент заблокирован";
//    private final static String STRING_SUM_TOO_SMALL = "Сумма слишком мала";
//    private final static String STRING_SUM_TOO_BIG = "Сумма слишком велика";
//    private final static String STRING_SUM_PARAMETER_ERROR = "Отсутствует или неправильный параметр 'sum'";
//    private final static String STRING_ID_PARAMETER_ERROR = "Отсутствует или неправильный параметр 'id'";
//    private final static String STRING_PAY_TIME_PARAMETER_ERROR = "Отсутствует или неправильный параметр 'pay_time'";
//    private final static String STRING_PAYMENT_ALREADY_COMPLETED = "Платеж с указанным 'id' уже проведен";
//    private final static String STRING_PAYMENT_ALREADY_REVOKED = "Платеж с указанным 'id' уже аннулирован";
//    private final static String STRING_PAYMENT_NOT_FOUND = "Платеж с указанным 'id' не найден";
//    private final static String STRING_COMMENT_TOO_LONG = "Длина поля comment больше 256 символов";


    /**
     *Поля запроса:
     *account - Идентификатор абонента
     *sum - Сумма платежа
     *timestamp - Дата/время формирования запроса
     *Поля ответа:
     *result - результат (допустимы E*, S1, F1, F2, F3, F4)
     *reason - уточняющий комментарий отказа для СП (не обязателен)
     *account - копия из запроса
     *sum - копия из запроса
     *timestamp - копия из запроса
     * @param inputData
     * @return
     */
    public String check(Map inputData) {

        // Идентификатор типа e-port-запроса (параметр из e-port-запроса)
        String type = "";

        // Идентификатор абонента в информационной системе провайдера - номер
        // заказа ПС ТЛСМ (параметр из e-port-запроса)
        String account = "";

        // Идентификатор кода заказа ТЛСМ
        long paymentOrderId = -1;

        // Сумма платежа
        double sum = -1.0;

        // Дата/время формирования запроса (параметр из e-port-запроса)
        String timestamp = null;

        // Дата/время формирования запроса (параметр из e-port-запроса)
        Date dtTimestamp = null;

        // Код ответа на запрос ПС e-port
        String result = "";

        // Комментарий отказа
        String reason = "";

        try {
            type = ((String[]) inputData.get("type"))[0];
            if (type.equals("check")) {
                try {
                    account = ((String[]) inputData.get("account"))[0];
                    if (!Utils.regexMatch("^[0-9]{19}$", account)) {
                        result = EPORT_RETURN_CODE_F3;
                        reason = Const.STRING_ENTERED_NUMBER_DOES_NOT_CONFORM_TO_ORDER_FORMAT;
                    } else {
                        try {
                            paymentOrderId = Long.parseLong(account);
                            try {
                                sum = Double.parseDouble(((String[]) inputData.get("sum"))[0]);
                                if (sum < 0) {
                                    sum = -1.0;
                                }
                            } catch (Exception ex) {
                                sum = -1.0;
                            }
                            try {
                                timestamp = ((String[]) inputData.get("timestamp"))[0];
                                // TODO: Денис - дату проверить на валидность
                                try {
                                    EntityManager em = EMF.getEntityManager();
                                    PaymentOrder paymentOrder = em.find(PaymentOrder.class, paymentOrderId);
                                    if (paymentOrder == null) {
                                        // Заказ не найден. Введен несуществующий код заказа.
                                        result = EPORT_RETURN_CODE_F3;
                                        reason = Const.STRING_ORDER_DOES_NOT_EXIST_ERROR;
                                    } else {
                                        // Заказ в ПС ТЛСМ найден
                                        MainPaymentHandler handler = null;
                                        CheckResponse checkResponse = null;
                                        handler = new MainPaymentHandler(em);
                                        // Проверка его статуса в ИМ - отправка запроса check
                                        checkResponse = handler.check(paymentOrder, sum);
                                        if (checkResponse != null) {
                                            int TLSMResultCode = -1;
                                            TLSMResultCode = checkResponse.getResultCode();
                                            switch (TLSMResultCode) {
                                                case ResultCode.SUCCESS_WITH_AMOUNT:
                                                    // TODO: Денис, с Суликом - тут должна быть проверка суммы заказа -
                                                    // для каждого ИМ-а надо проверять на вхождение в рамки мин. и макс.
                                                    // суммы заказа
//                                                    if (checkResponse != null) {
//                                                        amount = checkResponse.getAmount();
//                                                    } else {
//                                                        if (amountEmulator != EMULATOR_NOT_SET_DOUBLE) {
//                                                            amount = amountEmulator;
//                                                        }
//                                                    }
                                                    sum = checkResponse.getAmount();
                                                    result = EPORT_RETURN_CODE_S1;
                                                case ResultCode.SUCCESS_WITHOUT_AMOUNT:
                                                    // TODO: Денис, с Суликом - тут должна быть проверка суммы заказа -
                                                    // для каждого ИМ-а надо проверять на вхождение в рамки мин. и макс.
                                                    // суммы заказа
                                                    sum = paymentOrder.getAmount();
                                                    result = EPORT_RETURN_CODE_S1;
                                                case ResultCode.ORDER_ALREADY_PAID:
                                                    result = EPORT_RETURN_CODE_F4;
                                                    reason = Const.STRING_ORDER_PAID_AND_COMPLETED;
                                                case ResultCode.ORDER_VALID_AND_PROCESSING:
                                                    result = EPORT_RETURN_CODE_E4;
                                                    reason = Const.STRING_ORDER_IS_PROCESSING_IN_EMARKETPLACE;
                                                case ResultCode.ORDER_NOT_ACTUAL:
                                                    result = EPORT_RETURN_CODE_F4;
                                                    reason = Const.STRING_ORDER_IS_INVALID_FOR_EMARKETPLACE;
                                                case ResultCode.ERROR_TRY_AGAIN:
                                                    result = EPORT_RETURN_CODE_E4;
                                                    reason = Const.STRING_EMARKETPLACE_UNABLE_TO_RESPOND;
                                                case ResultCode.INTERNAL_ERROR:
                                                    result = EPORT_RETURN_CODE_E2;
                                                    reason = Const.STRING_UNKNOWN_ERROR;
                                                case ResultCode.UNKNOWN_CODE_RETURNED_BY_EMARKEPLACE:
                                                    result = EPORT_RETURN_CODE_E2;
                                                    reason = Const.STRING_UNKNOWN_CODE_RETURNED_BY_EMARKEPLACE;
                                                case ResultCode.ORDER_NOT_FOUND_IN_EMARKETPLACE:
                                                    // TODO: Денис, с Суликом - правильный ли код я возвращаю ОСМП в этом случае?
                                                    result = EPORT_RETURN_CODE_F3;
                                                    reason = Const.STRING_ORDER_DOES_NOT_EXIST_ERROR;
                                                case ResultCode.INVALID_SIGN_RETURNED_BY_EMARKETPLACE:
                                                    result = EPORT_RETURN_CODE_E2;
                                                    reason = Const.STRING_INVALID_SIGN_RETURNED_BY_EMARKETPLACE;
                                                case ResultCode.MARKET_ID_WAS_NOT_PROVIDED_BY_EMARKETPLACE:
                                                    result = EPORT_RETURN_CODE_E2;
                                                    reason = Const.STRING_MARKET_ID_WAS_NOT_PROVIDED_BY_EMARKETPLACE;
                                                case ResultCode.TRANSACTIONID_WAS_NOT_PROVIDED_BY_EMARKETPLACE:
                                                    result = EPORT_RETURN_CODE_E2;
                                                    reason = Const.STRING_TRANSACTIONID_WAS_NOT_PROVIDED_BY_EMARKETPLACE;
                                                case ResultCode.SUCCESS_BUT_AMOUNT_LESS_THAN_MUST_BE:
                                                    // TODO: Денис, с Суликом - как-то неправильно тут в запросе check мы отвечаем
//                                                    result = OSMP_RETURN_CODE_OK;
//                                                    comment = STRING_SUM_TOO_SMALL;
                                                case ResultCode.AMOUNT_LESS_THAN_MUST_BE:
                                                    // TODO: Денис, с Суликом - как-то неправильно тут в запросе check мы отвечаем
//                                                    result = OSMP_RETURN_CODE_SUM_TOO_SMALL;
//                                                    comment = Const.STRING_FAILED_SUM_TOO_SMALL_AND_ORDER_OWNER_UNDEFINED;
                                                case ResultCode.AMOUNT_MORE_THAN_MUST_BE:
                                                    // TODO: Денис, с Суликом - как-то неправильно тут в запросе check мы отвечаем
//                                                    result = OSMP_RETURN_CODE_SUM_TOO_BIG;
//                                                    comment = Const.STRING_FAILED_SUM_TOO_BIG_AND_ORDER_OWNER_UNDEFINED;
                                                default:
                                                    // TODO: Денис, с Суликом - что еще сделать в таком случае?
                                                    result = EPORT_RETURN_CODE_E2;
                                                    reason = Const.STRING_UNKNOWN_ERROR;
                                            }
                                        } else {
                                            // Не удалось получить ответ от ИМ-на на запрос
                                            result = EPORT_RETURN_CODE_E2;
                                            reason = Const.STRING_UNABLE_TO_REQUEST_EMARKETPLACE_FOR_ORDER_STATUS;
                                        }
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    result = EPORT_RETURN_CODE_E2;
                                    reason = Const.STRING_DB_ERROR;
                                }
                            } catch (Exception ex) {
                                result = EPORT_RETURN_CODE_E1;
                                reason = STRING_TIMESTAMP_PARAMETER_ERROR;
                            }
                        } catch (Exception ex) {
                            result = EPORT_RETURN_CODE_E1;
                            reason = Const.STRING_ENTERED_NUMBER_DOES_NOT_CONFORM_TO_ORDER_FORMAT;
                        }
                    }
                } catch (Exception ex) {
                    result = EPORT_RETURN_CODE_E1;
                    reason = STRING_ACCOUNT_PARAMETER_ERROR;
                }
            } else {
                result = EPORT_RETURN_CODE_E1;
                reason = STRING_UNDEFINED_REQUEST;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            result = EPORT_RETURN_CODE_E1;
            reason = STRING_UNDEFINED_REQUEST;
        } finally {
            try {
                // TODO
                return null;
//                return this.makeUpResponse(txn_id, prv_txn, amount, result, comment);
//
//                response = this.makeUpResponse(context.Server,
//                        type,
//                        result,
//                        reason,
//                        account,
//                        sum,
//                        dtTimestamp,
//                        dtAccepted,
//                        id,
//                        dtPay_time,
//                        dtRevoked,
//                        comment,
//                        debugLogFileStreamWriter);

            } catch (Exception ex) {
                return null;
            }
        }
    }

    public String pay(Map inputData) {
        //TODO: Alexey
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getStatus(Map inputData) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String cancel(Map inputData) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setConfig(InputHandlerConfig config) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
