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

import java.io.*;
import java.util.Vector;
import org.cyberplat.ipriv.info.*;
import org.cyberplat.ipriv.data.*;


/**
 *
 * @author  Shutov
 */
public class PGPTrustPacket extends PGPPacket {
    public final static byte DEFAULT_HEAD = (byte)(0x80 | (TYPE_TRUST << 2));
    private TrustInfo info = new TrustInfo ();

    /** Creates a new instance of PGPPublicKeyPacket */
    public PGPTrustPacket(byte[] in, int startIndex)  throws IOException {
        super(in, startIndex);
        info.data = in[startIndex + 2];
    }
    /** Creates a new instance of PGPPublicKeyPacket */
    public PGPTrustPacket()  throws IOException {
        super();
    }
    /** Creates a new instance of PGPPublicKeyPacket */
    public PGPTrustPacket(int level)  throws IOException {
        super();
        head = DEFAULT_HEAD;
        info.data = (byte)(level & 0xFF);
    }
    /** Creates a new instance of PGPSignaturePacket */
    public PGPTrustPacket(PGPPacket parent)  throws IOException {
        super();
        copyData(parent);
        parseBody();
    }
    
    void parseBody() throws IOException {
        getBody();
        int end = 0;
        info.data = body[end++];
    }
    public TrustInfo getInfo()
    {
        return info;
    }

    public Vector getOutVector() throws IOException {
        return getPacket().getOutVector();
    }
    
    public Radix64Serial getPacket() throws IOException {
        return buildBody();
    }
    
    public Radix64Serial buildBody() throws IOException {
        return new Radix64Serial(true, new byte[] {head, 1, info.data}, 0, 3);
    }
    
    public Vector buildBodyVector() throws IOException {
        Vector v = new Vector();
        v.addElement(new byte[] {head, 1, info.data});
        return v;
    }
}
