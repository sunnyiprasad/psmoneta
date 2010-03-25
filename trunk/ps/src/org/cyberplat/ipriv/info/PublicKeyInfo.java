/*
 * PublicKeyInfo.java
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
import org.cyberplat.util.*;

import org.cyberplat.ipriv.data.*;
import org.cyberplat.ipriv.cipher.*;
/**
 *
 * @author  Shutov
 */
public class PublicKeyInfo {
    public final static int MAX_RSA_MODULUS_BITS = 512;
    public final static int MAX_RSA_MODULUS_LEN = ((MAX_RSA_MODULUS_BITS + 7) / 8);
    
    public final static int OLD_VERSION = 2;
    public final static int NEW_VERSION = 3;
    
    public int    version = NEW_VERSION;
    public long   keyId = 0;
    public long   time = System.currentTimeMillis() / 1000;
    public int    validity = 0;
    public int    algorithm = 0;
    
    public BigIntegerImage modulus = null;
    public BigIntegerImage exponent = null;
    
    public String owner = null;
    byte[] text = null;
    
    /** Creates a new instance of PublicKeyInfo */
    public PublicKeyInfo(String owner) {
        this.owner = owner;
    }
    
    public IPrivRSACipher getCipher(boolean forEncryption) {
        return new IPrivRSACipher(false,
        modulus.getBigInteger(),
        exponent.getBigInteger(), forEncryption);
    }
    
    public String getKeyNumber() {
        return lib.makeKeyNumber(keyId);
    }
    
    public byte[] getText() {
        return text;
    }
    
    public byte[] setText(byte[] text) {
        return setText(text, 0, text.length);
    }
    
    public byte[] setText(byte[] text, int start, int n) {
        while(text[start] ==  '\r' || text[start] ==  '\n' || text[start] ==  ' ' || text[start] ==  '\t') {
            start++;
            n--;
        }
        
        this.text = new byte[n];
        System.arraycopy(text, start, this.text, 0, n);
        return this.text;
    }
    
    /*
    public void trace(String h) {
        //RST: lib.message(h + ":version = " + version);
        //RST: lib.message(h + ":keyId = " + keyId);
        //RST: lib.message(h + ":time = " + time);
        //RST: lib.message(h + ":validity = " + validity);
        //RST: lib.message(h + ":algorithm = " + algorithm);
        //RST: BigIntegerImage.xtrace(h + ":exponent", exponent);
        //RST: BigIntegerImage.xtrace(h + ":modulus", modulus);
    }
     */
}
