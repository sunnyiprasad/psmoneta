/*
 * IPrivCipher.java
 *
 * Created on 12 Декабрь 2005 г., 15:24
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

import org.bouncycastle.crypto.digests.MD5Digest;
import org.bouncycastle.crypto.engines.IDEAEngine;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.modes.PGPCFBBlockCipher;

import org.cyberplat.util.*;

/**
 *
 * @author  Shutov
 */
public class IPrivIDEACipher {
    /*
     * Cipher
     */
    private PGPCFBBlockCipher cipher = null;
    private boolean forEncryption;
    /*
     * Creates a new instance of IPrivCipher
     */
    public IPrivIDEACipher() {
    }

    /*
     * Returns digest of password 
    */
    public static byte[] getDigest(byte[] password)
    {
        MD5Digest md5 = new MD5Digest();
        md5.update(password, 0, password.length);
        byte [] param = new byte[md5.getDigestSize()];
        md5.doFinal(param, 0);
        return param;
    }
    
    /*
     * Creates a new instance of IPrivCipher
     */
    public IPrivIDEACipher(byte[] password, boolean forEncryption) {
        init(password, forEncryption);
    }
    /*
     * Initialize cipher *
     */
    private void init(byte[] password, boolean forEncryption) {
        this.forEncryption = forEncryption;
        cipher = new PGPCFBBlockCipher(new IDEAEngine(), false);
        cipher.init(forEncryption, new KeyParameter(password));
    }
    /*
     * Decript byte array
     */
    public byte[] process(byte[] in) {
        try {
            byte[] out = new byte[in.length];
            for (int i = 0; i < out.length; i += cipher.processBlock(in, i, out, i));
            return out;
        }
        catch (Exception ce) {
            //RST: lib.message("IPrivCipher.decrypt: " + ce.getMessage());
        }
        return null;
    }
    /*
     * Desrypd byte array skipping first zero elements
     */
    public byte[] processZeroStartedArray(byte[] in) {
        byte[] target = new byte[in.length];
        int start = 0;
        for (;start < in.length && in[start] == 0; start++)
            target[start] = 0;
        try {
            while (start < target.length)
                start += cipher.processBlock(in, start, target, start);
        }
        catch (Exception ce) {
            //RST: lib.message("IPrivCipher.decrypt: " + ce.getMessage());
            return null;
        }
        return target;
    }
    public boolean forEncryption()
    {
        return forEncryption;
    }
}
