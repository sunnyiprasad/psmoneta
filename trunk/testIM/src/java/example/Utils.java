/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package example;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 *
 * @author sulic
 */
public class Utils {

    public static String generateNumber() throws NoSuchAlgorithmException{
        return getMd5InHexString(new Date().toString());
    }

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

}
