/*
 * BigIntegerImage.java
 *
 * Created on 2 Декабрь 2005 г., 14:50
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

import org.cyberplat.ipriv.cipher.*;
import org.cyberplat.util.*;

import java.io.IOException;
import java.math.BigInteger;

/**
 *
 * @author  Shutov
 */

/*
 * EXCEPTION 1: BigIntegerImage: startIndex is not supported in the contructor.
 */
public class BigIntegerImage {
    private static int SIZE_HEADER = 2;
    private static int SIZE_BIGINTEGER = 128;
    
    private byte[] value = null;
    private byte[] encValue = null;
    
    int effectiveLength = 0;
    int checkSumma = 0;
    
    int bitsize = 0;
    
    /**
     * Creates a new instance of BigIntegerImage
     */
    public BigIntegerImage() {
    }
    /**
     * Creates a new instance of BigIntegerImage
     */
    public BigIntegerImage(byte[] b, int startIndex, IPrivIDEACipher cipher) {
        init(true, b, startIndex, cipher);
    }
    /**
     * Creates a new instance of BigIntegerImage
     */
    public byte [] init(boolean withContentLength, byte[] b, int startIndex, IPrivIDEACipher cipher) {
        try {
            if (withContentLength)
                readSized(b, startIndex);
            else {
                if (startIndex != 0)
                    throw new IOException(Integer.toString(101)); // BigIntegerImage: startIndex is not supported in the contructor
                readFromArray(b);
            }
            if (cipher != null) {
                if (cipher.forEncryption()) {
                    encValue = cipher.processZeroStartedArray(value);
                }
                else {
                    encValue = value;
                    value = cipher.processZeroStartedArray(encValue);
                }
            }
        } catch (Exception e) {
            //RST: lib.message(e.getMessage());
        }
        return value;
    }
    
    /**
     * Creates a new instance of BigIntegerImage
     */
    public BigIntegerImage(BigInteger b) {
        init(false, b.toByteArray(), 0, null);
    }
    /**
     * Creates a new instance of BigIntegerImage
     */
    public BigIntegerImage(BigInteger b, IPrivIDEACipher cipher) {
        init(false, b.toByteArray(), 0, cipher);
    }
    /**
     * Creates a new instance of BigIntegerImage
     */
    public BigIntegerImage(byte[] b) {
        init(false, b, 0, null);
    }
    /*
     * Returns next byte index
     */
    private int readSized(byte[] b, int startIndex) throws IOException{
        try {
            bitsize = lib.unsigned2(b, startIndex);
            int size = lib.bitsTobytes(bitsize);
            if (size <= 0) {
                value = new byte[0];
                return startIndex + SIZE_HEADER;
            }
            if (size > SIZE_BIGINTEGER)
                new IOException(Integer.toString(102));//Big integer size is " + size + " bytes - greater then " +  SIZE_BIGINTEGER + "bytes.");
            effectiveLength = size;
            startIndex += SIZE_HEADER;
            int delta = SIZE_BIGINTEGER - size;
            value = new byte[SIZE_BIGINTEGER];
            for (int i = 0; i < delta; i++)
                value[i] = 0;
            System.arraycopy(b, startIndex, value, delta, size);
            return size + startIndex;
        } catch (Exception e) {
            throw new IOException(Integer.toString(103));//BigIntegerImage.read: " + e.getClass().getName() + ": " + e.getMessage());
        }
    }
    
    /*
     * Returns image representation with the biginteger value bit-size in the first two bytes
     */
    public byte[] getSized() throws IOException{
        try {
            byte[] result;
            result = new byte[effectiveLength + SIZE_HEADER];
            lib.fillUnsigned2(lib.bits(value), result, 0);
            System.arraycopy(value, startValue(), result, SIZE_HEADER, effectiveLength);
            return result;
        } catch (Exception e) {
            throw new IOException(Integer.toString(104));//BigIntegerImage.read: " + e.getClass().getName() + ": " + e.getMessage());
        }
    }
    
    public byte[] getSized(IPrivIDEACipher cipherToEncrypt) throws IOException{
        try {
            byte[] result;
            encValue = cipherToEncrypt.processZeroStartedArray(value);
            result = new byte[encValue.length + SIZE_HEADER];
            System.arraycopy(encValue, startValue(), result, SIZE_HEADER, effectiveLength);
            lib.fillUnsigned2(lib.bits(encValue), result, 0);
            return result;
        } catch (Exception e) {
            throw new IOException(Integer.toString(105));//BigIntegerImage.read: " + e.getClass().getName() + ": " + e.getMessage());
        }
    }
    
    public byte[] getEncSized() throws IOException{
        try {
            byte[] result = new byte[effectiveLength + SIZE_HEADER];
            System.arraycopy(encValue, startEncValue(), result, SIZE_HEADER, effectiveLength);
            lib.fillUnsigned2(lib.bits(value), result, 0);
            return result;
        } catch (Exception e) {
            throw new IOException(Integer.toString(106));//BigIntegerImage.read: " + e.getClass().getName() + ": " + e.getMessage());
        }
    }
    
    /*
     * Returns next byte index
     */
    private byte[] readFromArray(byte[] b) throws IOException {
        try {
            int bsize = b.length;
            if (bsize <= 0) {
                value = new byte[0];
                return value;
            }
            if (bsize > SIZE_BIGINTEGER)
                throw new IOException(Integer.toString(107));//Big integer size is " + bsize + " bytes - greater then " +  SIZE_BIGINTEGER + "bytes.");
            
            effectiveLength = lib.bitsTobytes(lib.bits(b));
            int bstart = bsize - effectiveLength;
            
            int delta = SIZE_BIGINTEGER - effectiveLength;
            
            value = new byte[SIZE_BIGINTEGER];
            
            for (int i = 0; i < delta; i++)
                value[i] = 0;
            
            System.arraycopy(b, bstart, value, delta, effectiveLength);
            return value;
        } catch (Exception e) {
            throw new IOException(Integer.toString(108));//BigIntegerImage.read: " + e.getClass().getName() + ": " + e.getMessage());
        }
    }
    
    public int startValue() {
        return value.length - effectiveLength;
    }
    
    public int startEncValue() {
        return encValue.length - effectiveLength;
    }
    
    public int getEffectiveLength() {
        return effectiveLength;
    }
    
    public int length() {
        return effectiveLength + SIZE_HEADER;
    }
    
    public int checkSumma(int prev) {
        int b = lib.bits(value);
        int result = (b & 0x000000FF) + ((b & 0x0000FF00) >> 8);
        int i = 0;
        for (; i < value.length && value[i] == 0; i++);
        for (; i < value.length; i++)
            result = rSumma(result, (0x000000FF & value[i]));
        result = rSumma(result, prev);
        return result;
    }
    // summa of two unsigned bytes as unsigned short
    public static int rSumma(int a, int b) {
        int result = a + b;
        return (((result & 0xFFFF0000) != 0)?((result >> 16) - 1):(result)) & 0x0000FFFF;
    }
    
    public BigInteger getBigInteger() {
        return new BigInteger(value);
    }
    
    public BigInteger getBigInteger(int n) {
        return new BigInteger(getValue(n));
    }
    
    public byte[] getValue(int n) {
        if (n > value.length) {
            int d = n - value.length;
            byte[] b = new byte[n];
            for (int i = 0; i < d; i++)
                b[i] = 0;
            System.arraycopy(value, 0, b , d, value.length);
            return b;
        }
        byte[] b = new byte[n];
        System.arraycopy(value, value.length - n, b, 0, n);
        return b;
    }
    
    public byte[] getValue() {
        return value;
    }
    
    public byte[] getEncValue() {
        return encValue;
    }

    public BigIntegerImage clone() {
        BigIntegerImage result = new BigIntegerImage();
        if (value != null)
        {
            result.value = new byte[value.length];
            System.arraycopy(value, 0, result.value, 0, value.length);
        }
        if (encValue != null)
        {
            result.encValue = new byte[encValue.length];
            System.arraycopy(encValue, 0, result.encValue, 0, encValue.length);
        }
        result.effectiveLength = effectiveLength;
        result.checkSumma = checkSumma;
        result.bitsize = bitsize;
        return result;
    }
    
}
