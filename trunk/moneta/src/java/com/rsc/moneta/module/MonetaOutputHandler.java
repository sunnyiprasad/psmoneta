/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsc.moneta.module;

import com.rsc.moneta.dao.PaymentKeyDao;
import com.rsc.moneta.bean.PaymentKey;
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
    public CheckResponse check(PaymentKey key) {
        try {
            String query = key.getMarket().getCheckUrl();
            query += "?MNT_COMMAND=CHECK&MNT_ID=" + key.getMarket().getId() + "&MNT_TRANSACTION_ID="
                    + key.getKey() + "&MNT_AMOUNT=" + key.getAmount() + "&MNT_CURRENCY_CODE=RUB&MNT_TEST_MODE="
                    + key.getTest() + "&MNT_SIGNATURE=" + Utils.createSignature("check", key);
            URLConnection url = new URL(query).openConnection();
            DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
            fac.setNamespaceAware(true);
            Document doc = fac.newDocumentBuilder().parse(url.getInputStream());
            CheckResponse response = parseResponse(doc);
            if (checkSignature(response)) {
                return response;
            }
        } catch (Exception ex) {
            Logger.getLogger(MonetaOutputHandler.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        return null;
    }

    // Данный метод отправляет запрос в интернет магазин по протоколу монета. См. MONETA.Assistant стр. 21
    public CheckResponse pay(PaymentKey key) {
        try {
            String query = key.getMarket().getCheckUrl();
            query += "?MNT_ID=" + key.getMarket().getId() + "&MNT_TRANSACTION_ID="
                    + key.getKey() + "&MNT_AMOUNT=" + key.getAmount() + "&MNT_CURRENCY_CODE=RUB&MNT_TEST_MODE="
                    + key.getTest() + "&MNT_SIGNATURE=" + Utils.createSignature(key);
            URLConnection url = new URL(query).openConnection();
            DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
            fac.setNamespaceAware(true);
            Document doc = fac.newDocumentBuilder().parse(url.getInputStream());
            CheckResponse response = parseResponse(doc);
            if (checkSignature(response)) {
                return response;
            }
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
        response.setResultCode(Utils.getLongValue("MNT_RESULT_CODE", doc));
        response.setAmount(Utils.getDoubleValue("MNT_AMOUNT", doc));
        response.setOperationId(Utils.getLongValue("MNT_OPERATION_ID", doc));
        response.setTransactionId(doc.getElementsByTagName("MNT_SIGNATURE").item(0).getTextContent());
        return response;
    }

    private boolean checkSignature(CheckResponse response) throws NoSuchAlgorithmException {
        //TODO: Тут необходимо реализовать проверку сигнатуру ответа от ИМ
        if (response.getTransactionId() != null) {
            EntityManager em = EMF.getEntityManager();
            PaymentKey key = new PaymentKeyDao(em).getPaymentKeyByTransactionId(response.getTransactionId());
            if (key.getMarket().getId() == response.getMarketId()) {
                if (response.getSignature() != null) {
                    String sign = response.getResultCode() + response.getMarketId() + response.getTransactionId() + key.getMarket().getPassword();
                    return (response.getSignature().equalsIgnoreCase(Utils.getMd5InHexString(sign)));
                } else {
                    //Отсуствует подпись ответа
                }
            } else {
                //Идентификатор магазина не соответствует указанному.
            }
        } else {
            // Не указан Идентификатор транзакции
        }
        return false;
    }

    public int convertForeignCodeToBase(int code) throws UnknownStatusException {
        switch (code) {
            case MonetaOutputHandler.ANSWER_CONTAINS_AMOUNT:
                return com.rsc.moneta.Const.ORDER_STATUS_ACCEPTED;
            case MonetaOutputHandler.ORDER_IS_CREATE:
                return com.rsc.moneta.Const.ORDER_STATUS_ACCEPTED;
            case MonetaOutputHandler.ORDER_NOT_ACTUAL:
                return com.rsc.moneta.Const.ORDER_STATUS_NOT_PAID_AND_REJECTED_BY_EMARKETPLACE;
            case MonetaOutputHandler.PAYMENT_SUCCESS:
                return com.rsc.moneta.Const.ORDER_STATUS_PAID_AND_COMPLETED;
            case MonetaOutputHandler.UNKNOWN_STATUS:
                return com.rsc.moneta.Const.ORDER_STATUS_PAID_BUT_NOT_COMPLETED_AND_STILL_PROCESSING;
            default:
                throw new UnknownStatusException(code);
        }
    }

    public int convertBaseCodeToForeign(int code) throws UnknownStatusException {
        switch (code) {
            case com.rsc.moneta.Const.ORDER_STATUS_ACCEPTED:
                return MonetaOutputHandler.ORDER_IS_CREATE;
            case com.rsc.moneta.Const.ORDER_STATUS_NOT_PAID_AND_REJECTED_BY_EMARKETPLACE:
                return MonetaOutputHandler.ORDER_NOT_ACTUAL;
            case com.rsc.moneta.Const.ORDER_STATUS_PAID_AND_COMPLETED:
                return MonetaOutputHandler.PAYMENT_SUCCESS;
            case com.rsc.moneta.Const.ORDER_STATUS_PAID_BUT_NOT_COMPLETED_AND_STILL_PROCESSING:
                return MonetaOutputHandler.UNKNOWN_STATUS;
            case com.rsc.moneta.Const.ORDER_STATUS_PAID_BUT_REJECTED_BY_EMARKETPLACE:
                return MonetaOutputHandler.ORDER_NOT_ACTUAL;
            default:
                throw new UnknownStatusException(code);
        }
    }
}
