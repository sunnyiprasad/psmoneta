/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsc.moneta.module;

import com.rsc.moneta.dao.PaymentOrderDao;
import com.rsc.moneta.bean.PaymentOrder;
import com.rsc.moneta.dao.EMF;
import com.rsc.moneta.util.Utils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.XMLReader;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.commons.io.IOUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author sulic
 */
public class MonetaOutputHandler implements OutputHandler {

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

    // Данный метод отправляет запрос в интернет магазин по протоколу монета. См. MONETA.Assistant стр. 20
    public CheckResponse check(PaymentOrder key) {
        try {
            String query = key.getMarket().getCheckUrl();
            query += "?MNT_COMMAND=CHECK&MNT_ID=" + key.getMarket().getId() + "&MNT_TRANSACTION_ID="
                    + key.getTransactionId() + "&MNT_CURRENCY_CODE=RUB&MNT_TEST_MODE="
                    + key.getTest();
            if (key.getAmount() != null) {
                query += "&MNT_AMOUNT=" + key.getAmount();
            }
            query += "&MNT_SIGNATURE=" + Utils.createSignature("check", key);
            URLConnection url = new URL(query).openConnection();
            DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
            fac.setNamespaceAware(true);
            String xml = IOUtils.toString(url.getInputStream());
            System.out.println(xml);
            Document doc = fac.newDocumentBuilder().parse(IOUtils.toInputStream(xml));
            CheckResponse response = parseResponse(doc);
            return checkSignature(response);
        } catch (Exception ex) {
            Logger.getLogger(MonetaOutputHandler.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        return null;
    }

    // Данный метод отправляет запрос в интернет магазин по протоколу монета. См. MONETA.Assistant стр. 21
    public CheckResponse pay(PaymentOrder key) {
        try {
            String query = key.getMarket().getCheckUrl();
            query += "?MNT_ID=" + key.getMarket().getId() + "&MNT_TRANSACTION_ID="
                    + key.getTransactionId() + "&MNT_AMOUNT=" + key.getAmount() + "&MNT_CURRENCY_CODE=RUB&MNT_TEST_MODE="
                    + key.getTest() + "&MNT_SIGNATURE=" + Utils.createSignature(key);
            URLConnection url = new URL(query).openConnection();
            DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
            fac.setNamespaceAware(true);
            Document doc = fac.newDocumentBuilder().parse(url.getInputStream());
            CheckResponse response = parseResponse(doc);
            return checkSignature(response);
        } catch (Exception ex) {
            Logger.getLogger(MonetaOutputHandler.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        return null;
    }

    public CheckResponse parseResponse(Document doc) {
        CheckResponse response = new CheckResponse();
        response.setMarketId(Utils.getLongValue("MNT_ID", doc));
        response.setTransactionId(doc.getElementsByTagName("MNT_TRANSACTION_ID").item(0).getTextContent());
        response.setResultCode(Utils.getIntValue("MNT_RESULT_CODE", doc));
        response.setAmount(Utils.getDoubleValue("MNT_AMOUNT", doc));
        response.setOperationId(Utils.getLongValue("MNT_OPERATION_ID", doc));
        response.setSignature(doc.getElementsByTagName("MNT_SIGNATURE").item(0).getTextContent());
        response.setDescription(doc.getElementsByTagName("MNT_DESCRIPTION").item(0).getTextContent());
        return response;
    }

    private CheckResponse checkSignature(CheckResponse response) throws NoSuchAlgorithmException {
        //TODO: Тут необходимо реализовать проверку сигнатуру ответа от ИМ
        if (response.getTransactionId() != null) {
            EntityManager em = EMF.getEntityManager();
            PaymentOrder key = new PaymentOrderDao(em).getPaymentKey(response.getTransactionId(), response.getMarketId());
            if (key == null) {
                response.setResultCode(ResultCode.INTERNAL_ERROR);
                response.setComment("Заказ не найден");
            } else {
                if (key.getMarket().getId() == response.getMarketId()) {
                    if (response.getSignature() != null) {
                        String sign = response.getResultCode() + response.getMarketId() + response.getTransactionId() + key.getMarket().getPassword();
                        if (!response.getSignature().equalsIgnoreCase(Utils.getMd5InHexString(sign))){
                            response.setResultCode(ResultCode.INTERNAL_ERROR);
                            response.setDescription("Неправильная подпись");
                        }
                    } else {
                        response.setResultCode(ResultCode.INTERNAL_ERROR);
                        response.setDescription("Отсутствует подпись со стороны интернет магазина");
                        //Отсуствует подпись ответа
                    }
                } else {
                    //Идентификатор магазина не соответствует указанному.
                    response.setResultCode(ResultCode.INTERNAL_ERROR);
                    response.setDescription("Идентификатор магазина не соответствует указанному.");
                }
            }
        } else {
            // Не указан Идентификатор транзакции
            response.setResultCode(ResultCode.INTERNAL_ERROR);
            response.setDescription("Не указан Идентификатор транзакции");
        }
        return response;
    }

    // Метод соотнесения статуса заказа, возвращённого Интернет-Магазином и
    // статусов заказов ПС ТЛСМ, возвращаемых информационной системой 
    // Интернет - Магазина в ответ на  запросы "check" от ПС ТЛСМ
    public CheckResponseReturnCodes convertEmarketplaceReturnCodeToTLSMReturnCode(int emarketplaceReturnCode) throws UnknownStatusException {
        if (emarketplaceReturnCode == ANSWER_CONTAINS_AMOUNT) {
            return CheckResponseReturnCodes.ORDER_IS_VALID_AND_RESPONSE_CONTAINS_AMOUNT;
        } else {
            if (emarketplaceReturnCode == PAYMENT_SUCCESS) {
                return CheckResponseReturnCodes.ORDER_IS_COMPLETED_AND_TLSM_NOTIFIED;
            } else {
                if (emarketplaceReturnCode == UNKNOWN_STATUS || emarketplaceReturnCode == ORDER_IS_CREATE) {
                    return CheckResponseReturnCodes.ORDER_IS_VALID_AND_PROCESSING;
                } else {
                    if (emarketplaceReturnCode == ORDER_NOT_ACTUAL) {
                        return CheckResponseReturnCodes.ORDER_IS_INVALID;
                    } else {
                        throw new UnknownStatusException(emarketplaceReturnCode);
                    }
                }
            }
        }
    }

    // TODO: Денис, ИМХО неправильно такой метод иметь, так как я считаю, что:
    //18:19:15] Denis Solodovnikov говорит: мое
    //мнение такое что неправильно сопоставлять статус ответа от ИМ со статусом соотвествующего ему будущего нашего ответа терминальной ПС, так как один и тот же статус ответа от ИМ может соотвествовать разным статусам ответа терминальной ПС
    //
    //- в зависимости от нашей логики[18:
    //
    //21:43] Denis Solodovnikov говорит: в
    //итоге можно сделать класс либо енум наших статусов ответа от ИМ[18:
    //
    //22:06] Denis Solodovnikov говорит: и
    //вот к ним приводить статусы ответа от реальных ИМ в твоей функции[
    //
    //18:23:00] Denis Solodovnikov говорит: а
    //затем эти наши статусы ответа я анализирую в хендлере и решаю чего отправлять терминальной ПС
    public int convertForeignCodeToBase(int code) throws UnknownStatusException {
        switch (code) {
            case MonetaOutputHandler.ANSWER_CONTAINS_AMOUNT:
                return ResultCode.SUCCESS_WITH_AMOUNT;
            case MonetaOutputHandler.ORDER_IS_CREATE:
                return ResultCode.SUCCESS_WITHOUT_AMOUNT;
            case MonetaOutputHandler.ORDER_NOT_ACTUAL:
                return ResultCode.ORDER_NOT_ACTUAL;
            case MonetaOutputHandler.PAYMENT_SUCCESS:
                return ResultCode.SUCCESS_WITH_AMOUNT;
            case MonetaOutputHandler.UNKNOWN_STATUS:
                return ResultCode.ERROR_TRY_AGAIN;
            default:
                throw new UnknownStatusException(code);
        }
    }
}
