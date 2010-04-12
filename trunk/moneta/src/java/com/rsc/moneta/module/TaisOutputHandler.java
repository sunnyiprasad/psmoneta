/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsc.moneta.module;
import com.rsc.moneta.bean.PaymentOrder;
import com.rsc.moneta.dao.EMF;
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
/**
 *
 * @author sulic
 */
public class TaisOutputHandler implements OutputHandler{
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
            query += "?MNT_COMMAND=CHECK&MNT_ID=" + order.getMarket().getId() + "&MNT_TRANSACTION_ID="
                    + order.getTransactionId() + "&MNT_CURRENCY_CODE=RUB&MNT_TEST_MODE="
                    + order.getTest();
            if (order.getAmount() != null) {
                query += "&MNT_AMOUNT=" + order.getAmount();
            }
            query += "&MNT_SIGNATURE=" + Utils.createSignature("check", order);
            URLConnection url = new URL(query).openConnection();
            DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
            fac.setNamespaceAware(true);
            String xml = IOUtils.toString(url.getInputStream());
            System.out.println(xml);
            Document doc = fac.newDocumentBuilder().parse(IOUtils.toInputStream(xml));
            CheckResponse response = parseResponse(doc);
            return process(response, order);
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
            query += "?MNT_ID=" + order.getMarket().getId() + "&MNT_TRANSACTION_ID="
                    + order.getTransactionId() + "&MNT_AMOUNT=" + order.getAmount() + "&MNT_CURRENCY_CODE=RUB&MNT_TEST_MODE="
                    + order.getTest() + "&MNT_SIGNATURE=" + Utils.createSignature(order);
            URLConnection url = new URL(query).openConnection();
            DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
            fac.setNamespaceAware(true);
            String xml = IOUtils.toString(url.getInputStream());
            System.out.println(xml);
            Document doc = fac.newDocumentBuilder().parse(IOUtils.toInputStream(xml));
            CheckResponse response = parseResponse(doc);
            return process(response, order);
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

    private CheckResponse process(CheckResponse response, PaymentOrder order) throws NoSuchAlgorithmException {
        if (response.getTransactionId() != null) {
            EntityManager em = EMF.getEntityManager();
            if (order == null) {
                response.setResultCode(ResultCode.ORDER_NOT_FOUND);
                response.setComment("Заказ не найден");
            } else {
                if (response.getMarketId() != null) {
                    if (order.getMarket().isSignable()) {
                        if (response.getSignature() != null) {
                            String sign = response.getResultCode() + "";
                            sign += order.getMarket().getId();
                            sign += response.getTransactionId() + order.getMarket().getPassword();
                            String md5 = Utils.getMd5InHexString(sign);
                            if (!response.getSignature().equalsIgnoreCase(md5)) {
                                response.setResultCode(ResultCode.INVALID_SIGN);
                                response.setDescription("Неправильная подпись");
                                System.out.println("Invalid sign \n" + response.getSignature() + " my sign\n" + md5 + "\n" + sign);
                            } else {
                                response.setResultCode(convertForeignCodeToBase(response.getResultCode()));
                            }
                        } else {
                            response.setResultCode(ResultCode.INVALID_SIGN);
                            response.setDescription("Отсутствует подпись со стороны интернет магазина");
                            //Отсуствует подпись ответа
                        }
                    } else {
                        response.setResultCode(convertForeignCodeToBase(response.getResultCode()));
                    }
                } else {
                    //Идентификатор магазина не соответствует указанному.
                    response.setResultCode(ResultCode.ORDER_NOT_FOUND);
                    response.setDescription("Идентификатор магазина не соответствует указанному. Скорее всего неправильно введен код заказа.");
                }
            }
        } else {
            // Не указан Идентификатор транзакции
            response.setResultCode(ResultCode.TRANSACTIONID_NOT_DEFINE);
            response.setDescription("Не указан Идентификатор транзакции");
        }
        return response;
    }


    public int convertForeignCodeToBase(int code) {
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
                return ResultCode.UNKNOWN_CODE;
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

}
