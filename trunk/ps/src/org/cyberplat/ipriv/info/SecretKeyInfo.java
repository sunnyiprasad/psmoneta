/*
 * SecretKeyInfo.java
 *
 * Created on 1 Декабрь 2005 г., 16:24
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
import org.bouncycastle.crypto.params.KeyParameter;

import org.cyberplat.ipriv.data.*;
import org.cyberplat.util.*;
import org.cyberplat.ipriv.cipher.*;
import org.cyberplat.ipriv.algorithm.*;

/**
 *
 * @author  Shutov
 */
public class SecretKeyInfo extends PublicKeyInfo {
    public static final int SIZE_SECRET_ALGORITHM_PARAM = 8;
    public BigIntegerImage secretExponent = null;
    public int secretAlgorithm = SymmetricKeyAlgorithm.IDEA;
    public byte[] secretAlgorithmParam = null;
    public byte[] secretAlgorithmDump = null;
    public BigIntegerImage primeP = null;
    public BigIntegerImage primeQ = null;
    public BigIntegerImage inversePQ = null;
    public int checkSumma = 0;
    public byte[] password = null;
    public byte[] secretText = null;

    
    /** Creates a new instance of SecretKeyInfo */
    public SecretKeyInfo(String owner, byte[] password) {
        super(owner);
        this.password = password;
    }
    
    public IPrivRSACipher getSecretCipher() {
        return new IPrivRSACipher(true,
        modulus.getBigInteger(),
        secretExponent.getBigInteger(), true);
    }
    
    public IPrivIDEACipher getIDEACipher(boolean forEncryption) {
        return new IPrivIDEACipher(password, forEncryption);
    }
    
    public int calcCheckSumma() {
        int result = 0;
        result = secretExponent.checkSumma(result);
        result = primeP.checkSumma(result);
        result = primeQ.checkSumma(result);
        result = inversePQ.checkSumma(result);
        return result;
    }

    /*
    public void trace(String h) {
        // public part
        lib.message(h + ":version = " + version);
        lib.message(h + ":keyId = " + keyId);
        lib.message(h + ":time = " + time);
        lib.message(h + ":validity = " + validity);
        lib.message(h + ":algorithm = " + algorithm);
        BigIntegerImage.xtrace(h + ":exponent", exponent);
        BigIntegerImage.xtrace(h + ":modulus", modulus);
        // secret part
        lib.message(h + ":secretAlgorithm = " + secretAlgorithm);
        lib.messageA(h + ":secretAlgorithmParam = ", secretAlgorithmParam);
        lib.messageA(h + ":secretAlgorithmDump = ", secretAlgorithmDump);
        BigIntegerImage.xtrace(h + ":secretExponent", secretExponent);
        BigIntegerImage.xtrace(h + ":primeP", primeP);
        BigIntegerImage.xtrace(h + ":primeQ", primeQ);
        BigIntegerImage.xtrace(h + ":inversePQ", inversePQ);
        lib.message(h + ":checkSumma = " + checkSumma);
        lib.message(h + ":password = ", password);
    }
*/
    
    public PublicKeyInfo clonePublicKeyInfo() {
        PublicKeyInfo result = new PublicKeyInfo(owner);
        result.version = version;
        result.keyId = keyId;
        result.time = time;
        result.validity = validity;
        result.algorithm = algorithm;
        result.modulus = modulus.clone();
        result.exponent = exponent.clone();
        return result;
    }
    
    public byte[] getSecretText()
    {
        return secretText;
    }

    public byte[] setSecretText(byte[] secretText)
    {
        return (this.secretText = secretText);
    }
    
}
