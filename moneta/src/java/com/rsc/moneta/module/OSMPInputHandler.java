/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsc.moneta.module;

import java.util.Map;
import javax.persistence.EntityManager;
import java.util.regex.*;

import com.rsc.moneta.dao.EMF;
import com.rsc.moneta.bean.PaymentKey;

/**
 * Класс представляет класс - обработчик запросов, поступающих от терминала ОСМП
 * @author Солодовников Д.А.
 */
public class OSMPInputHandler implements InputHandler {

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

        int result = Const.OSMP_RETURN_CODE_OTHER_ERROR;
        String comment = "";

        try {
            command = inputData.get("command").toString();
            try {
                if (command.equals("check")) {
                    try {
                        txn_id = inputData.get("txn_id").toString();
                        if (!this.regexMatch("^[0-9]{1,20}$", txn_id)) {
                            result = Const.OSMP_RETURN_CODE_OTHER_ERROR;
                            comment = Const.STRING_TXN_ID_PARAMETER_ERROR;
                        } else {
                            try {
                                account = inputData.get("account").toString();
                                if (!this.regexMatch("^[0-9]{19}$", account)) {
                                    result = Const.OSMP_RETURN_CODE_ACCOUNT_ILLEGAL_FORMAT;
                                    comment = Const.STRING_ENTERED_NUMBER_DOES_NOT_CONFORM_TO_CARD_FORMAT;
                                } else {
                                    try {
                                        paymentKeyId = Long.parseLong(account);
                                        try {
                                            // TODO: Сулик - уточнить вообще надо ли проверять наличие ненужного нам параметра sum ?
                                            // sum = ...
                                            try {
                                                // TODO: Выяснить, является ли этот сиреновский билет либо наш код заказа неотработанным
                                                EntityManager em = EMF.getEntityManager();

                                                //Поиск по первичному ключу для всеъ объектов уже реализован JPA
                                                PaymentKey paymentKey = em.find(PaymentKey.class, paymentKeyId);

                                                if (paymentKey == null) {
                                                    // Заказ не найден. Введен несуществующий код заказа.
                                                    result = Const.OSMP_RETURN_CODE_ACCOUNT_NOT_FOUND;
                                                    comment = Const.STRING_ORDER_DOES_NOT_EXIST_ERROR;
                                                } else {
                                                    // Денис: Сулик, вот так не пиши, это во-первых неправильно, во-вторых, если считаешь,
                                                    // что этот статус не нужен, спроси хотя бы у меня накой я его ввёл
                                                    // if (paymentKey.getOrderStatus() == com.rsc.moneta.Const.ORDER_STATUS_ACCEPTED) {
                                                    if (paymentKey.getOrderStatus() == com.rsc.moneta.Const.ORDER_STATUS_UNDEFINED) {
                                                        result = Const.OSMP_RETURN_CODE_ACCOUNT_NOT_FOUND;
                                                        comment = Const.STRING_ORDER_DOES_NOT_EXIST_ERROR;
                                                    } else {
                                                        if (paymentKey.getOrderStatus() == com.rsc.moneta.Const.ORDER_STATUS_ACCEPTED) {
                                                            // TODO: "спросить" у Интернет-Магазина, валиден ли заказ и выполнить дальнейшую обработку

                                                            /////////// Код Сулика важный для меня для понимания

                                                            // Заказ найден. Проверяем его статус
                                                            // Все нормально можно отправлять чек в ИМ
                                                            // Создаем класс для работы с ИМ
                                                            MonetaOutputHandler handler = new MonetaOutputHandler();
                                                            CheckResponse checkResponse = handler.check(paymentKey);
                                                            if (checkResponse != null) {
                                                                //TODO: Тут надо не просто код в лонге присвоить, в строке с нулями.
                                                                // Метод преобразующий лонг в строку с нулями, засунуть в Utils что бы можно было переиспользовать.
                                                                // Вот здесь будет код ошибки или успеха нашей системы!
                                                                // Его надо конвертировать в систему кодирования ошибок ОСМП и отправить им ответ.
                                                                long resultCode = checkResponse.getResultCode();

                                                                prv_txn = paymentKey.getId();
                                                                result = Const.OSMP_RETURN_CODE_OK;
                                                                comment = Const.STRING_PAYMENT_COMPLETED;
                                                            } else {
                                                                // Что то не то, не должен вернуться null.
                                                                // Если вернулся нуль, значит проблемы со связью до ИМ или ещё по каким причинам недоступен ИМ.
                                                            }
                                                            /////////// Код Сулика важный для меня для понимания

                                                        } else {
                                                            if (paymentKey.getOrderStatus() == com.rsc.moneta.Const.ORDER_STATUS_NOT_PAID_AND_REJECTED_BY_EMARKETPLACE) {
                                                                result = Const.OSMP_RETURN_CODE_PAY_SUPPRESS;
                                                                comment = Const.STRING_ORDER_REJECTED_BY_EMARKETPLACE_BEFORE_TLSM_PAYMENT;
                                                            } else {
                                                                if (paymentKey.getOrderStatus() == com.rsc.moneta.Const.ORDER_STATUS_PAID_AND_COMPLETED) {
                                                                    prv_txn = paymentKey.getId();
                                                                    result = Const.OSMP_RETURN_CODE_OK;
                                                                    comment = Const.STRING_PAYMENT_COMPLETED;
                                                                } else {
                                                                    if (paymentKey.getOrderStatus() == com.rsc.moneta.Const.ORDER_STATUS_PAID_BUT_NOT_COMPLETED_AND_STILL_PROCESSING) {
                                                                        // TODO: Сулик, Денис - Обдумать, что в это случае делать
                                                                        throw new UnsupportedOperationException("Not supported yet.");
                                                                    } else {
                                                                        if (paymentKey.getOrderStatus() == com.rsc.moneta.Const.ORDER_STATUS_PAID_BUT_REJECTED_BY_EMARKETPLACE) {
                                                                            result = Const.OSMP_RETURN_CODE_PAY_SUPPRESS;
                                                                            comment = Const.STRING_ORDER_REJECTED_BY_EMARKETPLACE_AFTER_TLSM__PAYMENT;
                                                                        } else {
                                                                            result = Const.OSMP_RETURN_CODE_OTHER_ERROR;
                                                                            comment = Const.STRING_DB_ERROR;
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            } catch (Exception ex) {
                                                result = Const.OSMP_RETURN_CODE_OTHER_ERROR;
                                                comment = Const.STRING_UNKNOWN_ERROR;
                                            }
                                        } catch (Exception ex) {
                                            result = Const.OSMP_RETURN_CODE_OTHER_ERROR;
                                            comment = Const.STRING_SUM_PARAMETER_ERROR;
                                        }

                                    } catch (Exception ex) {
                                        result = Const.OSMP_RETURN_CODE_ACCOUNT_ILLEGAL_FORMAT;
                                        comment = Const.STRING_ENTERED_NUMBER_DOES_NOT_CONFORM_TO_CARD_FORMAT;
                                    }
                                }
                            } catch (Exception ex) {
                                result = Const.OSMP_RETURN_CODE_OTHER_ERROR;
                                comment = Const.STRING_ACCOUNT_PARAMETER_ERROR;
                            }
                        }
                    } catch (Exception ex) {
                        result = Const.OSMP_RETURN_CODE_OTHER_ERROR;
                        comment = Const.STRING_TXN_ID_PARAMETER_ERROR;
                    }
                } else {
                    result = Const.OSMP_RETURN_CODE_OTHER_ERROR;
                    comment = Const.STRING_COMMAND_PARAMETER_ERROR;
                }
            } catch (Exception ex) {
                result = Const.OSMP_RETURN_CODE_OTHER_ERROR;
                comment = Const.STRING_UNKNOWN_ERROR;
            }
        } catch (Exception ex) {
            result = Const.OSMP_RETURN_CODE_OTHER_ERROR;
            comment = Const.STRING_COMMAND_PARAMETER_ERROR;
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
