/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsc.moneta.module;

import java.util.Map;
import javax.persistence.EntityManager;
import java.util.regex.*;

import com.rsc.moneta.dao.EMF;
import com.rsc.moneta.dao.PaymentKeyDao;
import com.rsc.moneta.bean.PaymentKey;


/**
 * Класс представляет класс - обработчик запросов, поступающих от терминала ОСМП
 * @author Солодовников Д.А.
 */
public class OSMPInputHandler implements InputHandler{

    // Данный метод проверяет возможность платежа.
    // См. ОПИСАНИЕ ИНТЕРФЕЙСА ОСМП стр. 6.
    public String check(Map inputData) {

        String response = "";

        // внутренний номер платежа в системе ОСМП (параметр из ОСМП-запроса)
        String txn_id = ""; 

        // уникальный номер операции пополнения баланса абонента (в базе
        // провайдера), целое число длиной до 20 знаков (параметр из ОСМП-запроса)
        int prv_txn = -1;

        // сумма платежа, Если сумма представляет целое число, то оно все равно дополняется точкой и нулями, например – «152.00»
        // (параметр из ОСМП - запроса)
        double sum = -1.0;

        // идентификатор абонента в информационной системе провайдера (параметр из ОСМП-запроса)
        String account = "";

        // Идентификатор кода заказа ПС ххх
        long paymentKeyId = -1;

        // command=check – запрос на проверку состояния абонента
        String command = "";

        int result = Const.OSMP_RETURN_CODE_OTHER_ERROR;
        String comment = "";

        try {
            command = inputData.get("command").toString();
            try {
                if (command == "check")
                {
                    try {
                        txn_id = inputData.get("txn_id").toString();
                        if (!this.regexMatch("^[0-9]{1,20}$", txn_id)){
                            result = Const.OSMP_RETURN_CODE_OTHER_ERROR;
                            comment = Const.STRING_TXN_ID_PARAMETER_ERROR;
                        }
                        else
                        {
                            try {
                                account = inputData.get("account").toString();
                                if (!this.regexMatch("^[0-9]{19}$", account)) {
                                    result = Const.OSMP_RETURN_CODE_ACCOUNT_ILLEGAL_FORMAT;
                                    comment = Const.STRING_ENTERED_NUMBER_DOES_NOT_CONFORM_TO_CARD_FORMAT;
                                }
                                else {
                                    try {
                                        paymentKeyId = Long.parseLong(account);
                                        try {
                                            // TODO: уточнить у Сулика вообще надо ли проверять наличие ненужного нам параметра sum ?
                                            // sum = ...
                                            try {
                                                // TODO: Выяснить, является ли этот сиреновский билет либо наш код заказа неотработанным
                                                EntityManager em = EMF.getEntityManager();
                                                PaymentKey paymentKey = new PaymentKeyDao(em).getPaymentById(paymentKeyId);

                                                // TODO: !!!
                                                if (true) {
                                                    // TODO: Выполнить проверки валидности номера заказа средствами нашей системы (если это возможно)
                                                }
                                                else {
                                                    prv_txn = 0; // TODO: присваивать тот самый сиреновский билет либо наш код заказа в том случае если заказ уже отработан
                                                    result = Const.OSMP_RETURN_CODE_OK;
                                                    comment = Const.STRING_PAYMENT_COMPLETED;
                                                }
                                            }
                                            catch (Exception ex) {
                                            }
                                        }
                                        catch (Exception ex) {
                                            result = Const.OSMP_RETURN_CODE_OTHER_ERROR;
                                            comment = Const.STRING_SUM_PARAMETER_ERROR;
                                        }

                                    }
                                    catch (Exception ex) {
                                        result = Const.OSMP_RETURN_CODE_ACCOUNT_ILLEGAL_FORMAT;
                                        comment = Const.STRING_ENTERED_NUMBER_DOES_NOT_CONFORM_TO_CARD_FORMAT;
                                    }
                                }
                            }
                            catch (Exception ex){
                                result = Const.OSMP_RETURN_CODE_OTHER_ERROR;
                                comment = Const.STRING_ACCOUNT_PARAMETER_ERROR;
                            }
                        }
                    }
                    catch (Exception ex){
                        result = Const.OSMP_RETURN_CODE_OTHER_ERROR;
                        comment = Const.STRING_TXN_ID_PARAMETER_ERROR;
                    }
                }
                else
                {
                    result = Const.OSMP_RETURN_CODE_OTHER_ERROR;
                    comment = Const.STRING_COMMAND_PARAMETER_ERROR;
                }
            }
            catch (Exception ex) {
                result = Const.OSMP_RETURN_CODE_OTHER_ERROR;
                comment = Const.STRING_UNKNOWN_ERROR;
            }
        }
        catch (Exception ex) {
            result = Const.OSMP_RETURN_CODE_OTHER_ERROR;
            comment = Const.STRING_COMMAND_PARAMETER_ERROR;
        }



        return response;


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
        }
        else {
            return false;
        }
    }

}
