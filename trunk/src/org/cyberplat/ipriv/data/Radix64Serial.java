/*
 * Radix64.java
 *
 * Created on 28 Ноябрь 2005 г., 14:32
 */

/**
 * CyberPlat.Com License
 *
 * Copyright (c) 2005-2006 CyberPlat.Com (http://www.cyberplat.com/)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software 
 * and associated documentation files (the "Software"), to deal in the Software without restriction, 
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, 
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package org.cyberplat.ipriv.data;

import java.io.*;
import org.bouncycastle.util.encoders.*;

import org.cyberplat.util.*;

/**
 *
 * @author  Shutov
 */
public class Radix64Serial {
    public static int CRC24_INIT = 0xB704CE;
    public static int CRC24_POLY = 0x1864CFB;
    
    public static int SIZE_CRC = 4;
    public byte[] data = null;
    public int dataLength = -1;
    public byte[] encoded = null;
    
    private boolean plainData = false;
    
    public int crc = 0;
    
    public int bytes2int(byte [] ba) {
        return (
        ((0xFF & (int)ba[0]) << 16) |
        ((0xFF & (int)ba[1]) <<  8) |
        ((0xFF & (int)ba[2])      )
        );
    }
    
    public static int makeCrc32(int result, byte b) {
        result ^= b << 16;
        for (int i = 0; i < 8; i++) {
            result <<= 1;
            if (0 != (result & 0x1000000))
                result ^= CRC24_POLY;
        }
        return result;
    }
    
    public static int makeCrc24(byte [] src, int nsrc) {
        int result = CRC24_INIT;
        for (int j = 0; j < nsrc; j++)
            result = makeCrc32(result, src[j]);
        return result & 0x00FFFFFF;
    }
    
    /** Creates a new instance of Radix64 */
    public Radix64Serial() {
    }
    /** Creates a new instance of Radix64 from plain array and encode it
     *  length can be -1
     */
    public Radix64Serial(boolean encodeIt, byte[] b, int start, int length) throws IOException {
        if (length == -1)
            length = b.length;
        if (encodeIt)
            encode(b, start, length);
        else
            decode(b, start, length);
    }
    /** Creates a new instance of Radix64 from plain array and encode it
     *  length can be -1
     */
    public Radix64Serial(byte[] b) throws IOException {
        encode(b, 0, b.length);
    }
    /** Creates a new instance of Radix64 from DataInputStream */
    public Radix64Serial(DataInputStream in, int length) throws IOException {
        if (length !=0)
            parse(in, length);
    }
    
    /** Parse DataInputStream */
    public byte[] parse(DataInputStream in, int length) throws IOException {
        int len = length - lib.TAG_CRLF.length() - lib.TAG_EQV.length()- SIZE_CRC;
        if (len < 0)
            throw new IOException(Integer.toString(301));//Radix64Serial: Negative length.");
        if (in.available() < length)
            throw new IOException(Integer.toString(302));//Radix64Serial: Enough data is not available.");
        
        encoded = lib.readByteArray(in, length);
        Radix64 radix64 = new Radix64();
        
        int tail_length = lib.TAG_CRLF.length() + lib.TAG_EQV.length() + SIZE_CRC;
        data = new byte[lib.outSizeForDecodingBase64(encoded, tail_length)];
        dataLength = radix64.decode(encoded, len,  data);
        if (dataLength == -1)
            throw new IOException(Integer.toString(303));//Radix64Serial.parse: Error during radix processing.");
        
        crc = radix64.crc.getValue24();
        return data;
    }
    
    /** Parse DataInputStream */
    public byte[] decode(byte[] b, int start, int length) throws IOException {
        if (length == -1)
            length = b.length - start;
        int len = length - lib.TAG_CRLF.length() - lib.TAG_EQV.length()- SIZE_CRC;
        if (len < 0)
            throw new IOException(Integer.toString(304));//Radix64Serial: Negative length.");
        encoded = new byte[length];
        System.arraycopy(b, start, encoded, 0, length);
        
        Radix64 radix64 = new Radix64();
        data = new byte[lib.outSizeForDecodingBase64(encoded, lib.TAG_CRLF.length() + lib.TAG_EQV.length()+ SIZE_CRC)];
        for (int i = data.length - 1; i > 0 && i > data.length - 8; i--)
            data[i] = 0;
        
        dataLength = radix64.decode(encoded, len, data);
        if (dataLength == -1)
            throw new IOException(Integer.toString(305));//Radix64Serial.decode: Error during radix processing.");
        
        crc = radix64.crc.getValue24();
        return data;
    }
    
    /** encode in byte array b to radix bytearray */
    public byte[] encode(byte[] b, int start, int length) throws IOException {
        if (length == -1)
            length = b.length - start;
        data = new byte[length];
        if (length != 0)
            System.arraycopy(b, start, data, 0, length);
        Radix64 radix64 = new Radix64();
        
        encoded = new byte[lib.outSizeForEncodingBase64(length, true) +
        lib.TAG_CRLF.length() + lib.TAG_EQV.length() + SIZE_CRC];
        dataLength = radix64.encode(data, encoded);
        if (dataLength < 0)
            throw new IOException(Integer.toString(304));//Radix64Serial.encode: Error during radix processing.");
        
        crc = radix64.crc.getValue24();
        /*
        byte[] stamp = byte[4];
        lib.fillUnsigned4(crc, stamp, 0);
        stamp = Base64.encode(stamp);
        System.arraycopy(stamp, 0, encoded, dataLength, stamp.length);
         */
        return encoded;
    }
    
    public java.util.Vector getOutVector() {
        java.util.Vector result = new java.util.Vector();
        if (plainData)
            result.addElement(data);
        else {
            if (encoded == null && data != null)
                try {
                    encode(data, 0, data.length);
                } catch (Exception ex) {
                    return result;
                }
            result.addElement(encoded);
        }
        return result;
    }
    
    public boolean setPlain(boolean onoff) {
        return plainData = onoff;
    }
    
    public boolean isPlain() {
        return plainData;
    }
    
    public byte[] setPlainData(byte[] b) {
        data = b;
        encoded = b;
        plainData = true;
        return b;
    }
    
    public byte[] setPlainData(byte[] ba, int start, int length) {
        data = new byte[length];
        System.arraycopy(ba, start, data, 0, length);
        plainData = true;
        return (encoded = data);
    }

    public byte[] getPlainData()
    {
        return data;
    }
}