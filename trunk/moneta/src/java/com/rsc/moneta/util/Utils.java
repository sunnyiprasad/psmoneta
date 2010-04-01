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

    public static String byteArrayToHexString(byte [] data){
        return "";
    }

    public static byte [] md5(String data) throws NoSuchAlgorithmException{
        MessageDigest digest = MessageDigest.getInstance("MD5");
        digest.update(data.getBytes());
        return digest.digest();
    }

    public static Long getLongValue(String tagName, Document doc){
        NodeList list = doc.getElementsByTagName(tagName);
        try{
            Node node = list.item(0);
            return Long.parseLong(node.getTextContent());
        }catch(NumberFormatException e){
            return null;
        }
    }
    public static Double getDoubleValue(String tagName, Document doc){
        NodeList list = doc.getElementsByTagName(tagName);
        try{
            Node node = list.item(0);
            return Double.parseDouble(node.getTextContent());
        }catch(NumberFormatException e){
            return null;
        }
    }

    public static String createSignature(PaymentKey key) {
        //TODO: Генерация сигнатуры для заказа.
        return "";
    }

}
