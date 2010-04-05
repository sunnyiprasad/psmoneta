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
}
