/*
 * SymmetricKeyAlgorithm.java
 *
 * Created on 30 Íîÿáğü 2005 ã., 16:21
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

/**
 * Basic tags for symmetric key algorithms
 */
package org.cyberplat.ipriv.algorithm;
import org.bouncycastle.bcpg.SymmetricKeyAlgorithmTags;

/**
 *
 * @author  Shutov
 */
public final class SymmetricKeyAlgorithm implements SymmetricKeyAlgorithmTags {

    public static final boolean isValid(int id)
    {
        return (id >= 0 && id <= 9) | (id >= 100 && id <= 110);
    }
    
    public static final String getString(int id)
    {
        if (id >= 100 && id <= 110)
            return "EXPERIMENTAL";
        switch (id)
        {
            case NULL: return "NULL";
            case IDEA: return "IDEA";
            case TRIPLE_DES: return "TRIPLE-DES";
            case CAST5: return "CAST5";
            case BLOWFISH: return "BLOWFISH";
            case SAFER: return "SAFER-SKI128";
            case DES: return "DES/SK";
            case AES_128: return "AES-128";
            case AES_192: return "AES-192";
            case AES_256: return "AES-256";
            case TWOFISH: return "TWOFISH";
        }
        return "Unknown";
    }
}
