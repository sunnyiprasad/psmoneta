/*
 * PGPSerial.java
 *
 * Created on 29 Ноябрь 2005 г., 11:07
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
import java.util.Enumeration;
import java.io.*;

import org.bouncycastle.util.encoders.*;
import org.cyberplat.util.*;

/**
 *
 * @author  Shutov
 */
public class PGPPacket {
    public static final int TYPE_RESERVED = 0;
    public static final int TYPE_PUBLIC_KEY_ENCRYPTED_SESSION_KEY = 1;
    public static final int TYPE_SIGNATURE = 2;
    public static final int TYPE_SYMMETRIC_KEY_ENCRYPTED_SESSION_KEY = 3;
    public static final int TYPE_ONE_PASS_SIGNATURE = 4;
    public static final int TYPE_SECRET_KEY = 5;
    public static final int TYPE_PUBLIC_KEY = 6;
    public static final int TYPE_SECRET_SUBKEY = 7;
    public static final int TYPE_COMPRESSED_DATA = 8;
    public static final int TYPE_SYMMETRICALLY_ENCRYPTED_DATA = 9;
    public static final int TYPE_MARKER = 10;
    public static final int TYPE_LITERAL_DATA = 11;
    public static final int TYPE_TRUST = 12;
    public static final int TYPE_USER_ID = 13;
    public static final int TYPE_PUBLIC_SUBKEY = 14;
    public static final int TYPE_PRIVATE_VALUE = 60;
    public static final int TYPE_EXPERIMENTAL_VALUE = 63;
    
    public static final int INDETERMINATED_LENGTH = 1;
    
    protected byte[] in = null;
    //protected byte[] header = null;
    protected byte[] body = null;
    
    protected int headerLength = 0;
    protected int bodyLength = -1;
    protected byte head = 0; // the first byte of header
    
    int i0 = 0;
    /**
     * Creates a new instance of PGPPacket
     */
    public PGPPacket() {
    }
    /**
     * Creates a new instance of PGPPacket
     */
    public PGPPacket(byte[] in) throws IOException {
        parse(in, 0);
    }
    /** Creates a new instance of PGPPacket */
    public PGPPacket(byte[] in, int startIndex) throws IOException {
        parse(in, startIndex);
    }
    /**
     * Parse Data input stream, return total length of parsed data
     */
    public int parse(byte[] in, int startIndex) throws IOException {
        init(in, startIndex);
        if (!isValid())
            throw new IOException("601");
        return getHeaderLength() + getBodyLength();
    }
    /**
     * Read byte array and init class
     */
    private void init(byte[] in, int startIndex) {
        this.in = in;
        head = in[i0 = startIndex];
        body = null;
        bodyLength = -1;
        headerLength = 0;
    }
    /*
     * Returns true if packet is valid
     */
    public boolean isValid() {
        return (head & 0x80) != 0;
    }
    /*
     * Returns true if packet has new format
     */
    public boolean hasNewFormat() throws IOException {
        return (head & 0x40) != 0;
    }
    /*
     * Returns length of the packet header
     */
    public int getHeaderLength() throws IOException {
        if (headerLength != 0)
            return headerLength;
        if (hasNewFormat()) {
            int b1 = lib.unsigned(in[i0 + 1]);
            if (b1 < 192) return headerLength = 2;
            if (b1 < 224) return headerLength = 3;
            if (b1 < 255) return headerLength = INDETERMINATED_LENGTH;
            if (b1 == 255) return headerLength = 6;
        } else  {
            switch((int)0x00000003 & head) {
                case 0: return headerLength = 2;
                case 1: return headerLength = 3;
                case 2: return headerLength = 5;
                case 3: return headerLength = INDETERMINATED_LENGTH;
            }
        }
        throw new IOException("602");
    }
    /*
     * Returns body of the packet
     */
    public byte[] getBody() throws IOException {
        if (body != null)
            return body;
        int j = 0;
        body = new byte[getBodyLength()];
        int end = i0  + getHeaderLength() + bodyLength;
        for (int i = i0  + headerLength; i < end; i++)
            body[j++] = in[i];
        return body;
    }
    /*
     * Returns body length of the packet header
     */
    public int getBodyLength() throws IOException {
        if (bodyLength != -1)
            return bodyLength;
        if (getHeaderLength() == INDETERMINATED_LENGTH)
            return -1;
        if (hasNewFormat()) {
            switch (headerLength) {
                case 2:  return bodyLength = lib.unsigned(in[i0 + 1]);
                case 3:  return bodyLength = (((lib.unsigned(in[i0 + 1]) - 192) << 8) + lib.unsigned(in[i0 + 2]) + 192);
                case 6:  return bodyLength = ((lib.unsigned(in[i0 + 2]) << 24) | (lib.unsigned(in[i0 + 3]) << 16) |
                (lib.unsigned(in[i0 + 4]) << 8) |  lib.unsigned(in[i0 + 5]));
                case INDETERMINATED_LENGTH:
                    return bodyLength = (1 << (lib.unsigned(in[i0 + 1]) & 0x1F));
            }
        } else {
            switch (headerLength) {
                case 2: return bodyLength = lib.unsigned(in[i0 + 1]);
                case 3: return bodyLength = ((lib.unsigned(in[i0 + 1])  << 8) | lib.unsigned(in[i0 + 2]));
                case 5: return bodyLength = ((lib.unsigned(in[i0 + 1]) << 24) |
                (lib.unsigned(in[i0 + 2]) << 16) |
                (lib.unsigned(in[i0 + 3]) <<  8) |  lib.unsigned(in[i0 + 4]));
                case INDETERMINATED_LENGTH:
                    return bodyLength = (1 << (lib.unsigned(in[i0 + 1]) & 0x1F));
            }
        }
        return 0;
    }
    /*
     * Returns the packet type
     */
    public int getType() throws IOException {
        if (hasNewFormat())
            return 0x003F & head;
        else
            return (0x003C & head) >> 2;
    }
    /*
     * Returns the packet type name
     */
    public String getTypeInfo() throws IOException {
        return typeInfo(getType());
    }
    /*
     * Returns a name of type by id
     */
    static public String typeInfo(int type) {
        switch (type) {
            case TYPE_RESERVED: return "TYPE_RESERVED";
            case TYPE_PUBLIC_KEY_ENCRYPTED_SESSION_KEY: return "TYPE_PUBLIC_KEY_ENCRYPTED_SESSION_KEY";
            case TYPE_SIGNATURE: return "TYPE_SIGNATURE";
            case TYPE_SYMMETRIC_KEY_ENCRYPTED_SESSION_KEY: return "TYPE_SYMMETRIC_KEY_ENCRYPTED_SESSION_KEY";
            case TYPE_ONE_PASS_SIGNATURE: return "TYPE_ONE_PASS_SIGNATURE";
            case TYPE_SECRET_KEY: return "TYPE_SECRET_KEY";
            case TYPE_PUBLIC_KEY: return "TYPE_PUBLIC_KEY";
            case TYPE_SECRET_SUBKEY: return "TYPE_SECRET_SUBKEY";
            case TYPE_COMPRESSED_DATA: return "TYPE_COMPRESSED_DATA";
            case TYPE_SYMMETRICALLY_ENCRYPTED_DATA: return "TYPE_SYMMETRICALLY_ENCRYPTED_DATA";
            case TYPE_MARKER: return "TYPE_MARKER";
            case TYPE_LITERAL_DATA: return "TYPE_LITERAL_DATA";
            case TYPE_TRUST: return "TYPE_TRUST";
            case TYPE_USER_ID: return "TYPE_USER_ID";
            case TYPE_PUBLIC_SUBKEY: return "TYPE_PUBLIC_SUBKEY";
            case TYPE_PRIVATE_VALUE: return "TYPE_PRIVATE_VALUE";
            case TYPE_EXPERIMENTAL_VALUE: return "TYPE_EXPERIMENTAL_VALUE";
            default: return "TYPE_UNKNOWN_VALUE";
        }
    }
    /*
     * Returns a vector of found packets
     */
    public static Vector parsePackets(byte[] in, String owner, byte[] password) throws IOException {
        
        Vector packets = null;
        PGPPacket p = null;
        int i = 0;
        while (i < in.length) {
            if (in[i] == 0 || in[i] == '\r' || in[i] == '\n' || in[i] == '\t' || in[i] == ' ') {
                i++;
                continue;
            }
            if (packets == null)
                packets = new Vector();
            p = new PGPPacket();
            i += p.parse(in, i);
            switch (p.getType()) {
                case TYPE_PUBLIC_KEY :
                    //lib.message("-------------->>> BEGIN ADD TYPE_PUBLIC_KEY <<<--------------");
                    packets.addElement(new PGPPublicKeyPacket(p, owner));
                    //lib.message("-------------->>> END ADD TYPE_PUBLIC_KEY <<<--------------");
                    break;
                case TYPE_SIGNATURE :
                    //lib.message("-------------->>> BEGIN ADD TYPE_SIGNATURE <<<--------------");
                    packets.addElement(new PGPSignaturePacket(p));
                    //lib.message("-------------->>> END ADD TYPE_SIGNATURE <<<--------------");
                    break;
                case TYPE_SECRET_KEY :
                    //lib.message("-------------->>> BEGIN ADD TYPE_SECRET_KEY <<<--------------");
                    packets.addElement(new PGPSecretKeyPacket(p, owner, password));
                    //lib.message("-------------->>> END ADD TYPE_SECRET_KEY <<<--------------");
                    break;
                case TYPE_TRUST :
                    //lib.message("-------------->>> BEGIN ADD TYPE_TRUST <<<--------------");
                    packets.addElement(new PGPTrustPacket(p));
                    //lib.message("-------------->>> END ADD TYPE_TRUST <<<--------------");
                    break;
                case TYPE_USER_ID :
                    //lib.message("-------------->>> BEGIN ADD TYPE_USER_ID <<<--------------");
                    packets.addElement(new PGPUserIdPacket(p));
                    //lib.message("-------------->>> END ADD TYPE_USER_ID <<<--------------");
                    break;
                default:
                    packets.addElement(p);
            }
        }
        return packets;
    }
    /*
     * Dublicate the data packet
     */
    public PGPPacket copyData(PGPPacket parent) {
        this.in = parent.in;
        this.body = parent.body;
        
        this.headerLength = parent.headerLength;
        this.bodyLength = parent.bodyLength;
        this.head = parent.head;
        this.i0 = parent.i0;
        return this;
    }
}
