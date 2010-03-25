/*
 * PGPPublicKeyPacket.java
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

/**
 *
 * @author  Shutov
 */
public class PGPPublicKeyPacket extends PGPPacket {
    public static final byte STD_HEAD = (byte)(0x81 | (TYPE_PUBLIC_KEY << 2));
    private PublicKeyInfo info;
    /** Creates a new instance of PGPPublicKeyPacket */
    public PGPPublicKeyPacket(String owner, byte[] in, int startIndex)  throws IOException {
        super(in, startIndex);
        info = new PublicKeyInfo(owner);
    }
    /** Creates a new instance of PGPPublicKeyPacket */
    public PGPPublicKeyPacket(String owner)  throws IOException {
        super();
        info = new PublicKeyInfo(owner);
        head = STD_HEAD;
    }
    /** Creates a new instance of PGPPublicKeyPacket */
    public PGPPublicKeyPacket(PublicKeyInfo info)  throws IOException {
        super();
        this.info = info;
        head = STD_HEAD;
    }
    /** Creates a new instance of PGPSignaturePacket */
    public PGPPublicKeyPacket(PGPPacket parent, String owner)  throws IOException {
        super();
        info = new PublicKeyInfo(owner);
        copyData(parent);
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
        v.insertElementAt(lib.fillUnsigned2(lib.VectorLength(v), new byte[] {head, 0, 0}, 1), 0);
        return v;
    }
    
    void parseBody() throws IOException {
        getBody();
        int end = 0;
        
        info.version = lib.unsigned(body[end++]);
        if (//info.version != PublicKeyInfo.OLD_VERSION &&
        info.version != PublicKeyInfo.NEW_VERSION)
            throw new IOException("701");//PGPPublic key Packet: bad public key version.");
        
        info.keyId = lib.unsigned4(body, end);
        end += 4;
        info.time = (long)(lib.unsigned4(body, end));
        end += 4;
        info.validity = lib.unsigned2(body, end);
        end += 2;
        info.algorithm = lib.unsigned(body, end++);
        
        if (info.algorithm != PublicKeyAlgorithm.RSA_GENERAL)
            throw new IOException("702");//PGPPublicKeyPacket: unexpected public key algorithm '"            + PublicKeyAlgorithm.getString(info.algorithm) + "'.");
        
        info.modulus = new BigIntegerImage(body, end, null);
        end += info.modulus.length();
        info.exponent = new BigIntegerImage(body, end, null);
        end += info.exponent.length();
        if (end != body.length)
            throw new IOException("703");//PublicKeyPacket: Bad body length.");
        Date d = new Date(info.time * 1000);
     /*
      * $$$ ENDIF
      *
      */
    }
    public PublicKeyInfo getInfo() {
        return info;
    }
    
}
