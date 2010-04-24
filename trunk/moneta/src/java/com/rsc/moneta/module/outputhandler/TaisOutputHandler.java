/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsc.moneta.module.outputhandler;

import com.rsc.moneta.Currency;
import com.rsc.moneta.bean.PaymentOrder;
import com.rsc.moneta.dao.EMF;
import com.rsc.moneta.module.CheckResponse;
import com.rsc.moneta.module.OutputHandler;
import com.rsc.moneta.module.ResultCode;
import com.rsc.moneta.util.Utils;
import java.net.URL;
import java.net.URLConnection;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.commons.io.IOUtils;
import org.w3c.dom.NodeList;

/**
 *
 * @author sulic
 */
public class TaisOutputHandler implements OutputHandler {
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
    public CheckResponse check(PaymentOrder order) {
        try {
            String query = order.getMarket().getCheckUrl();
            query += "?MNT_COMMAND=CHECK&MNT_ID=" + order.getMarket().getId()
                    + "&MNT_TRANSACTION_ID=" + order.getTransactionId()
                    + "&MNT_AMOUNT=" + order.getAmount()
                    + "&MNT_CURRENCY_CODE=" + Utils.accountTypeToString(order.getCurrency())
                    + "&MNT_SIGNATURE=" + Utils.createSignature(order)
                    + "&MNT_OPERATION_ID=" + order.getId();
            int i = order.getTest() ? 1 : 0;
            query += "&MNT_TEST_MODE=" + i;
            if (order.getCustom1() != null) {
                query += "&MNT_CUSTOM1=" + order.getCustom1();
            }
            if (order.getCustom2() != null) {
                query += "&MNT_CUSTOM2=" + order.getCustom2();
            }
            if (order.getCustom3() != null) {
                query += "&MNT_CUSTOM3=" + order.getCustom3();
            }
            query += "&MNT_SIGNATURE=" + Utils.createSignature("check", order);
            Logger.getLogger(TaisOutputHandler.class.getName()).severe(query);
            URLConnection url = new URL(query).openConnection();
            DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
            fac.setNamespaceAware(true);
            String xml = IOUtils.toString(url.getInputStream());
            Logger.getLogger(TaisOutputHandler.class.getName()).severe(xml);
            Document doc = fac.newDocumentBuilder().parse(IOUtils.toInputStream(xml));
            CheckResponse response = parseResponse(doc);
            return process(response, order, true);
        } catch (Exception ex) {
            Logger.getLogger(MonetaOutputHandler.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        return null;
    }

    // Данный метод отправляет запрос в интернет магазин по протоколу монета. См. MONETA.Assistant стр. 21
    public CheckResponse pay(PaymentOrder order) {
        try {
            String query = order.getMarket().getCheckUrl();
            query += "?MNT_ID=" + order.getMarket().getId()
                    + "&MNT_TRANSACTION_ID=" + order.getTransactionId()
                    + "&MNT_AMOUNT=" + order.getAmount()
                    + "&MNT_CURRENCY_CODE=" + Utils.accountTypeToString(order.getCurrency())
                    + "&MNT_SIGNATURE=" + Utils.createSignature(order)
                    + "&MNT_OPERATION_ID=" + order.getId();
            int i = order.getTest() ? 1 : 0;
            query += "&MNT_TEST_MODE=" + i;
            if (order.getCustom1() != null) {
                query += "&MNT_CUSTOM1=" + order.getCustom1();
            }
            if (order.getCustom2() != null) {
                query += "&MNT_CUSTOM2=" + order.getCustom2();
            }
            if (order.getCustom3() != null) {
                query += "&MNT_CUSTOM3=" + order.getCustom3();
            }
            Logger.getLogger(TaisOutputHandler.class.getName()).severe(query);
            URLConnection url = new URL(query).openConnection();
            DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
            fac.setNamespaceAware(true);
            String xml = IOUtils.toString(url.getInputStream());
            Logger.getLogger(TaisOutputHandler.class.getName()).severe(xml);
            Document doc = fac.newDocumentBuilder().parse(IOUtils.toInputStream(xml));
            CheckResponse response = parseResponse(doc);
            return process(response, order, false);
        } catch (Exception ex) {
            Logger.getLogger(MonetaOutputHandler.class.getName()).log(Level.SEVERE, ex.getMessage());
            ex.printStackTrace();
        }
        return null;
    }

    public CheckResponse parseResponse(Document doc) {
        CheckResponse response = new CheckResponse();
        response.setMarketId(Utils.getLongValue("MNT_ID", doc));

        NodeList nodeList = doc.getElementsByTagName("MNT_TRANSACTION_ID");
        if (nodeList != null) {
            if (nodeList.getLength() > 0) {
                response.setTransactionId(nodeList.item(0).getTextContent());
            }
        }

        nodeList = doc.getElementsByTagName("MNT_SIGNATURE");
        if (nodeList != null) {
            if (nodeList.getLength() > 0) {
                response.setSignature(nodeList.item(0).getTextContent());
            }
        }

        nodeList = doc.getElementsByTagName("MNT_DESCRIPTION");
        if (nodeList != null) {
            if (nodeList.getLength() > 0) {
                response.setDescription(nodeList.item(0).getTextContent());
            }
        }
        response.setResultCode(Utils.getIntValue("MNT_RESULT_CODE", doc));
        response.setAmount(Utils.getDoubleValue("MNT_AMOUNT", doc));
        response.setOperationId(Utils.getLongValue("MNT_OPERATION_ID", doc));
        return response;
    }

    private CheckResponse process(CheckResponse response, PaymentOrder order, boolean isCheck) throws NoSuchAlgorithmException {
        if (response.getTransactionId() != null) {
            EntityManager em = EMF.getEntityManager();
            if (order == null) {
                response.setResultCode(ResultCode.ORDER_NOT_FOUND_IN_EMARKETPLACE);
                response.setDescription("Заказ не найден");
            } else {
                if (response.getMarketId() != null) {
                    if (order.getMarket().isSignable()) {
                        if (response.getSignature() != null) {
                            String sign = response.getResultCode() + "";
                            sign += order.getMarket().getId();
                            sign += response.getTransactionId() + order.getMarket().getPassword();
                            String md5 = Utils.getMd5InHexString(sign);
                            if (!response.getSignature().equalsIgnoreCase(md5)) {
                                response.setResultCode(ResultCode.INVALID_SIGN_RETURNED_BY_EMARKETPLACE);
                                response.setDescription("Неправильная подпись");
                                Logger.getLogger(TaisOutputHandler.class.getName()).severe("Invalid sign \n" + response.getSignature() + " my sign\n" + md5 + "\n" + sign);
                            } else {
                                response.setResultCode(convertCheckForeignCodeToBase(response.getResultCode(), isCheck));
                            }
                        } else {
                            response.setResultCode(ResultCode.INVALID_SIGN_RETURNED_BY_EMARKETPLACE);
                            response.setDescription("Отсутствует подпись со стороны интернет магазина");
                            //Отсуствует подпись ответа
                        }
                    } else {
                        response.setResultCode(convertCheckForeignCodeToBase(response.getResultCode(), isCheck));
                    }
                } else {
                    //Идентификатор магазина не соответствует указанному.
                    response.setResultCode(ResultCode.ORDER_NOT_FOUND_IN_EMARKETPLACE);
                    response.setDescription("Идентификатор магазина не соответствует указанному. Скорее всего неправильно введен код заказа.");
                }
            }
        } else {
            // Не указан Идентификатор транзакции
            response.setResultCode(ResultCode.TRANSACTIONID_WAS_NOT_PROVIDED_BY_EMARKETPLACE);
            response.setDescription("Не указан Идентификатор транзакции");
        }
        return response;
    }

    private int convertCheckForeignCodeToBase(int code, boolean isCheck) {
        if (isCheck) {
            switch (code) {
                case ANSWER_CONTAINS_AMOUNT:
                    return ResultCode.SUCCESS_WITH_AMOUNT;
                case ORDER_IS_CREATE:
                    return ResultCode.SUCCESS_WITHOUT_AMOUNT;
                case ORDER_NOT_ACTUAL:
                    return ResultCode.ORDER_NOT_ACTUAL;
                case PAYMENT_SUCCESS:
                    return ResultCode.SUCCESS_WITH_AMOUNT;
                case UNKNOWN_STATUS:
                    return ResultCode.ERROR_TRY_AGAIN;
                default:
                    return ResultCode.UNKNOWN_CODE_RETURNED_BY_EMARKEPLACE;
            }
        } else {
            switch (code) {
                case ANSWER_CONTAINS_AMOUNT:
                    return ResultCode.ERROR_TRY_AGAIN;
                case ORDER_IS_CREATE:
                    return ResultCode.ERROR_TRY_AGAIN;
                case ORDER_NOT_ACTUAL:
                    return ResultCode.INTERNAL_ERROR;
                case PAYMENT_SUCCESS:
                    return ResultCode.SUCCESS_WITHOUT_AMOUNT;
                case UNKNOWN_STATUS:
                    return ResultCode.ERROR_TRY_AGAIN;
                default:
                    return ResultCode.UNKNOWN_CODE_RETURNED_BY_EMARKEPLACE;
            }
        }
    }
}
