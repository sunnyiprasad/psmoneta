/*
 * IPrivText.java
 *
 * Created on 24 Ноябрь 2005 г., 14:08
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

package org.cyberplat.ipriv.data;

import java.io.*;
import java.util.Vector;

import org.cyberplat.util.*;

import org.cyberplat.util.*;
import org.cyberplat.ipriv.packet.*;
import org.cyberplat.ipriv.info.*;

/**
 *
 * @author  Shutov
 */
public class IPrivSerial {
    public static String TYPE_SIGNED_MESSAGE = "SM";
    public static String TYPE_SECRET_KEY = "NM";
    public static String TYPE_PUBLIC_KEY = "NS";
    public static String VERSION = "01";
    
    public static int SIZE_MINIMAL_DATA_SIZE = 140;
    private static int SIZE_KEY_NUMBER = 8;
    private static int SIZE_VERSION = 2;
    private static int SIZE_TYPE = 2;
    //    private static int SIZE_SPACE = 9;
    private static int SIZE_IDENTIFIER = 20;
    
    private static String TAG_BEGIN = "BEGIN";
    private static String TAG_END = "END";
    
    private static String TAG_BEGIN_SIGNATURE = "BEGIN SIGNATURE";
    private static String TAG_END_SIGNATURE = "END SIGNATURE";
    
    private int length = 0;
    private String version = "01";
    public String type = "UN";
    
    private int encodedDataLength = 0;
    private byte[] encodedDataCrc = null;
    
    public int dataLength = 0;
    public Radix64Serial data = null;
    
    private int signatureLength = 0;
    public Radix64Serial signature = null;
    
    public String signer = "";
    public String signerKeyNumber = "";
    
    public String owner = null;
    public String ownerKeyNumber = null;
    
    public Vector keyPackets = null;
    public Vector sigPackets = null;
    
    public PGPSignaturePacket sigPack = null;
    public PGPSecretKeyPacket secPack = null;
    public PGPPublicKeyPacket pubPack = null;
    public PGPUserIdPacket userIdPack = null;
    
    /** Creates a new instance of IPrivText */
    public IPrivSerial() {
    }
    /** Creates a new instance of IPrivText */
    public IPrivSerial(String type) {
        this.type = type;
    }
    /** Creates a new instance of IPrivText */
    public IPrivSerial(DataInputStream in, byte[] password, java.util.Hashtable keyInfoTable) throws IOException {
        parse(in, password, keyInfoTable);
    }
    
    
    public IPrivSerial(SignatureInfo si, String si_owner, String si_ownerKeyNumber,
    byte[] si_data, int si_start, int si_length) throws java.io.IOException {
        type = IPrivSerial.TYPE_SIGNED_MESSAGE;
        
        owner = si_owner;
        ownerKeyNumber = si_ownerKeyNumber;
        
        data = new Radix64Serial();
        data.setPlainData(si_data, si_start, si_length);
        dataLength = si_length;
        
        sigPack = new PGPSignaturePacket(si);
        sigPackets = new Vector();
        sigPackets.addElement(sigPack);
        signature = sigPack.getPacket();
        
    }
    
    public IPrivSerial(
    String p_type,
    String p_owner, String p_ownerKeyNumber,
    String p_signer, String p_signerKeyNumber,
    Radix64Serial p_data,
    Radix64Serial p_signature) {
        type = p_type;
        owner = p_owner;
        ownerKeyNumber = lib.makeKeyNumber(p_ownerKeyNumber);
        if (p_signer != null)
            signer = p_signer;
        if (p_signerKeyNumber != null)
            signerKeyNumber = lib.makeKeyNumber(p_signerKeyNumber);
        data = p_data;
        signature = p_signature;
    }
    
    /**
     * Parse input stream taht can contain
     * a secret key, a public key or a signed lib.message
     *
     * Парсирует входящий поток который может сожержать ключ приватный, публичный или
     * подписанное сообщение.
     */
    synchronized public void parse(DataInputStream in, byte[] password, java.util.Hashtable keyInfoTable) throws IOException {

        // проверяем что поток равено null
        if (in == null)
            throw new IOException(Integer.toString(201));//Stream is null.");

        // проверяем на то что минимальный размер ключа 140 байт
        if (in.available() < SIZE_MINIMAL_DATA_SIZE)
            throw new IOException(Integer.toString(202) + "-" + in.available());//Not enough data. (" + in.available() + " < " + SIZE_MINIMAL_DATA_SIZE + ")");

        // проверяем что количество доступных байтов в потоке меньше чем размер ключа указанный в саомом ключе
        length = lib.ReadNumericAtStart(in);
        System.out.println(in.available());
        if (in.available() < length)
            throw new IOException(Integer.toString(203));//Bad data length. (" + in.available() + " < " + length + ")");

        // Проверяется версия.
        version = lib.readString(in, SIZE_VERSION);
        if (!version.equals(VERSION))
            throw new IOException(Integer.toString(204));//Wrong version.");

        // ПРоверяем что тип размера может быть публичный приватный или подписанное сообщение
        type = lib.readString(in, SIZE_TYPE);
        if (!type.equals(this.TYPE_SECRET_KEY) &&
        !type.equals(this.TYPE_PUBLIC_KEY) &&
        !type.equals(this.TYPE_SIGNED_MESSAGE))
            throw new IOException(Integer.toString(205));//Bad type.");

        // размер зашифрованных данных
        encodedDataLength = lib.readNumeric(in);

        // размер данных
        dataLength = lib.readNumeric(in);

        //длина подписи
        signatureLength = lib.readNumeric(in);
        lib.readLn(in);

        //Читает имя владельца ключа строка 20 байт
        owner = lib.readString(in, SIZE_IDENTIFIER).trim();
        // Читает идентичикатор владельца, он у на имеет вид 00000001 8 байт.
        ownerKeyNumber = lib.makeKeyNumber(lib.readString(in, SIZE_KEY_NUMBER).trim());
        lib.readLn(in);

        // Читает имя подписавшего ключ, строка 20 байт
        signer = lib.readString(in, SIZE_IDENTIFIER).trim();
        // Читает идентичикатор владельца, он у на имеет вид 00000001 8 байт.
        signerKeyNumber = lib.makeKeyNumber(lib.readString(in, SIZE_KEY_NUMBER).trim());
        lib.readLn(in);


        if (!lib.readString(in, TAG_BEGIN.length()).equals(TAG_BEGIN))
            throw new IOException(Integer.toString(206));//TAG_BEGIN + " not found or corrupted.");
        lib.readLn(in);

        // Если это ключ публичный или приватный
        if (type.equals(TYPE_PUBLIC_KEY) || type.equals(TYPE_SECRET_KEY)) {
            data = new Radix64Serial(in, encodedDataLength);
            keyPackets = PGPPacket.parsePackets(data.data, owner, password);
            if (keyPackets == null)
                throw new IOException(Integer.toString(207));//IPrivSerial: key packets not found.");
            
            for (java.util.Enumeration e = keyPackets.elements(); e.hasMoreElements();) {
                PGPPacket p = (PGPPacket)e.nextElement();
                if (p instanceof PGPPublicKeyPacket) {
                    pubPack = (PGPPublicKeyPacket)p;
                }
                else if (p instanceof PGPSecretKeyPacket) {
                    secPack = (PGPSecretKeyPacket)p;
                }
                else if (p instanceof PGPUserIdPacket) {
                    userIdPack = (PGPUserIdPacket)p;
                }
                
            }
            
            if (type.equals(TYPE_PUBLIC_KEY) && pubPack == null)
                throw new IOException(Integer.toString(208));//IPrivSerial: public key packet not found.");
            else
                if (type.equals(TYPE_SECRET_KEY) && secPack == null)
                    throw new IOException(Integer.toString(209));//IPrivSerial: secret key packet not found.");
        } else {
            // Иначе это сообщение подписанное.
            data = new Radix64Serial();
            data.setPlainData(lib.readByteArray(in, encodedDataLength));
        }
        
        lib.readLn(in);
        
        if (!lib.readString(in, TAG_END.length()).equals(TAG_END))
            throw new IOException(Integer.toString(210));//TAG_END + " not found or corrupted.");
        lib.readLn(in);
        
        if (!lib.readString(in, TAG_BEGIN_SIGNATURE.length()).equals(TAG_BEGIN_SIGNATURE))
            throw new IOException(Integer.toString(211));//TAG_BEGIN_SIGNATURE + " not found or corrupted.");
        lib.readLn(in);
        
        if (signatureLength > 0) {
            signature = new Radix64Serial(in, signatureLength);
            sigPackets = PGPPacket.parsePackets(signature.data, owner, password);
            if (sigPackets == null)
                throw new IOException(Integer.toString(212));//IPrivSerial: signature packets not found.");
            
            for (java.util.Enumeration e = sigPackets.elements(); e.hasMoreElements();) {
                PGPPacket p = (PGPPacket)e.nextElement();
                //RST: lib.message("PACKET INFO : " + p.toString());
                if (p instanceof PGPSignaturePacket)
                    sigPack = (PGPSignaturePacket)p;
            }
            
            if (sigPack == null)
                new IOException(Integer.toString(213));//IPrivSerial: signature packet not found.");
            
            if (sigPack.getInfo() == null)
                new IOException(Integer.toString(214));//IPrivSerial: sigPack.getInfo() == null");
            
            if (type.equals(TYPE_SIGNED_MESSAGE)) {
                sigPack.getInfo().check(null, null, data.encoded, 0, data.encoded.length);
            }
            else if (type.equals(TYPE_PUBLIC_KEY)) {
                PublicKeyInfo pki = pubPack.getInfo();
                String pkiKeyNumber = pki.getKeyNumber();
                if (!pkiKeyNumber.equals(signerKeyNumber)) {
                    if (!keyInfoTable.containsKey(signerKeyNumber))
                        throw new IOException(Integer.toString(215));//Signer not found");
                    else
                        pki = (PublicKeyInfo)keyInfoTable.get(signerKeyNumber);
                }
                sigPack.getInfo().check(pki, owner, data.data, 0,
                pubPack.getBody().length + 3);
            }
        }
        lib.readLn(in);
        if (!lib.readString(in, TAG_END_SIGNATURE.length()).equals(TAG_END_SIGNATURE))
            throw new IOException(Integer.toString(216));//TAG_END_SIGNATURE + " not found or corrupted.");
    }
    /**
     */
    public PGPSignaturePacket getSignaturePacket() {
        return sigPack;
    }
    /**
     */
    public PGPPublicKeyPacket getPublicKeyPacket() {
        return pubPack;
    }
    /**
     */
    public PGPSecretKeyPacket getSecretKeyPacket() {
        return secPack;
    }
    /**
     * Get data
     */
    public Radix64Serial getData() {
        return data;
    }
    
    public boolean isSecretKey() {
        return type.equals(TYPE_SECRET_KEY);
    }
    
    public boolean isPublicKey() {
        return type.equals(TYPE_PUBLIC_KEY);
    }
    
    public boolean isSignedMessage() {
        return type.equals(TYPE_SIGNED_MESSAGE);
    }
    
    public String getSigner() { return signer; }
    public String getSignerKeyNumber() { return signerKeyNumber; }
    
    public String getOwner() { return owner; }
    public String getOwnerKeyNumber() { return ownerKeyNumber; }
    
    
    public java.util.Vector getOutVector() throws java.io.IOException {
        Vector dataVector = null;
        if (data != null) {
            dataVector = data.getOutVector();
            encodedDataLength = lib.VectorLength(dataVector);
            if (data.data != null)
                dataLength = data.data.length;
            else
                dataLength = encodedDataLength;
        }
        Vector signatureVector = null;
        if (signature != null) {
            signatureVector = signature.getOutVector();
            signatureLength = lib.VectorLength(signatureVector);
        }
        
        Vector out = new Vector();
        // The first line
        out.addElement(lib.format(version, SIZE_VERSION, (byte)'0'));
        out.addElement(lib.format(type, SIZE_TYPE, (byte)'*'));
        out.addElement(lib.format(encodedDataLength, lib.SIZE_NUMERIC, (byte)'0'));
        out.addElement(lib.format(dataLength, lib.SIZE_NUMERIC, (byte)'0'));
        out.addElement(lib.format(signatureLength, lib.SIZE_NUMERIC, (byte)'0'));
        out.addElement(lib.format(lib.TAG_CRLF, lib.TAG_CRLF.length(), (byte)0));
        // The second line
        out.addElement(lib.formatLeft(owner, SIZE_IDENTIFIER, (byte)' '));
        out.addElement(lib.format(ownerKeyNumber, SIZE_KEY_NUMBER, (byte)'0'));
        out.addElement(lib.format(lib.TAG_CRLF));
        // The third line
        out.addElement(lib.formatLeft(signer, SIZE_IDENTIFIER, (byte)' '));
        out.addElement(lib.format(signerKeyNumber, SIZE_KEY_NUMBER, (byte)'0'));
        out.addElement(lib.format(lib.TAG_CRLF));
        // The BEGIN tag
        out.addElement(lib.format(TAG_BEGIN));
        out.addElement(lib.format(lib.TAG_CRLF));
        // Data received from data packets
        out.addElement(dataVector);
        out.addElement(lib.format(lib.TAG_CRLF));
        // The END tag
        out.addElement(lib.format(TAG_END));
        out.addElement(lib.format(lib.TAG_CRLF));
        // The BEGIN SIGNATURE tag
        out.addElement(lib.format(TAG_BEGIN_SIGNATURE));
        out.addElement(lib.format(lib.TAG_CRLF));
        // Data received from signature packets
        out.addElement(signatureVector);
        out.addElement(lib.format(lib.TAG_CRLF));
        // The END SIGNATURE tag
        out.addElement(lib.format(TAG_END_SIGNATURE));
        
        // Insert the first element with content data here!
        length = lib.VectorLength(out);
        out.insertElementAt(lib.format(length, lib.SIZE_NUMERIC, (byte)'0'), 0);
        return out;
    }
    public String toString() {
        try {
            return lib.VectorToString(getOutVector());
        } catch (java.io.IOException ex) {
            return ex.getMessage();
        }
    }
    public byte[] toByteArray() {
        try {
            return lib.VectorToByteArray(getOutVector());
        } catch (java.io.IOException ex) {
            return ConvertText.toWin1251(ex.getMessage());
        }
    }
    
    public byte[] getPlainData() {
        if (data != null)
            return data.getPlainData();
        else
            return null;
    }
}