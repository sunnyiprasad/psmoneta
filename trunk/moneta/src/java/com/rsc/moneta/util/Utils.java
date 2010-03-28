/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsc.moneta.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author sulic
 */
public class Utils {

    public static String byteArrayToHexString(byte [] data){
        return null;
    }

    public static byte [] md5(String data) throws NoSuchAlgorithmException{
        MessageDigest digest = MessageDigest.getInstance("MD5");
        digest.update(data.getBytes());
        return digest.digest();
    }

}
