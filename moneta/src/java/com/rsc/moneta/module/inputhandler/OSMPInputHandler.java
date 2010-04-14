/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsc.moneta.module.inputhandler;

import com.opensymphony.xwork2.Result;
import java.util.Map;
import javax.persistence.EntityManager;
import java.util.regex.*;

import com.rsc.moneta.dao.EMF;
import com.rsc.moneta.bean.PaymentOrder;
import com.rsc.moneta.module.ResultCode;
//import com.rsc.moneta.Currency;
import com.rsc.moneta.module.CheckResponse;
import com.rsc.moneta.module.InputHandler;
import com.rsc.moneta.module.MainPaymentHandler;

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
    public final static String STRING_ORDER_REJECTED_BY_EMARKETPLACE_BEFORE_TLSM_PAYMENT = "Интернет-Магазином заказ признан инвалидным и отменён до получения оплаты";
    public final static String STRING_ORDER_REJECTED_BY_EMARKETPLACE_AFTER_TLSM_PAYMENT = "Интернет-Магазином заказ признан инвалидным и отменён после получения оплаты";
    public final static String STRING_UNABLE_TO_REQUEST_EMARKETPLACE_FOR_ORDER_STATUS = "Нет возможности запросить статус заказа у Интернет-Магазина";
    public final static String STRING_ORDER_IS_INVALID_FOR_EMARKETPLACE = "Заказ не валиден в информационной системе Интернет-Магазина (отменён и т.д.)";
    public final static String STRING_ORDER_PAID_AND_COMPLETED = "Этот заказ уже обработан и оплачен";
    public final static String STRING_ORDER_IS_PROCESSING_IN_EMARKETPLACE = "Заказ находится в стадии обработки";
    public final static String STRING_EMARKETPLACE_UNABLE_TO_RESPOND = "Интернет-Магазин не в состоянии ответить на отправленный запрос в данный момент";
    public final static String STRING_UNKNOWN_CODE_RETURNED_BY_EMARKEPLACE = "Интернет-Магазин возвратил неизвестный код результата";

//    public final static String STRING_SUM_TOO_SMALL = "Сумма слишком мала";
//    public final static String STRING_SUM_TOO_BIG = "Сумма слишком велика";


    // Данный метод проверяет возможность платежа.
    // См. ОПИСАНИЕ ИНТЕРФЕЙСА ОСМП стр. 6.
    public String check(Map inputData) {

        // внутренний номер платежа в системе ОСМП (параметр из ОСМП-запроса)
        String txn_id = "";

        // уникальный номер операции пополнения баланса абонента (в базе
        // провайдера), целое число длиной до 20 знаков
        long prv_txn = -1;

        // сумма платежа, Если сумма представляет целое число, то оно все равно дополняется точкой и нулями, например – «152.00»
        // (параметр из ОСМП - запроса)
        double sum = -1.0;

        // идентификатор абонента в информационной системе провайдера (параметр из ОСМП-запроса)
        String account = "";

        // Идентификатор кода заказа ТЛСМ
        long paymentKeyId = -1;

        // command=check – запрос на проверку состояния абонента
        String command = "";

        int result = OSMP_RETURN_CODE_OTHER_ERROR;
        String comment = "";

        try {
            command = inputData.get("command").toString();
            try {
                if (command.equals("check")) {
                    try {
                        txn_id = inputData.get("txn_id").toString();
                        if (!this.regexMatch("^[0-9]{1,20}$", txn_id)) {
                            result = OSMP_RETURN_CODE_OTHER_ERROR;
                            comment = STRING_TXN_ID_PARAMETER_ERROR;
                        } else {
                            try {
                                account = inputData.get("account").toString();
                                if (!this.regexMatch("^[0-9]{19}$", account)) {
                                    result = OSMP_RETURN_CODE_ACCOUNT_ILLEGAL_FORMAT;
                                    comment = STRING_ENTERED_NUMBER_DOES_NOT_CONFORM_TO_ORDER_FORMAT;
                                } else {
                                    try {
                                        paymentKeyId = Long.parseLong(account);
                                        // Решено по согласованию с "Платика" что параметр sum не должен быть в запросе "check"
                                        try {
                                            EntityManager em = EMF.getEntityManager();
                                            
                                            //Поиск по первичному ключу для всех объектов уже реализован JPA
                                            PaymentOrder paymentOrder = em.find(PaymentOrder.class, paymentKeyId);

                                            if (paymentOrder == null) {
                                                // Заказ не найден. Введен несуществующий код заказа.
                                                result = OSMP_RETURN_CODE_ACCOUNT_NOT_FOUND;
                                                comment = STRING_ORDER_DOES_NOT_EXIST_ERROR;
                                            } else {
                                                // Денис: Сулик, вот так не пиши, это во-первых неправильно, во-вторых, если считаешь,
                                                // что этот статус не нужен, спроси хотя бы у меня накой я его ввёл
                                                // if (paymentKey.getStatus() == com.rsc.moneta.Currency.ORDER_STATUS_ACCEPTED) {
                                                if (paymentOrder.getStatus() == PaymentOrder.ORDER_STATUS_ACCEPTED) {
                                                    result = OSMP_RETURN_CODE_ACCOUNT_NOT_FOUND;
                                                    comment = STRING_ORDER_DOES_NOT_EXIST_ERROR;
                                                } else {
                                                    if (paymentOrder.getStatus() == PaymentOrder.ORDER_STATUS_ACCEPTED) {
                                                        // Заказ в ПС ТЛСМ найден. 
                                                        MainPaymentHandler handler = new MainPaymentHandler(em);

                                                        // Проверка его статуса в ИМ - отправка запроса check
                                                        CheckResponse checkResponse = handler.check(paymentOrder);

                                                        if (checkResponse != null) {
                                                            int TLSMResultCode = checkResponse.getResultCode();
                                                            if (TLSMResultCode == ResultCode.SUCCESS_WITH_AMOUNT) {
                                                                // TODO: тут должна быть проверка суммы заказа - для каждого ИМ-а надо
                                                                // проверять на вхождение в рамки мин. и макс. суммы заказа
                                                                sum = checkResponse.getAmount();
                                                                result = OSMP_RETURN_CODE_OK;
                                                                comment = "";
                                                            } else {
                                                                if (TLSMResultCode == ResultCode.SUCCESS_WITHOUT_AMOUNT)
                                                                {
                                                                    // TODO:!!!
                                                                }else {
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
                                                                            }
                                                                            else {
                                                                                if (TLSMResultCode == ResultCode.ERROR_TRY_AGAIN) {
                                                                                    result = OSMP_RETURN_CODE_TEMPORARY_ERROR;;
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
                                                                                            // TODO: !!!
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
                                                    } else {
                                                        if (paymentOrder.getStatus() == PaymentOrder.ORDER_STATUS_NOT_PAID_AND_REJECTED_BY_EMARKETPLACE) {
                                                            result = OSMP_RETURN_CODE_PAY_SUPPRESS;
                                                            comment = STRING_ORDER_REJECTED_BY_EMARKETPLACE_BEFORE_TLSM_PAYMENT;
                                                        } else {
                                                            if (paymentOrder.getStatus() == PaymentOrder.ORDER_STATUS_PAID_AND_COMPLETED) {
                                                                // TODO: !!!
                                                            } else {
                                                                if (paymentOrder.getStatus() == PaymentOrder.ORDER_STATUS_PAID_BUT_NOT_COMPLETED_AND_STILL_PROCESSING) {
                                                                    // TODO: Сулик, Денис - Обдумать, что в это случае делать
                                                                    throw new UnsupportedOperationException("Not supported yet.");
                                                                } else {
                                                                    if (paymentOrder.getStatus() == PaymentOrder.ORDER_STATUS_PAID_BUT_REJECTED_BY_EMARKETPLACE) {
                                                                        result = OSMP_RETURN_CODE_PAY_SUPPRESS;
                                                                        comment = STRING_ORDER_REJECTED_BY_EMARKETPLACE_AFTER_TLSM_PAYMENT;
                                                                    } else {
                                                                        result = OSMP_RETURN_CODE_OTHER_ERROR;
                                                                        comment = STRING_DB_ERROR;
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        } catch (Exception ex) {
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
            result = OSMP_RETURN_CODE_OTHER_ERROR;
            comment = STRING_COMMAND_PARAMETER_ERROR;
        } finally {
            try {
                return this.makeUpResponse(txn_id, prv_txn, sum, result, comment);
            } catch (Exception ex) {
                return null;
            }
        }
    }

    // Данный метод проводит платеж
    // См. ОПИСАНИЕ ИНТЕРФЕЙСА ОСМП стр. 7.
    public String pay(Map inputData) {
        //TODO: Denis
        throw new UnsupportedOperationException("Not supported yet.");
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
        // TODO: Сулик уточнит будет ли вообще приходить сумма в запросе, если будет - надо её возвращать
//        if (amount != -1.00) {
//            response +=
//                     "<sum>" + amount.ToString("f2", new System.Globalization.CultureInfo("en-US", false).NumberFormat) + "</sum>";
//        }
        response +=
                "<result>" + String.format("%d", result) + "</result>";
        response +=
                "<comment>" + comment + "</comment>";
        response +=
                "</response>";
        return response;
    }
}
