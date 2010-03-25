/*
 * ParsePOSTFormat.java
 *
 * Created on 6 Февраль 2006 г., 16:11
 */

package org.cyberplat.util;

import java.util.Hashtable;
import java.io.*;
/**
 *
 * @author  Shutov
 */
public class PostFormat {
    private Hashtable _t = null;
    private int _encodeBase64 = 0;
    static public final String SPLITTER = "&";
    static public final int RIGHT_BASE64 = 1;
    static public final int LEFT_BASE64 = 2;
    static public final int BOTH_BASE64 = 3;
    static public final URLEncoder urlencoder = new URLEncoder();
    
    public PostFormat() {
        _t = new Hashtable();
    }
    
    public PostFormat(int encodeBase64) {
        _encodeBase64 = encodeBase64;
        _t = new Hashtable();
    }
    
    public PostFormat(String message, int decodeBase64) throws IOException {
        _encodeBase64 = decodeBase64;
        _t = decode(message, decodeBase64);
    }
    
    public PostFormat(byte[] message, int decodeBase64) throws IOException {
        _encodeBase64 = decodeBase64;
        _t = decode(ConvertText.toUnicode(message), decodeBase64);
    }
    
    public PostFormat(String message) throws IOException {
        _t = decode(message);
    }
    
    public PostFormat(byte[] message) throws IOException {
        _t = decode(ConvertText.toUnicode(message));
    }
    
    public int setEncodeBase64(int encodeBase64) {
        return (_encodeBase64 = encodeBase64);
    }
    
    public int getEncodeBase64() {
        return _encodeBase64;
    }
    
    public void putParam(String name, String body) {
        _t.put(name, body);
    }
    
    public void putIntParam(String name, int i) {
        _t.put(name, Integer.toString(i, 10));
    }
    
    public void putLongParam(String name, long l) {
        _t.put(name, Long.toString(l, 10));
    }
    
    public String doFinal() throws IOException {
        return encode(_t, _encodeBase64);
    }
    
    public String getParam(String name) {
        return (String)_t.get(name);
    }
    
    public int getIntParam(String name, int def) {
        String s = getParam(name);
        if (s != null)
            try { return Integer.parseInt(s, 10); }
            catch (NumberFormatException e) {}
        return def;
    }
    
    public long getLongParam(String name, long def) {
        String s = getParam(name);
        if (s != null)
            try { return Long.parseLong(s, 10); }
            catch (NumberFormatException e) {}
        return def;
    }
    
    public static String paramEncode(String name, String body, int encodeBase64) throws IOException {
        if (name.equals(""))
            return urlcoder(base64(body, encodeBase64, RIGHT_BASE64, true), true);
        else
            return
            urlcoder(base64(name, encodeBase64, LEFT_BASE64, true), true) +
            "=" +
            urlcoder(base64(body, encodeBase64, RIGHT_BASE64, true), true);
    }
    
    public static String encode(Hashtable t, int encodeBase64) throws IOException {
        StringBuffer sb = new StringBuffer();
        for(java.util.Enumeration e = t.keys(); e.hasMoreElements();) {
            String key = (String)e.nextElement();
            if (sb.length() > 0)
                sb.append(SPLITTER);
            sb.append(paramEncode(key, (String)t.get(key), encodeBase64));
        }
        return sb.toString();
    }
    
    public PostFormat clone() {
        PostFormat clone = new PostFormat(getEncodeBase64());
        for(java.util.Enumeration e = _t.keys(); e.hasMoreElements();) {
            String key = (String)e.nextElement();
            clone._t.put(key, (String)_t.get(key));
        }
        return clone;
    }
    
    public static Hashtable decode(String message) throws IOException {
        return decode(message, 0);
    }
    
    public static Hashtable decode(String message, int decodeBase64) throws IOException {
        return decode(message, decodeBase64, SPLITTER);
    }
    
    public static Hashtable decode(byte[] message, int decodeBase64, String splitter) throws IOException {
        return decode(ConvertText.toUnicode(message), decodeBase64, splitter);
    }
    
    
    public static Hashtable decode(String message, int decodeBase64, String splitter) throws IOException {
        Hashtable t = new Hashtable();
        try {
            int start = 0;
            while(true) {
                int p = message.substring(start).indexOf(splitter);
                if (p == -1) {
                    split(t, message.substring(start), decodeBase64);
                    break;
                } else {
                    split(t, message.substring(start, start + p), decodeBase64);
                    start += p + splitter.length();
                }
            }
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
        return t;
    }
    
    public static String urlcoder(String in, boolean encode) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        if (encode)
            urlencoder.encode(in, out);
        else
            urlencoder.decode(in, out);
        return lib.ToString(out);
    }
    
    private static String base64(String s, int decodeBase64, int part, boolean encode) throws IOException {
        return (((part & decodeBase64) == 0)?(s):(base64(s, encode)));
    }
    
    public static String base64(String s, boolean encode) throws IOException {
        byte[] sb = s.getBytes();
        return base64(sb, encode);
    }
    

    public static ByteArrayOutputStream base64stream(byte [] data, boolean encode) throws IOException {
        org.bouncycastle.util.encoders.Base64Encoder base64 = new
        org.bouncycastle.util.encoders.Base64Encoder();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        if (encode)
            base64.encode(data, 0, data.length, out);
        else {
            byte[] clone_data = lib.compact64(data);
            base64.decode(clone_data, 0, clone_data.length, out);
        }
        return out;
    }
    
    public static String base64(byte [] data, boolean encode) throws IOException {
        return lib.ToString(base64stream(data, encode));
    }
    
    public static byte[] base64bytes(byte [] data, boolean encode) throws IOException {
        return base64stream(data, encode).toByteArray();
    }
    
    private static void split(Hashtable t, String in, int decodeBase64) throws IOException {
        try {
            int p = in.indexOf('=');
            if (p < 1)
                t.put("", base64(urlcoder(in, false), decodeBase64, RIGHT_BASE64, false));
            else
                if (p + 1 == in.length())
                    t.put(
                    base64(urlcoder(in.substring(0, p), false), decodeBase64, LEFT_BASE64, false),"");
                else
                    t.put(
                    base64(urlcoder(in.substring(0, p), false), decodeBase64, LEFT_BASE64, false),
                    base64(urlcoder(in.substring(p + 1), false), decodeBase64, RIGHT_BASE64, false));
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }
    
    public Hashtable getTable() {
        return _t;
    }
}
