/*
 * PGPSignaturePacket.java
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

import java.util.Vector;
import java.util.Date;
import java.io.*;

import org.cyberplat.util.*;
import org.cyberplat.ipriv.info.*;
import org.cyberplat.ipriv.data.*;
/**
 *
 * @author  Shutov
 */
public class PGPSignaturePacket extends PGPPacket {
    public final static int SIZE_IPRIV_HEAD = 9;
    public final static byte DEFAULT_HEAD = (byte)0x89;
    private SignatureInfo info;
    private Radix64Serial calculatedSignature = null;
    
    /** Creates a new instance of PGPSignaturePacket */
    public PGPSignaturePacket(byte[] in, int startIndex) throws IOException {
        super(in, startIndex);
        info = new SignatureInfo();
        head = DEFAULT_HEAD;
    }
    /** Creates a new instance of PGPSignaturePacket */
    public PGPSignaturePacket()  throws IOException {
        super();
        info = new SignatureInfo();
        head = DEFAULT_HEAD;
    }
    /** Creates a new instance of PGPSignaturePacket */
    public PGPSignaturePacket(PGPPacket parent) throws IOException {
        super();
        info = new SignatureInfo();
        copyData(parent);
        parseBody();
    }
    
    /** Creates a new instance of PGPSignaturePacket */
    public PGPSignaturePacket(SignatureInfo si) throws IOException {
        super();
        info = si;
        head = DEFAULT_HEAD;
    }
    
    public void resetSignature() {
        calculatedSignature = null;
    }
    
    public Vector buildBodyVector() throws IOException {
        Vector v = new Vector();
        switch (info.type) {
            case SignatureInfo.SIGN_TEXT_DOCUMENT:
                v.addElement(new byte[]{SignatureInfo.BASIC_VERSION, SIZE_IPRIV_HEAD});
                v.addElement(info.additional);
                v.addElement(new byte[]{(byte)info.publicKeyAlgorithm, (byte)info.hashAlgorithm });
                v.addElement(info.leftHashValue);
                v.addElement(info.value.getSized());
                v.insertElementAt(lib.fillUnsigned2(lib.VectorLength(v), new byte[] {head, 0, 0}, 1), 0);
                break;
            case SignatureInfo.SIGN_GENERIC_CERTIFICATION:
                v.addElement(new byte[]{SignatureInfo.BASIC_VERSION, SIZE_IPRIV_HEAD});
                v.addElement(info.additional);
                v.addElement(new byte[]{(byte)info.publicKeyAlgorithm, (byte)info.hashAlgorithm });
                v.addElement(info.leftHashValue);
                v.addElement(info.value.getSized());
                v.insertElementAt(lib.fillUnsigned2(lib.VectorLength(v), new byte[] {head, 0, 0}, 1), 0);
                break;
            default:
                throw new IOException("901");//PGPSignaturePacket.buildBody: Unknown type of the SignatureInfo data.");
        }
        return v;
    }
    
    public Radix64Serial buildBody() throws IOException {
        return calculatedSignature = new Radix64Serial(true, lib.VectorToByteArray(buildBodyVector()), 0, -1);
    }
    
    public Radix64Serial getPacket() throws IOException {
        return (calculatedSignature == null)? (buildBody()):(calculatedSignature);
    }
    
    public Vector getOutVector() throws IOException {
        return getPacket().getOutVector();
    }
    
    void parseBody() throws IOException {
        int end = 0;
        info.version = lib.unsigned(getBody()[end++]);
        info.length = lib.unsigned(body[end++]);
        switch (info.length) {
            case 5: // PGP Standard
                info.type = lib.unsigned(body[end++]);
                info.time = (long)lib.unsigned4(body, end);
                end += 4;
                info.keyId = lib.unsigned8(body, end);
                end += 8;
                break;
            case SIZE_IPRIV_HEAD: // IPRIV
                info.type = lib.unsigned(body[end++]);
                info.keyId = lib.unsigned4(body, end);
                end += 4;
                info.time = (long)lib.unsigned4(body, end);
                end += 4;
                break;
            default:
                throw new IOException("902");//PGPSignaturePacket: Unexpected signature header length (" + info.length + ").");
        }
        if (info.publicKeyAlgorithm != lib.unsigned(body[end++]))
            throw new IOException("903");//PGPSignaturePacket: Unsupported public key.");
        
        if (info.hashAlgorithm != lib.unsigned(body[end++]))
            throw new IOException("904");//PGPSignaturePacket: Unsupported hash algorithm.");
        
        info.leftHashValue[0] = body[end++];
        info.leftHashValue[1] = body[end++];
        
        info.value = new BigIntegerImage(body, end, null);
        end += info.value.length();
    }
    
    public SignatureInfo getInfo() {
        return info;
    }
    
}
