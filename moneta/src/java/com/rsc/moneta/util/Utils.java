/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsc.moneta.util;

import com.rsc.moneta.bean.PaymentKey;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author sulic
 */
public class Utils {

    static final String HEXES = "0123456789ABCDEF";

    public static String byteArrayToHexString(byte[] raw) {
         if (raw == null) {
            return null;
        }
        final StringBuilder hex = new StringBuilder(2 * raw.length);
        for (final byte b : raw) {
            hex.append(HEXES.charAt((b & 0xF0) >> 4)).append(HEXES.charAt((b & 0x0F)));
        }
        return hex.toString();
    }

    public static String getMd5InHexString(String data) throws NoSuchAlgorithmException {
        // На вход подается строка, на выходе выдается MD5 в формате HEX в caps lock
        return Utils.byteArrayToHexString(Utils.md5(data));
    }

    public static byte[] md5(String data) throws NoSuchAlgorithmException {
        //вычисляет md5 хэш
        MessageDigest digest = MessageDigest.getInstance("MD5");
        digest.update(data.getBytes());
        return digest.digest();
    }

    public static Long getLongValue(String tagName, Document doc) {
        NodeList list = doc.getElementsByTagName(tagName);
        try {
            Node node = list.item(0);
            return Long.parseLong(node.getTextContent());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Double getDoubleValue(String tagName, Document doc) {
        NodeList list = doc.getElementsByTagName(tagName);
        try {
            Node node = list.item(0);
            return Double.parseDouble(node.getTextContent());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static String createSignature(PaymentKey key) {
        //TODO: Генерация сигнатуры для заказа.
        return "";
    }
}
