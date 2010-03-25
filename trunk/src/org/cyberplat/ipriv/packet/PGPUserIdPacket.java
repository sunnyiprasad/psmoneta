/*
 * PGPUserIdPacket.java
 *
 * Created on 5 Декабрь 2005 г., 12:41
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

import org.cyberplat.util.*;
import org.cyberplat.ipriv.info.*;
import org.cyberplat.ipriv.data.*;

/**
 *
 * @author  Shutov
 */
public class PGPUserIdPacket extends PGPPacket {
    public static final byte STD_HEAD = (byte)(0x80 | (TYPE_USER_ID << 2));
    private UserIdInfo info;

    /** Creates a new instance of PGPUserIdPacket */
    public PGPUserIdPacket(byte[] in, int startIndex)  throws IOException {
        super(in, startIndex);
        info = new UserIdInfo();
    }
    /** Creates a new instance of PGPUserIdPacket */
    public PGPUserIdPacket()  throws IOException {
        super();
        head = STD_HEAD;
        info = new UserIdInfo();        
    }
    
    /** Creates a new instance of PGPUserIdPacket */
    public PGPUserIdPacket(byte[] userId)  throws IOException {
        super();
        head = STD_HEAD;
        info = new UserIdInfo(userId, 0, userId.length);
    }
    
    /** Creates a new instance of PGPUserIdPacket */
    public PGPUserIdPacket(String userId)  throws IOException {
        super();
        head = STD_HEAD;
        info = new UserIdInfo(userId);
    }
    
    /** Creates a new instance of PGPUserIdPacket */
    public PGPUserIdPacket(PGPPacket parent)  throws IOException {
        super();
        info = new UserIdInfo();
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
        v.addElement(new byte[] {head, (byte)info.data.length});
        v.addElement(info.data);
        return v;
    }
    
    void parseBody() throws IOException {
        getBody();
        info = new UserIdInfo(body, 0, body.length);
    }
    
    public UserIdInfo getInfo()
    {
        return info;
    }
}
