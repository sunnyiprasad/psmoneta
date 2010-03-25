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

package org.cyberplat.ipriv;

import java.io.*;
import java.lang.*;
import java.util.Vector;
import java.util.Hashtable;


import org.bouncycastle.crypto.*;

import java.math.BigInteger;

//import org.bouncycastle.util.IPriv.*;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.params.RSAPrivateCrtKeyParameters;
import org.bouncycastle.crypto.params.RSAKeyGenerationParameters;
import org.bouncycastle.crypto.generators.RSAKeyPairGenerator;

import org.cyberplat.util.*;
import org.cyberplat.rms.*;

import org.cyberplat.ipriv.algorithm.*;
import org.cyberplat.ipriv.cipher.*;
import org.cyberplat.ipriv.data.*;
import org.cyberplat.ipriv.info.*;
import org.cyberplat.ipriv.packet.*;

/**
 *
 * @author  Shutov
 */
public class  IPriv {
    static final String SECRET_FILE_NAME = "sk";
    static final String PUBLIC_FILE_NAME = "pk";
    
    public static boolean VERIFY_SELF_SIGNED = false;
    //public static String defaultDirectory = "c:\\cyberplat\\";
    byte[] password = new byte[] {0};

    
    String secretFileName = SECRET_FILE_NAME;
    String publicFileName = PUBLIC_FILE_NAME;
    DataStorage storageSecretFile = null;
    DataStorage storagePublicFile = null;
    
    private String ownerKeyNumber = null;
    private BufferedBlockCipher cipher = null;
    public SecretKeyInfo secretKeyInfo = null;
    public PublicKeyInfo publicKeyInfo = null;
    public Hashtable publicKeyInfoTable = new Hashtable();
    
    // Constructor
    public IPriv(String password) {
        this.password = getDigest(password);
    }
    
    // Constructor
    public IPriv(byte[] password_digest) {
        this.password = password_digest;
    }


    // Constructor
    public IPriv(String password, String secretFileName, String publicFileName) {
        this.secretFileName = secretFileName;
        this.publicFileName = publicFileName;
        this.password = getDigest(password);
    }
    
    // Constructor
    public IPriv(byte[] password_digest, String secretFileName, String publicFileName) {
        this.secretFileName = secretFileName;
        this.publicFileName = publicFileName;
        this.password = password_digest;
    }
    
    public PublicKeyInfo getPublicKeyInfo() {
        return publicKeyInfo;
    }
    
    public String getOwner() {
        return secretKeyInfo.owner;
    }
    
    public byte[] getPassword() {
        return password;
    }
    
    public PublicKeyInfo getPublicKeyInfo(String keyNumber) {
        return (PublicKeyInfo) publicKeyInfoTable.get(lib.makeKeyNumber(keyNumber));
    }
    
    public PublicKeyInfo getPublicKeyInfo(long keyNumber) {
        return (PublicKeyInfo) publicKeyInfoTable.get(lib.makeKeyNumber(keyNumber));
    }
    
    public InputStream getStoredSecretKeyFileData() throws IOException {
        if (storageSecretFile != null){
            return storageSecretFile.getInputStream();
        }else{
            return new FileInputStream(secretFileName);
        }
    }
    
    public InputStream getStoredPublicKeyFileData() throws IOException {
        if (storagePublicFile != null && !storagePublicFile.isEmpty())
            return storagePublicFile.getInputStream();
        else
            return new FileInputStream( publicFileName);
    }

    public static  boolean storedPublicKeyFileDataExists(String storageName, String storagePassword) {
        return DataStorage.storageExists(storageName, storagePassword, PUBLIC_FILE_NAME);
    }
    
    public void openSecretKeyFileData(String storageName, String storagePassword) throws IOException {
        storageSecretFile = new DataStorage(storageName, storagePassword, secretFileName);
        storageSecretFile.loadStoredData();
    }

    public static boolean storedSecretKeyFileDataExists(String storageName, String storagePassword) {
        return DataStorage.storageExists(storageName, storagePassword, SECRET_FILE_NAME);
    }
    
    
    public void deleteSecretKeyFileData() throws IOException {
        if (storageSecretFile != null) {
            storageSecretFile.delete();
            storageSecretFile = null;
        }
    }
    
    public void openPublicKeyFileData(String storageName, String storagePassword) throws IOException {
        storagePublicFile = new DataStorage(storageName, storagePassword, publicFileName);
        storagePublicFile.loadStoredData();
        
        //RST: lib.message("openPublicKeyFileData as " + storageSecretFile.getRMSName() + ".");
    }
    
    public void deletePublicKeyFileData() throws IOException {
        if (storagePublicFile != null) {
            storagePublicFile.delete();
            storagePublicFile = null;
        }
    }
    
    public void storePublicKeyFileData(String storageName, String storagePassword) throws IOException {
        if (storagePublicFile == null)
            storagePublicFile = new DataStorage(storageName, storagePassword, publicFileName);
        if (storagePublicFile == null)
            throw new IOException("1001");
        byte[] txt = getPublicKeyFileData();
        storagePublicFile.updateStoredData(txt);
    }
    
    public void storeSecretKeyFileData(String storageName, String storagePassword) throws IOException {
        if (storageSecretFile == null)
            storageSecretFile = new DataStorage(storageName, storagePassword, secretFileName);
        if (storageSecretFile == null)
            throw new IOException("1002");//IPriv.storeSecretKeyFileData: Secret key storage is null.");
        
        byte[] txt = getSecretKeyText();
        //RST: lib.message("storeSecretKeyFileData in " + storageSecretFile.getRMSName() + ":\r\n", txt);
        storageSecretFile.updateStoredData(txt);
    }
    
    public void closeSecretKeyFileData() throws IOException {
        if (storageSecretFile != null) {
            storageSecretFile.close();
            storageSecretFile = null;
        }
    }
    
    public void closePublicKeyFileData() throws IOException {
        if (storagePublicFile != null) {
            storagePublicFile.close();
            storagePublicFile = null;
        }
    }
    
    public void init(String storageName, String storagePassword) throws IOException {
        openSecretKeyFileData(storageName, storagePassword);
        openPublicKeyFileData(storageName, storagePassword);
        init();
    }
    
    /*
     * Read configurations file and init the IPriv object
     */
    public void init() throws IOException {
        try {
            // Загружаем приватный ключ
            InputStream stream  = getStoredSecretKeyFileData();
            if (stream == null)
                throw new IOException("1003");
            
            DataInputStream dis = new DataInputStream(stream);
            
            byte[] rd = null;
            
            if (dis.available() > 0) {
                rd = new byte[dis.available()];
                dis.readFully(rd);
                dis = null;
                dis = new DataInputStream(new ByteArrayInputStream(rd));
            }
            
            IPrivSerial ips = new IPrivSerial(dis, password, publicKeyInfoTable);

            if (ips == null)
                throw new IOException("1004");//IPriv.init: Can't create secret serialized.");

            PGPSecretKeyPacket sk = ips.getSecretKeyPacket();
            if (sk == null)
                throw new IOException("1005");//IPriv.init: Secret key packet not found.");

            secretKeyInfo = sk.getInfo();
            if (secretKeyInfo == null)
                throw new IOException("1006");//IPriv.init: Secret key info not found.");

            if (secretKeyInfo.owner == null)
                throw new IOException("1007");//IPriv.init: Secret key owner is null.");

            secretKeyInfo.setSecretText(rd);
            
            ownerKeyNumber = ips.getOwnerKeyNumber();
            try {
                long l = java.lang.Long.parseLong(ownerKeyNumber, 10);
                if (secretKeyInfo.keyId != l )
                    throw new IOException("1008");//IPriv.init: Invalid secret key number " + ownerKeyNumber + " and " + secretKeyInfo.keyId);
                
            } catch (java.lang.Exception e) {
                throw new IOException(e.getMessage());
            }

            // Конец загрузки приватного ключа

            // Начало загрузки публичного ключа
            
            stream  = getStoredPublicKeyFileData();
            if (stream != null) {
                rd = null;
                dis = new DataInputStream(stream);
                
                int nstart = 0;
                int ntotal = dis.available();
                if (ntotal > 0) {
                    rd = new byte[dis.available()];
                    dis.readFully(rd);
                    dis = null;
                    dis = new DataInputStream(new ByteArrayInputStream(rd));
                }
                
                do {
                    ips = new IPrivSerial(dis, password, publicKeyInfoTable);
                    if (ips == null)
                        throw new IOException("1009");//IPriv.init: Can't create public serialized.");
                    
                    PGPPublicKeyPacket pk = ips.getPublicKeyPacket();
                    if (pk == null)
                        throw new IOException("1010");//IPriv.init: Public key packet not found.");
                    PublicKeyInfo pki = pk.getInfo();
                    int ns = ntotal - nstart - dis.available();
                    pki.setText(rd, nstart, ns);
                    nstart += ns;
                    publicKeyInfoTable.put(ips.getOwnerKeyNumber(), pki);
                    
                } while (dis.available() >= IPrivSerial.SIZE_MINIMAL_DATA_SIZE);
                
                if (publicKeyInfo == null)
                    publicKeyInfo = (PublicKeyInfo) publicKeyInfoTable.get(ownerKeyNumber);
            } else {
                lib.message("IPriv.init: Can't read " + secretFileName);
            }
            if (publicKeyInfo == null)
                publicKeyInfo = secretKeyInfo.clonePublicKeyInfo();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }
    
    
    /*
     * CheckSignature
     */
    public boolean checkSignature(DataInputStream in) throws IOException {
        return checkSignature(in, new IPrivSerial());
    }
    
    /*
     * CheckSignature
     */
    public boolean checkSignature(DataInputStream in, IPrivSerial sis) throws IOException {
        try {
            if (sis == null)
                sis = new IPrivSerial();
            sis.parse(in, password, publicKeyInfoTable);
            if (!sis.isSignedMessage())
                return false;
            
            PGPSignaturePacket sp = sis.getSignaturePacket();
            
            if (sp == null)
                return false;
            PublicKeyInfo signerPublicKey = getPublicKeyInfo(sis.getOwnerKeyNumber());
            if (signerPublicKey == null)
                throw new IOException("1011");//IPriv.checkSignature: Unknown signer " + sis.getOwnerKeyNumber());
            
            SignatureInfo si = sp.getInfo();
            if (si == null)
                return false;
            
            if (verify(si, signerPublicKey.modulus, signerPublicKey.exponent) == null)
                throw new IOException("1012");//IPriv.checkSignature: Bad signature");
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        return true;
    }
    
    private byte[] verify(SignatureInfo si, BigIntegerImage modulus, BigIntegerImage exponent) throws java.io.IOException {
        int lh = si.hashValue.length;
        if (lh == 0)
            throw new java.io.IOException("1013");//IPriv.verify: Bad string length to verify.");
        
        IPrivRSACipher cipher = //keyInfo.getCipher(false);
        new IPrivRSACipher(false, modulus.getBigInteger(), exponent.getBigInteger(), false);
        
        byte[] si_value = si.value.getValue(si.value.getEffectiveLength());
        byte[] result = cipher.process(si_value, 0, si_value.length);
        
        if (result == null || result.length < lh)
            return null;
        //RST: lib.messageA("VERIFY  COMPARE : ", si.hashValue);
        //RST: lib.messageA("        AND     : ", result);
        //RST: lib.message("         ==>     : " + lib.compareFromTheEnd(si.hashValue, result));
        
        if (lib.compareFromTheEnd(si.hashValue, result))
            return result;
        return null;
    }
    /**/
    public SignatureInfo signData(byte[] data, int start, int ndata) throws IOException {
        if (publicKeyInfo == null)
            throw new IOException("1014");//IPriv.sigData: Public key is null.");
        
        int nsrc = data.length;
        
        SignatureInfo si = new SignatureInfo();
        si.time = System.currentTimeMillis();
        si.keyId = secretKeyInfo.keyId;
        
        si.type = SignatureInfo.SIGN_TEXT_DOCUMENT;
        
        si.getHashMD5(data, start, ndata, null);
        si.initLeftHashValue();
        byte[] md = si.getHashData();
        byte[] enc_md = encrypt(md, 0, md.length);
        si.value = new BigIntegerImage(enc_md);
        
        if (VERIFY_SELF_SIGNED && verify(si, secretKeyInfo.modulus, secretKeyInfo.exponent) == null)
            throw new IOException("1015");//IPriv.signData: Bad signature.");
        
        return si;
    }
    
    private byte[] encrypt(byte[] in, int istart, int ilen) throws IOException {
        int status;
        int modulusLen = publicKeyInfo.modulus.getEffectiveLength();
        if (ilen + 11 > modulusLen)
            throw new IOException("1016");//IPriv.encrypt: not enough modulus length.");
        
        //        byte[] pkcsBlock = new byte[PublicKeyInfo.MAX_RSA_MODULUS_LEN];
        byte[] pkcsBlock = new byte[PublicKeyInfo.MAX_RSA_MODULUS_LEN];
        int i = 0;
        int end = modulusLen - ilen - 1;
        
        // make { 0, 1, -1, ... -1, 0, {signature}}
        pkcsBlock[i++] = 0;// start
        pkcsBlock[i++] = 1;// block type 1
        while (i < end) pkcsBlock[i++] = -1;
        // add zero as separator
        pkcsBlock[i++] = 0;
        System.arraycopy(in, istart, pkcsBlock, i, ilen);
        IPrivRSACipher cipher = secretKeyInfo.getSecretCipher();
        
        byte[] result = cipher.process(pkcsBlock, 1, pkcsBlock.length - 1);
        // Zeroize potentially sensitive information.
        for (i = 0; i < pkcsBlock.length; i++)
            pkcsBlock[0] = 0;
        pkcsBlock = null;
        //RST: lib.messageA("result: ", result);
        return result;
    }
    
    // Constructor
    public boolean generate(KeyCardInfo kci) {
        return generate(kci.keyId, kci.userId);
    }
    
    public boolean generate(long vKeyId, String vOwner) {
        String owner = vOwner.trim();
        long   generate_time = System.currentTimeMillis() / 1000;
        if (owner.length() > 20)
            owner = owner.substring(0, 20);
        
        ownerKeyNumber = lib.makeKeyNumber(vKeyId);
        
        BigInteger pubExp = new BigInteger(new byte[]{17});
        java.security.SecureRandom sr = new java.security.SecureRandom();
        RSAKeyGenerationParameters RSAKeyGenPara = new RSAKeyGenerationParameters(pubExp, sr, PublicKeyInfo.MAX_RSA_MODULUS_BITS, 80);
        RSAKeyPairGenerator RSAKeyPairGen = new RSAKeyPairGenerator();
        RSAKeyPairGen.init(RSAKeyGenPara);
        AsymmetricCipherKeyPair keyPair = RSAKeyPairGen.generateKeyPair();
        RSAPrivateCrtKeyParameters secKey = (RSAPrivateCrtKeyParameters) keyPair.getPrivate();
        RSAKeyParameters pubKey = (RSAKeyParameters) keyPair.getPublic();
        
        publicKeyInfo = new PublicKeyInfo(owner);
        publicKeyInfo.time = generate_time;
        
        publicKeyInfo.exponent = new BigIntegerImage(pubKey.getExponent());
        publicKeyInfo.modulus = new BigIntegerImage(pubKey.getModulus());
        
        publicKeyInfo.version = PublicKeyInfo.NEW_VERSION;
        publicKeyInfo.keyId = vKeyId;
        publicKeyInfo.algorithm = org.bouncycastle.bcpg.PublicKeyAlgorithmTags.RSA_GENERAL;
        publicKeyInfo.validity = 0;
        
        clearKeyInfoTable();
        
        secretKeyInfo = new SecretKeyInfo(owner, password);
        secretKeyInfo.time = generate_time;
        secretKeyInfo.version = PublicKeyInfo.NEW_VERSION;
        secretKeyInfo.keyId = vKeyId;
        
        secretKeyInfo.algorithm = org.bouncycastle.bcpg.PublicKeyAlgorithmTags.RSA_GENERAL;
        
        secretKeyInfo.secretAlgorithm = SymmetricKeyAlgorithm.IDEA;
        
        secretKeyInfo.secretAlgorithmParam = sr.getSeed(secretKeyInfo.SIZE_SECRET_ALGORITHM_PARAM);
        
        IPrivIDEACipher cipher =  secretKeyInfo.getIDEACipher(true);
        secretKeyInfo.secretAlgorithmDump = cipher.process(secretKeyInfo.secretAlgorithmParam);
        
        secretKeyInfo.modulus = new BigIntegerImage(secKey.getModulus());
        secretKeyInfo.exponent = new BigIntegerImage(secKey.getPublicExponent());
        
        secretKeyInfo.secretExponent = new BigIntegerImage(secKey.getExponent(), cipher);
        
        secretKeyInfo.primeP = new BigIntegerImage(secKey.getP(), cipher);
        secretKeyInfo.primeQ = new BigIntegerImage(secKey.getQ(), cipher);
        secretKeyInfo.inversePQ = new BigIntegerImage(secKey.getQInv(), cipher);
        
        secretKeyInfo.checkSumma = secretKeyInfo.calcCheckSumma();
        return true;
    }
    // returns signed document
    public byte[] sign(byte[] data, int start, int ndata) throws IOException {
        SignatureInfo si = signData(data, start, ndata);
        IPrivSerial p = new IPrivSerial(si, getOwner(), ownerKeyNumber, data, start, ndata);
        Vector v = p.getOutVector();
        if (v == null)
            throw new IOException("1017");//IPriv.sign: error in resulting vector");
        return lib.VectorToByteArray(v);
    }
    // returns secret key as text document
    public byte[] getSecretKeyText() throws IOException {
        if (secretKeyInfo.getSecretText() != null)
            return secretKeyInfo.getSecretText();
        
        PGPSecretKeyPacket skp = new PGPSecretKeyPacket(secretKeyInfo);
        PGPUserIdPacket uip = new PGPUserIdPacket(getOwner());
        // слияние двух векторов
        java.util.Vector v = skp.buildBodyVector();
        v.addElement(uip.buildBodyVector());
        Radix64Serial packBody = new Radix64Serial(true, lib.VectorToByteArray(v), 0, -1);
        IPrivSerial p = new IPrivSerial(
        IPrivSerial.TYPE_SECRET_KEY,
        getOwner(), ownerKeyNumber,
        null, null,
        packBody, null
        );
        v = p.getOutVector();
        if (v == null)
            throw new IOException("1018");//IPriv.getSecretKeyText: error in resulting vector");
        return secretKeyInfo.setSecretText(lib.VectorToByteArray(v));
    }
    
    private SignatureInfo signPublicKeyBody(String keyUserId, byte[] body) throws IOException {
        if (publicKeyInfo == null)
            throw new IOException("1019");//IPriv.signPublicKeyBody: Public key hasn't been initialized.");
        SignatureInfo si = new SignatureInfo();
        si.keyId = secretKeyInfo.keyId;
        si.type = SignatureInfo.SIGN_GENERIC_CERTIFICATION;
        si.getHashMD5(body, 0, body.length, ConvertText.toWin1251(keyUserId));
        si.initLeftHashValue();
        byte[] md = si.getHashData();
        byte[] enc_md = encrypt(md, 0, md.length);
        si.value = new BigIntegerImage(enc_md);
        return si;
    }
    
    public byte[] getPublicKeyText(String keyNumber) throws IOException {
        PublicKeyInfo pki = getPublicKeyInfo(keyNumber);
        if (pki == null)
            throw new IOException("1020");//IPriv.getPublicKeyText: Public key " + keyNumber + "not found");
        return getPublicKeyText(pki);
    }
    
    public byte[] getPublicKeyText(long keyNumber) throws IOException {
        PublicKeyInfo pki = getPublicKeyInfo(keyNumber);
        if (pki == null)
            throw new IOException("1021");//IPriv.getPublicKeyText: Public key " + keyNumber + "not found");
        return getPublicKeyText(pki);
    }
    // returns public key as text document
    public byte[] getPublicKeyText(PublicKeyInfo pki) throws IOException {
        if (pki.getText() != null)
            return pki.getText();
        String p_owner = pki.owner;
        String p_ownerKeyNumber = Long.toString(pki.keyId);
        String p_signer = getOwner();
        String p_signerKeyNumber = ownerKeyNumber;
        
        Vector v = new Vector();
        
        v.addElement((new PGPPublicKeyPacket(pki)).buildBodyVector());
        
        SignatureInfo si = signPublicKeyBody(p_owner, lib.VectorToByteArray(v));
        
        if (verify(si, secretKeyInfo.modulus, secretKeyInfo.exponent) == null)
            throw new IOException("1022");//IPriv.getPublicKeyText: Bad signature of the public key during sign.");
        
        v.addElement((new PGPTrustPacket(-121)).buildBodyVector());
        v.addElement((new PGPUserIdPacket(p_owner)).buildBodyVector());
        v.addElement((new PGPTrustPacket(3)).buildBodyVector());
        
        Vector s = new Vector();
        
        s.addElement((new PGPSignaturePacket(si)).buildBodyVector());
        s.addElement((new PGPTrustPacket(-57)).buildBodyVector());
        
        IPrivSerial ips = new IPrivSerial(IPrivSerial.TYPE_PUBLIC_KEY,
        p_owner, p_ownerKeyNumber, p_signer, p_signerKeyNumber,
        new Radix64Serial(lib.VectorToByteArray(v)),
        new Radix64Serial(lib.VectorToByteArray(s)));
        return pki.setText(ips.toByteArray());
    }
    // returns public key as text document
    public byte[] getPublicKeyText() throws IOException {
        return getPublicKeyText(publicKeyInfo);
    }
    
    public byte[] getPublicKeyFileData() throws IOException {
        Vector v = new Vector();
        v.addElement(getPublicKeyText());
        for (java.util.Enumeration e = publicKeyInfoTable.elements(); e.hasMoreElements();) {
            PublicKeyInfo p = (PublicKeyInfo)e.nextElement();
            if (p.keyId != publicKeyInfo.keyId) {
                v.addElement(new byte[]{'\r', '\n'});
                v.addElement(getPublicKeyText(p));
            }
        }
        return lib.VectorToByteArray(v);
    }
    
    public void clearKeyInfoTable() {
        publicKeyInfoTable.clear();
        publicKeyInfoTable.put(ownerKeyNumber, publicKeyInfo);
    }
    
    public byte[] getKeysFileData() throws IOException {
        Vector v = new Vector();
        v.addElement(getSecretKeyText());
        v.addElement(new byte[]{'\r', '\n'});
        v.addElement(getPublicKeyFileData());
        return lib.VectorToByteArray(v);
    }
    /*
     * Return MD5 digest of a string
     */
    public static byte[] getDigest(String s) {
        return IPrivIDEACipher.getDigest(ConvertText.toWin1251(s));
    }
    public byte[] getDigest() {
        if (password == null || (password.length == 1 &&  password[0] == 0))
            return null;
        return password;
    }
    
    public byte[] acceptKey(DataInputStream in, boolean needClearKeyInfoTable) throws IOException {
        byte[] ext_password = new byte[]{0};
        IPrivSerial ips = new IPrivSerial(in, ext_password, new Hashtable());
        PGPPublicKeyPacket ext_pkp = ips.getPublicKeyPacket();
        if (ext_pkp == null)
            throw new IOException("1023");//IPriv.acceptKey: public key not found");
        
        byte[] new_pub_key_text =  getPublicKeyText(ext_pkp.getInfo());
        
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(new_pub_key_text));
        
        ips = new IPrivSerial(dis, password, publicKeyInfoTable);
        if (ips == null)
            throw new IOException("1024");//IPriv.init: Can't create public serialized.");
        
        PGPPublicKeyPacket pk = ips.getPublicKeyPacket();
        if (pk == null)
            throw new IOException("1025");//IPriv.init: Public key packet not found.");
        
        PublicKeyInfo pki = pk.getInfo();
        pki.setText(new_pub_key_text, 0, new_pub_key_text.length);
        
        if (needClearKeyInfoTable)
            clearKeyInfoTable();
        publicKeyInfoTable.put(ips.getOwnerKeyNumber(), pki);
        return new_pub_key_text;
    }
}
