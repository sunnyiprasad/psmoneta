/*
 * IPrivRSACipher.java
 *
 * Created on 16 Декабрь 2005 г., 12:59
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

package org.cyberplat.ipriv.cipher;

import org.bouncycastle.crypto.engines.RSAEngine;
import java.math.BigInteger;
import org.bouncycastle.crypto.params.RSAKeyParameters;

import org.cyberplat.util.*;

/**
 *
 * @author  Shutov
 */
public class IPrivRSACipher {
    /*
     * Cipher
     */
    //private PKCS1Encoding cipher = null;
    private RSAEngine cipher = null;
    public boolean forEncryption = false;
    public boolean isPrivate = false;
    /*
     * Creates a new instance of IPrivCipher
     */
    public IPrivRSACipher() {
    }
    /*
     * Creates a new instance of IPrivCipher
     */
    public IPrivRSACipher(boolean isPrivate, BigInteger modulus, BigInteger exponent, boolean forEncryption) {
        this.forEncryption = forEncryption;
        this.isPrivate = isPrivate;
        init(isPrivate, modulus, exponent, forEncryption);
    }
    /*
     * Initialize cipher *
     */
    private void init(boolean isPrivate, BigInteger modulus, BigInteger exponent, boolean forEncryption) {
        RSAKeyParameters key = new RSAKeyParameters(isPrivate, modulus, exponent);
        cipher = new RSAEngine();
        cipher.init(forEncryption, key);
    }
    /*
     * Decript byte array
     */
    public byte[] process(byte[] in, int in_start, int in_length) {
        try {
            for (int j = in.length - 1; j > 0; j--)
                if (in[j] != 0) {
                    j++;
                    break;
                }
            
            java.util.Vector v = new java.util.Vector();
            int len = 0;
            int i = in_start;
            int in_end = in_start + in_length;
            int isize = cipher.getInputBlockSize();
            for (; i < in_end; i += isize) {
                int bsize = in_end - i;
                if (bsize > 0)
                    bsize = isize;
                v.addElement(cipher.processBlock(in, i, bsize));
                len += ((byte[])v.lastElement()).length;
            }
            byte[] out = new byte[len];
            i = 0;
            byte[] b;
            int d;
            for (java.util.Enumeration e = v.elements(); e.hasMoreElements(); i += b.length) {
                b = (byte[])e.nextElement();
                System.arraycopy(b, 0, out, i, b.length);
            }
            return out;
        }
        catch (Exception ce) {
            //RST: lib.message("IPrivCipher: " + ce.getMessage());
        }
        return null;
    }
}
