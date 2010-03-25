/*
 * PGPSecretKeyPacket.java
 *
 * Created on 29 Ноябрь 2005 г., 16:34
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


package org.cyberplat.ipriv.packet;

import java.util.Date;
import java.util.Vector;
import java.io.*;

import org.cyberplat.util.*;
import org.cyberplat.ipriv.info.*;
import org.cyberplat.ipriv.data.*;
import org.cyberplat.ipriv.algorithm.*;
import org.cyberplat.ipriv.cipher.*;
/**
 *
 * @author  Shutov
 */
public class PGPSecretKeyPacket extends PGPPacket {
    public static final byte STD_HEAD = (byte)(0x81 | (TYPE_SECRET_KEY << 2));
    /**
     * Secret key data
     */
    private SecretKeyInfo info = null;
    /**
     * Creates a new instance of PGPSecretKeyPacket
     */
    public PGPSecretKeyPacket(byte[] in, int startIndex, String owner, byte[] password)  throws IOException {
        super(in, startIndex);
        info = new SecretKeyInfo(owner, password);
    }
    /**
     * Creates a new instance of PGPSecretKeyPacket
     */
    public PGPSecretKeyPacket(String owner, byte[] password)  throws IOException {
        super();
        head = STD_HEAD;
        info = new SecretKeyInfo(owner, password);
    }
    /**
     * Creates a new instance of PGPSecretKeyPacket
     */
    public PGPSecretKeyPacket(SecretKeyInfo info)  throws IOException {
        super();
        head = STD_HEAD;
        this.info = info;
    }
    /**
     * Creates a new instance of PGPSecretKeyPacket
     */
    public PGPSecretKeyPacket(PGPPacket parent, String owner, byte[] password)  throws IOException {
        super();
        copyData(parent);
        info = new SecretKeyInfo(owner, password);
        parseBody();
    }
    
    public Vector getOutVector() throws IOException {
        return getPacket().getOutVector();
    }
    
    public Radix64Serial getPacket() throws IOException {
        return buildBody();
    }
    
    public Radix64Serial buildBody() throws IOException {
        return new Radix64Serial(true, lib.VectorToByteArray(buildBodyVector()), 0, -1);
    }
    
    public Vector buildBodyVector() throws IOException {
        Vector v = new Vector();
        v.addElement(new byte[]{(byte)info.version});
        v.addElement(lib.fillUnsigned4((int)(info.keyId), new byte[4], 0));
        v.addElement(lib.fillUnsigned4((int)(info.time), new byte[4], 0));
        v.addElement(lib.fillUnsigned2(info.validity, new byte[2], 0));
        v.addElement(new byte[]{(byte)info.algorithm});
        v.addElement(info.modulus.getSized());
        v.addElement(info.exponent.getSized());
        v.addElement(new byte[]{(byte)info.secretAlgorithm});
        
        v.addElement(info.secretAlgorithmDump);
        
        v.addElement(info.secretExponent.getEncSized());
        v.addElement(info.primeP.getEncSized());
        v.addElement(info.primeQ.getEncSized());
        v.addElement(info.inversePQ.getEncSized());
        v.addElement(lib.fillUnsigned2(info.checkSumma, new byte[2], 0));
        
        v.insertElementAt(lib.fillUnsigned2(lib.VectorLength(v), new byte[] {head, 0, 0}, 1), 0);
        return v;
    }
    
    /**
     * Parse data
     */
    private void parseBody() throws IOException {
        getBody();
        int end = 0;
        
        info.version = lib.unsigned(body[end++]);
        if (info.version != PublicKeyInfo.NEW_VERSION)
            throw new IOException("801");//PGPPublic key Packet: bad public key version.");
        
        info.keyId = lib.unsigned4(body, end);
        end += 4;
        info.time = ((long)lib.unsigned4(body, end));
        end += 4;
        info.validity = lib.unsigned2(body, end);
        end += 2;
        info.algorithm = lib.unsigned(body, end++);
        
        
        
        if (info.algorithm != PublicKeyAlgorithm.RSA_GENERAL)
            throw new IOException("802");//PGPSecretKeyPacket: unexpected public key algorithm '"   + PublicKeyAlgorithm.getString(info.algorithm) + "'.");
        
        info.modulus = new BigIntegerImage(body, end, null);
        
        end += info.modulus.length();
        info.exponent = new BigIntegerImage(body, end, null);
        end += info.exponent.length();
        
        info.secretAlgorithm = lib.unsigned(body, end++);
        
        if (!SymmetricKeyAlgorithm.isValid(info.secretAlgorithm))
            throw new IOException("803");//SecretKeyPAcket: Unknown symmetric algoritm.");
        if (info.secretAlgorithm != SymmetricKeyAlgorithm.IDEA)
            throw new IOException("804");//SecretKeyPAcket: Unsupported symmetric algoritm " +  SymmetricKeyAlgorithm.getString(info.secretAlgorithm));
        
        info.secretAlgorithmDump = new byte[info.SIZE_SECRET_ALGORITHM_PARAM];
        System.arraycopy(body, end, info.secretAlgorithmDump, 0, info.secretAlgorithmDump.length);
        end += info.secretAlgorithmDump.length;
        
        IPrivIDEACipher cipher = info.getIDEACipher(false);
        info.secretAlgorithmParam = cipher.process(info.secretAlgorithmDump);
        
        info.secretExponent = new BigIntegerImage(body, end, cipher);
        end += info.secretExponent.length();
        
        info.primeP = new BigIntegerImage(body, end, cipher);
        end += info.primeP.length();
        
        info.primeQ = new BigIntegerImage(body, end, cipher);
        end += info.primeQ.length();
        
        info.inversePQ = new BigIntegerImage(body, end, cipher);
        end += info.inversePQ.length();
        
        info.checkSumma = lib.unsigned2(body, end);
        end += 2;
        
        //info.trace("PGPSecretKeyPacket.parseBody:");
        
        if (end != body.length)
            throw new IOException("805");//SecretKeyPacket: Bad body length.");
        if (info.checkSumma != info.calcCheckSumma()){
            System.out.println(" crc = "+ info.checkSumma);
            System.out.println(" crc = "+ info.calcCheckSumma());
            throw new IOException("806");//SecretKeyPacket: Bad check summa.");
        }
    }
    /**
     * Returns Secret key info
     */
    public SecretKeyInfo getInfo() {
        return info;
    }
}
