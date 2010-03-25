/*
 * HashAlgorithm.java
 *
 * Created on 30 Íîÿáğü 2005 ã., 16:48
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

package org.cyberplat.ipriv.algorithm;
import org.bouncycastle.bcpg.HashAlgorithmTags;
/**
 *
 * @author  Shutov
 */
public final class HashAlgorithm implements HashAlgorithmTags {
    
    public static final int MD5 = 1; //
    public static final int SHA1 = 2; //
    public static final int RIPEMD160 = 3; //
    public static final int DOUBLE_WITH_SHA = 4; // reserved
    public static final int MD2 = 5; // "MD2"
    public static final int TIGER192 = 6; // "TIGER192"
    public static final int HAVAL_5_160 = 7; // "HAVAL-5-160"
    
    public static final boolean isValid(int id) {
        return (id >= 100 && id <= 110) | (id >= 1 && id <= 7);
    }
    
    public static final String getString(int id) {
        if (id >= 100 && id <= 110)
            return "EXPERIMENTAL";
        switch (id) {
            case MD5: return "MD5";
            case SHA1: return "SHA-1";
            case RIPEMD160: return "RIPE-MD160";
            case DOUBLE_SHA: return "DOUBLE_SHA";
            case MD2: return "MD2";
            case TIGER_192: return "TIGER-192";
            case HAVAL_5_160: return "HAVAL-5-160";
            case SHA256: return "SHA-256";
            case SHA384: return "SHA-384";
            case SHA512: return "SHA-512";
        }
        return "Unknown";
    }
}
