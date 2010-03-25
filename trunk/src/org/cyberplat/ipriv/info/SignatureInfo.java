/*
 * SignatureInfo.java
 *
 * Created on 1 Декабрь 2005 г., 11:26
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

package org.cyberplat.ipriv.info;

import java.io.*;

import org.bouncycastle.crypto.digests.MD5Digest;
import org.bouncycastle.bcpg.PublicKeyAlgorithmTags;

import org.cyberplat.util.*;
import org.cyberplat.ipriv.data.*;

import org.cyberplat.ipriv.algorithm.*;

/**
 *
 * @author  Shutov
 */
public class SignatureInfo {
    public static final int SIGN_BINARY_DOCUMENT = 0x00;
    public static final int SIGN_TEXT_DOCUMENT   = 0x01;
    public static final int SIGN_STANDALONE_SIGNATURE = 0x02;
    public static final int SIGN_GENERIC_CERTIFICATION = 0x10;
    public static final int SIGN_PERSONA_CERTIFICATION = 0x11;
    public static final int SIGN_CASUAL_CERTIFICATION = 0x12;
    public static final int SIGN_POSITIVE_CERTIFICATION = 0x13;
    public static final int SIGN_SUBKEY_BINDING = 0x18;
    public static final int SIGN_DIRECTLY_ON_KEY = 0x1F;
    public static final int SIGN_KEY_REVOCATION = 0x20;
    public static final int SIGN_SUBKEY_REVOCATION = 0x28;
    public static final int SIGN_CERTIFICATION_REVOCATION = 0x30;
    public static final int SIGN_TIMESTAMP = 0x40;
    
    public static final int BASIC_VERSION = 0x03;
    public static final int EXPANDABLE_VERSION = 0x04;
    
    public static final int SIZE_ADDITIONAL = 9;
    
    public int    version = 0;
    public int    type = 0;
    public int    length = 0;
    public long   time = System.currentTimeMillis() / 1000;
    public long   keyId = 0;
    
    public byte[] leftHashValue = new byte[] { 0, 0 };
    
    public byte[] hashValue = null;

    public int hashValueShift = -1;
    
    public int    publicKeyAlgorithm = PublicKeyAlgorithmTags.RSA_GENERAL;
    public int    hashAlgorithm = HashAlgorithm.MD5;
    public byte[] additional = null;
    
    public BigIntegerImage value = null;
    
    public static final byte[/*18*/] LEFT_HASH_CHECK_TAG = {
        48, 32, 48, 12, 6, 8, 42, -122, 72,
        -122, -9, 13,  2, 5, 5,  0,    4,  16
    };
    
    
    public SignatureInfo() {
    }
    
    public String getTypeInfo() {
        return getTypeInfo(type);
    }
    
    public static String getTypeInfo(int type) {
        switch (type) {
            case SIGN_BINARY_DOCUMENT: return "SIGN_BINARY_DOCUMENT";
            case SIGN_TEXT_DOCUMENT: return "SIGN_TEXT_DOCUMENT";
            case SIGN_STANDALONE_SIGNATURE: return "SIGN_STANDALONE_SIGNATURE";
            case SIGN_GENERIC_CERTIFICATION: return "SIGN_GENERIC_CERTIFICATION";
            case SIGN_PERSONA_CERTIFICATION: return "SIGN_PERSONA_CERTIFICATION";
            case SIGN_CASUAL_CERTIFICATION: return "SIGN_CASUAL_CERTIFICATION";
            case SIGN_POSITIVE_CERTIFICATION: return "SIGN_POSITIVE_CERTIFICATION";
            case SIGN_SUBKEY_BINDING: return "SIGN_SUBKEY_BINDING";
            case SIGN_DIRECTLY_ON_KEY: return "SIGN_DIRECTLY_ON_KEY";
            case SIGN_KEY_REVOCATION: return "SIGN_KEY_REVOCATION";
            case SIGN_SUBKEY_REVOCATION: return "SIGN_SUBKEY_REVOCATION";
            case SIGN_CERTIFICATION_REVOCATION: return "SIGN_CERTIFICATION_REVOCATION";
            case SIGN_TIMESTAMP: return "SIGN_TIMESTAMP";
            case BASIC_VERSION: return "BASIC_VERSION";
            case EXPANDABLE_VERSION: return "EXPANDABLE_VERSION";
        }
        return "UNKNOWN SIGNATURE TYPE";
    }
    
    private void initAdditional() {
        additional = new byte[SIZE_ADDITIONAL]; // length
        additional[0] = (byte)(type & 0x000000FF);
        lib.fillUnsigned4((int)(keyId & 0xFFFFFFFF), additional, 1);
        lib.fillUnsigned4((int)time, additional, 5);
    }
    
    private int initHashValue() {
        int shift = LEFT_HASH_CHECK_TAG.length;
        
        hashValue = new byte[shift + 16];
        System.arraycopy(LEFT_HASH_CHECK_TAG, 0, hashValue, 0, shift);
        
        for (int i = shift; i < hashValue.length; i++)
            hashValue[i] = 0;
        return shift;
    }
    
    public int getHashMD5(byte[] src, int start, int nsrc, byte[] keyUserId) {
        initAdditional();
        hashValueShift = initHashValue();
        MD5Digest md5 = new MD5Digest();
        
        md5.update(src, start, nsrc);
        if (keyUserId != null)
            md5.update(keyUserId, 0, keyUserId.length);
        md5.update(additional, 0, SIZE_ADDITIONAL);
        
        md5.doFinal(hashValue, hashValueShift);
        return getHashValue();
    }
    
    public void initLeftHashValue() {
        if (hashValueShift != -1) {
            leftHashValue[0] = hashValue[hashValueShift];
            leftHashValue[1] = hashValue[hashValueShift + 1];
        } else {
            leftHashValue[0] = 0;
            leftHashValue[1] = 0;
        }
    }
    
    public int getHashValue() {
        return (hashValue[hashValueShift] << 8) | (hashValue[hashValueShift + 1]);
    }
    
    public int getLeftHashValue() {
        return (leftHashValue[0] << 8) | (leftHashValue[1]);
    }
    
    public byte[] getLeftHashData() {
        return leftHashValue;
    }
    
    public byte[] getHashData() {
        return hashValue;
    }
    
    public void check(PublicKeyInfo keyInfo, String signer, byte[] src, int start, int nsrc)
    throws IOException {
        switch (type) {
            case SIGN_TEXT_DOCUMENT:
                if (length != SIZE_ADDITIONAL)
                    throw new IOException("501");//SignatureInfo: Unknown signature length.");
                if (getLeftHashValue() != getHashMD5(src, start, nsrc, null))
                    throw new IOException("502");//SignatureInfo: Bad signature.");
                break;
            case SIGN_GENERIC_CERTIFICATION:
                byte[] result =  keyInfo.getCipher(false).process(value.getValue(),value.startValue(),value.getEffectiveLength());
                if (result == null || result[0] != 1)
                    throw new IOException("503");//Bad-formed or corrupted signature certification.");
                
                int k;
                for (k = 1; k < result.length && result[k] == -1; k++);
                if (k == result.length || result[k] != 0)
                    throw new IOException("504");//Strange signature certification.");
                
                k++;
                for (int i = 0; i < LEFT_HASH_CHECK_TAG.length; i++)
                    if (result[k + i] != LEFT_HASH_CHECK_TAG[i])
                        throw new IOException("505");//Corrupted signature certification.");
                
                k += LEFT_HASH_CHECK_TAG.length;
                if (result[k] != leftHashValue[0] || result[k + 1] != leftHashValue[1])
                    throw new IOException("506");//Bad signature certification.");
                
                
                int lhash = getHashMD5(src, start, nsrc, ConvertText.toWin1251(signer));
                
                
                if (getHashValue() != getLeftHashValue())
                    throw new IOException("507");//Bad signature certification.");
                
                if (!lib.compareFromTheEnd(hashValue, result))
                    throw new IOException("508");//Bad signature certification.");
                break;
            default:
                throw new IOException("509");//SignatureInfo: Unknow signature type(" + type + ").");
        }
    }
}
