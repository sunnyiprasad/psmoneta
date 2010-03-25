/*
 * lib.java
 *
 * Created on 28 Ноябрь 2005 г., 14:38
 */

package org.cyberplat.util;

import java.io.*;
import java.math.*;
import java.util.Enumeration;
import java.util.Vector;

/**
 *
 * @author  Shutov
 */
public class lib {
    public static boolean SHOW_MESSAGES = true;
    
    public static int SIZE_NUMERIC = 8;
    
    public static String TAG_CRLF = "\r\n";
    public static String TAG_LF = "\n";
    public static String TAG_EQV = "=";
    
    /** Creates a new instance of lib */
    public lib() {
    }
    /**
     * Print a message string to stdout
     */
    
    public static void message(String mesg) {
        if (SHOW_MESSAGES)
            System.out.println(mesg);
    }
    
    public static void showMessages(boolean onOff) {
        SHOW_MESSAGES = onOff;
    }
    
    
    /**
     * Read numeric field
     * Читает 8 байт и вовзвращает их как целочисленное значение
     */
    public static int readNumeric(DataInputStream in) throws IOException {
        try {
            String s = "";
            for (int i = 0; i < SIZE_NUMERIC; i++) {
                s += (char)(in.readByte());
            }
            return Integer.parseInt(s, 10);
        } catch (Exception e) {
            throw new IOException("+readNumeric + " + e.getClass().getName() + ": " + e.getMessage());
        }
    }
    /**
     * Read numeric field with skipping first space characters
     * Читает числовое поле пропуская первые символы
     */
    public static int ReadNumericAtStart(DataInputStream in) throws IOException {
        try {
            byte b = ' ';
            while (" \r\n\t".indexOf((char)b) != -1) {
                b = in.readByte();
            }
            String s = "" + (char)b;
            for (int i = 0; i < SIZE_NUMERIC - 1; i++) {
                //s += (char)(in.readByte());
                s += (char)(in.readByte());
            }
            return Integer.parseInt(s, 10);
        } catch (Exception e) {
            throw new IOException("+readNumeric + " + e.getClass().getName() + ": " + e.getMessage());
        }
    }
    /**
     * Read till the end of line
     */
    public static String readLn(DataInputStream in) throws IOException {
        try {
            String b = "";
            for (int i = 0; i < TAG_CRLF.length(); i++) {
                //b += (char)in.read();
                b += (char)(in.readByte());
                if (b.equals(TAG_LF))
                    break;
            }
            if (!b.equals(TAG_CRLF) && !b.equals(TAG_LF))
                throw new IOException("Bad end of line. Found:'" + b + "'");
            return b;
        } catch (Exception e) {
            throw new IOException(".readLn: " + e.getClass().getName() + ": " + e.getMessage());
        }
    }
    /**
     * Read '=' char(s)
     */
    public static String readEqv(DataInputStream in) throws IOException {
        try {
            String b = "";
            for (int i = 0; i < TAG_EQV.length(); i++)
                //b += (char)in.read();
                b += (char)(in.readByte());
            if (!b.equals(TAG_EQV))
                throw new IOException("Bad '" + TAG_EQV + "'. Found:'" + b + "'");
            return b;
        } catch (Exception e) {
            throw new IOException(".readEqv: " + e.getClass().getName() + ": " + e.getMessage());
        }
    }
    /**
     * Read a string
     */
    public static String readString(DataInputStream in, int size) throws IOException {
        try {
            String b = "";
            for (int i = 0; i < size; i++)
                //b += (char)in.read();
                b += (char)(in.readByte());
            
            return b;
        } catch (Exception e) {
            throw new IOException("+readString + " + e.getClass().getName() + ": " + e.getMessage());
        }
    }
    /**
     * Read a byte array
     */
    public static byte[] readByteArray(DataInputStream in, int size) throws IOException {
        try {
            byte[] b = new byte[size];
            in.readFully(b);
            //if (in.read(b, 0, size) != size)
            //    throw new IOException("Unexpected end.");
            return b;
        } catch (Exception e) {
            throw new IOException("+readByteArray + " + e.getClass().getName() + ": " + e.getMessage());
        }
    }
    /**
     * Print a message to stdout.
     * Message is forming as a concantenation of title amd mesg
     */
    public static void message(String title, byte[] mesg) {
        String s = title;
        if (mesg != null)
            for (int i = 0; i < mesg.length; i++)
                s += (char) mesg[i];
        else
            s += "(null)";
        message(s);
    }
    /**
     * Print a message to stdout.
     * Message array is printing by number is forming as a concantenation of title amd mesg
     */
    public static void messageA(String title, byte[] mesg) {
        if (mesg == null) {
            message(title + "(null)");
            return;
        }
        String s = title + "[/*" + mesg.length + "*/]{";
        for (int i = 0; i < mesg.length; i++)
            if (i == 0)
                s += mesg[i];
            else
                s += ", " + mesg[i];
        message(s + "}");
    }
    
    public static boolean isHex(byte b) {
        int c = 0xFF & b;
        return ((c < 0x30 || c > 0x66)?(false):(
        (c <= 0x39) ||
        (c >= 0x41 && c <= 0x46) ||
        (c >= 0x61)));
    }
    
    
    /**
     * Returns unsigned byte value
     */
    //
    public static int unsigned(byte b) {
        return (0x000000FF & b);
    }
    //
    public static int unsigned(byte[] b, int startIndex) {
        return (0x000000FF & (b[startIndex]));
    }
    //
    public static int unsigned2(byte[] b, int startIndex) {
        return ((0x000000FF & (b[startIndex    ])) <<  8) |
        ( 0x000000FF & (b[startIndex + 1]));
    }
    //
    public static int unsigned3(byte[] b, int startIndex) {
        return
        ((0x000000FF & (b[startIndex    ])) << 16) |
        ((0x000000FF & (b[startIndex + 1])) <<  8) |
        ( 0x000000FF & (b[startIndex + 2]));
    }
    //
    public static int unsigned4(byte[] b, int startIndex) {
        return ((0x000000FF & (b[startIndex + 0])) << 24) |
        ((0x000000FF & (b[startIndex + 1])) << 16) |
        ((0x000000FF & (b[startIndex + 2])) <<  8) |
        ( 0x000000FF & (b[startIndex + 3]));
    }
    //
    public static long unsigned8(byte[] b, int startIndex) {
        long u = unsigned4(b, startIndex);
        long l = unsigned4(b, startIndex + 4);
        return (u << 32) | l;
    }
    //
    public static byte[] fillUnsigned2(int src, byte[] b, int startIndex) {
        if (b == null) {
            b = new byte[2];
            startIndex = 0;
        }
        b[startIndex    ] = (byte)((src >> 8) & 0x000000FF);
        b[startIndex + 1] = (byte)((src     ) & 0x000000FF);
        return b;
    }
    //
    public static byte[] fillUnsigned3(int src, byte[] b, int startIndex) {
        if (b == null) {
            b = new byte[3];
            startIndex = 0;
        }
        b[startIndex    ] = (byte)((src >> 16) & 0x000000FF);
        b[startIndex + 1] = (byte)((src >>  8) & 0x000000FF);
        b[startIndex + 2] = (byte)((src      ) & 0x000000FF);
        return b;
    }
    //
    public static byte[] fillUnsigned4(int src, byte[] b, int startIndex) {
        if (b == null) {
            b = new byte[4];
            startIndex = 0;
        }
        b[startIndex    ] = (byte)((src >> 24) & 0x000000FF);
        b[startIndex + 1] = (byte)((src >> 16) & 0x000000FF);
        b[startIndex + 2] = (byte)((src >>  8) & 0x000000FF);
        b[startIndex + 3] = (byte)((src      ) & 0x000000FF);
        return b;
    }
    //
    public static byte[] fillUnsigned8(long src, byte[] b, int startIndex) {
        if (b == null) {
            b = new byte[8];
            startIndex = 0;
        }
        b[startIndex    ] = (byte)((src >> 56) & 0x000000FF);
        b[startIndex + 1] = (byte)((src >> 48) & 0x000000FF);
        b[startIndex + 2] = (byte)((src >> 40) & 0x000000FF);
        b[startIndex + 3] = (byte)((src >> 32) & 0x000000FF);
        b[startIndex + 4] = (byte)((src >> 24) & 0x000000FF);
        b[startIndex + 5] = (byte)((src >> 16) & 0x000000FF);
        b[startIndex + 6] = (byte)((src >>  8) & 0x000000FF);
        b[startIndex + 7] = (byte)((src      ) & 0x000000FF);
        return b;
    }
    
    /**
     * Read a byte array
     */
    public static byte[] readByteArray(byte[] b, int startIndex, int size) {
        
        byte[] result = new byte[size];
        System.arraycopy(b, startIndex, result, 0, size);
        return result;
    }
    /**
     * Calcs effective bits in byte array
     */
    public static int bbits(byte b) {
        int i = 8;
        for (int m = 128; (i > 0) && ((b & m) == 0); i--, m >>= 1);
        return i;
    }
    
    /**
     * Calcs effective bits in byte array
     */
    public static int bits(byte[] b) {
        for (int i = 0; i < b.length; i++)
            if (b[i] != 0)
                return bbits(b[i]) + (b.length - i - 1) * 8;
        return 0;
    }
    //
    public static int bitsTobytes(int b) {
        return (b + 7) >> 3;
    }
    //
    public static int outSizeForEncodingBase64(int length, boolean blocked) {
        int result;
        length *= 8;
        int l = length % 6;
        int ll = length / 6;
        switch (length % 6) {
            case 2: result = length / 6 + 3;
            break;
            case 4: result = length / 6 + 2;
            break;
            default:
                result = length / 6;
        }
        if (blocked)
            result += 2 * ((result - 1)/ 64);
        return result;
    }
    //
    public static int outSizeForDecodingBase64(byte[] encoded, int minus)
    throws IOException {
        int spaces = 0;
        int length = encoded.length - minus;
        
        if (length < 1)
            return 0;
        
        for (int i = 0; i < length; i++)
            if (encoded[i] == ' ' || encoded[i] == '\t' || encoded[i] == '\r' || encoded[i] == '\n')
                spaces++;
        
        int t = length - spaces;
        if (t == 0)
            return 0;
        
        if (t % 4 > 0)
            throw new IOException("Bad Base64 text size.");
        
        if ((encoded[length - 1]) != 0x3D)
            return (t * 6)/ 8;
        
        if ((encoded[length - 2]) == 0x3D)
            return 6 * (t - 4) / 8 + 1;
        return 6 * (t - 4) / 8 + 2;
    }
    
    public static boolean isBase64(byte b)
    {
        return 
            (b >= 'A' && b <= 'Z') ||
            (b >= 'a' && b <= 'z') ||
            (b >= '0' && b <= '9') ||
            ( b == '+') ||
            ( b == '/') ||
            ( b == '=');
    }
    
    public static byte[] compact64(byte[] encoded) throws IOException {
        if (encoded == null)
            return null;
        int len = 0;
        for (int i = 0; i < encoded.length; i++)
            if (isBase64(encoded[i]))
                len++;
        if (len == encoded.length)
            return encoded;
        byte[] result = new byte[len];
        len = 0;
        for (int i = 0; i < encoded.length; i++)
        {
            byte b = encoded[i];
            if (isBase64(b))
                result[len++] = b;
        }
        return result;
    }
    
    //
    public static byte[] format(int val, int length, byte leadChar) {
        return lib.format(ConvertText.toWin1251(Integer.toString(val, 10)), length, leadChar);
    }
    //
    public static byte[] format(String val, int length, byte leadChar) {
        return lib.format(ConvertText.toWin1251(val), length, leadChar);
    }
    //
    public static byte[] format(String val) {
        return ConvertText.toWin1251(val);
    }
    //
    public static byte[] format(byte[] sval, int length, byte leadChar) {
        if (sval.length == length)
            return sval;
        byte[] result = new byte[length];
        int delta = length - sval.length;
        for (int i = 0; i < delta; i++)
            result[i] = leadChar;
        System.arraycopy(sval, 0, result, delta, sval.length);
        return result;
    }
    //
    public static byte[] formatLeft(int val, int length, byte tailChar) {
        return lib.formatLeft(ConvertText.toWin1251(Integer.toString(val, 10)), length, tailChar);
    }
    //
    public static byte[] formatLeft(String val, int length, byte tailChar) {
        return lib.formatLeft(ConvertText.toWin1251(val), length, tailChar);
    }
    //
    public static byte[] formatLeft(byte[] sval, int length, byte tailChar) {
        if (sval.length == length)
            return sval;
        byte[] result = new byte[length];
        System.arraycopy(sval, 0, result, 0, sval.length);
        for (int i = sval.length; i < length; i++)
            result[i] = tailChar;
        return result;
    }
    //
    public static String VectorToString(Vector v) {
        String result = "";
        for (Enumeration e = v.elements(); e.hasMoreElements();) {
            Object o = e.nextElement();
            if (o == null)
                continue;
            if (o instanceof Vector)
                result += VectorToString((Vector) o);
            else
                result += ConvertText.toUnicode((byte[])o);
        }
        return result;
    }
    //
    public static int VectorToStream(Vector v, DataOutputStream out)   {
        int result = 0;
        for (Enumeration e = v.elements(); e.hasMoreElements();) {
            Object o = e.nextElement();
            if (o == null)
                continue;
            if (o instanceof Vector) {
                int n = VectorToStream((Vector) o, out);
                if (n == -1)
                    return n;
                result += n;
                continue;
            }
            if (o instanceof byte[]) {
                try {
                    byte[] b = (byte[])o;
                    result += b.length;
                    out.write(b);
                } catch (java.io.IOException ex) {
                    return -1;
                }
                continue;
            }
            return -1;
        }
        return result;
    }
    //
    public static int VectorLength(Vector v)   {
        int result = 0;
        for (Enumeration e = v.elements(); e.hasMoreElements();) {
            Object o = e.nextElement();
            if (o == null)
                continue;
            if (o instanceof Vector) {
                int n = VectorLength((Vector) o);
                if (n == -1)
                    return n;
                result += n;
                continue;
            } else if (o instanceof byte[]) {
                result += ((byte[])o).length;
            } else
                return -1;
        }
        return result;
    }
    //
    public static byte[] VectorToByteArray(Vector v) {
        int length = VectorLength(v);
        if (length == -1)
            return null;
        byte[] result = new byte[length];
        return (VectorToByteArray(v, result, 0) == -1)?(null):(result);
    }
    //
    public static int VectorToByteArray(Vector v, byte[] target, int start) {
        int result = start;
        for (Enumeration e = v.elements(); e.hasMoreElements();) {
            Object o = e.nextElement();
            if (o == null)
                continue;
            if (o instanceof Vector) {
                int n = VectorToByteArray((Vector) o, target, result);
                if (n == -1)
                    return n;
                result += n;
                continue;
            } else if (o instanceof byte[]) {
                byte[] b = (byte[])o;
                System.arraycopy(b, 0, target, result, b.length);
                result += b.length;
            } else
                return -1;
        }
        return result - start;
    }
    //
    public static boolean compareFromTheEnd(byte[] a, byte[] b) {
        int i = a.length - 1;
        int j = b.length - 1;
        if (i == -1)
            return (j == -1);
        if (j == -1)
            return (i == -1);
        while (i != -1 && j != -1) {
            if (a[i--] != b[j--])
                return false;
        }
        return true;
    }
    //
    public static byte[] concat(byte[] a, byte[] b) {
        byte[] result = new byte[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }
    //
    public static byte[] concat(byte[] a, byte[] b, byte[] c) {
        byte[] result = new byte[a.length + b.length + c.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        System.arraycopy(c, 0, result, a.length + b.length, c.length);
        return result;
    }
    //
    public static byte[] concat(byte[] a, int a_start, int a_length, byte[] b, int b_start, int b_length) {
        byte[] result = new byte[a_length + b_length];
        System.arraycopy(a, a_start, result, 0, a_length);
        System.arraycopy(b, b_start, result, a_length, b_length);
        return result;
    }
    //
    public static byte[] subarray(byte[] a, int start, int length) {
        byte[] result = new byte[length];
        System.arraycopy(a, start, result, 0, length);
        return result;
    }
    //
    public static String makeKeyNumber(String keyNumber) {
        String result = "00000000";
        if (keyNumber == null)
            return result;
        if (keyNumber.length() > 7)
            return keyNumber;
        result += keyNumber;
        return result.substring(result.length() - 8);
    }
    //
    public static String makeKeyNumber(long keyNumber) {
        return lib.makeKeyNumber(Long.toString(keyNumber));
    }
    
    public static String ToString(ByteArrayOutputStream ba) {
        /*
        StringBuffer sb = new StringBuffer();
        byte[] bb = ba.toByteArray();
        for (int i = 0; i < bb.length; i++)
        {
            sb.append((char)bb[i]);
        }
        return sb.toString();
        return org.cyberplat.util.ConvertText.toUnicode(ba.toByteArray());
         */
        return org.cyberplat.util.ConvertText.toUnicode(ba.toByteArray());
    }
}


