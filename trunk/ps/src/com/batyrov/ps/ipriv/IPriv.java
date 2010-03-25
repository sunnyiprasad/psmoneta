package com.batyrov.ps.ipriv;


import org.cyberplat.util.lib;
import org.cyberplat.util.ConvertText;
import org.cyberplat.ipriv.cipher.IPrivRSACipher;
import org.cyberplat.ipriv.cipher.IPrivIDEACipher;
import org.cyberplat.ipriv.info.PublicKeyInfo;
import org.cyberplat.ipriv.info.SignatureInfo;
import org.cyberplat.ipriv.info.SecretKeyInfo;
import org.cyberplat.ipriv.packet.PGPPublicKeyPacket;
import org.cyberplat.ipriv.packet.PGPSignaturePacket;
import org.cyberplat.ipriv.packet.PGPSecretKeyPacket;
import org.cyberplat.ipriv.data.IPrivSerial;
import org.cyberplat.ipriv.data.BigIntegerImage;


import java.util.Hashtable;
import java.util.Vector;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.DataInputStream;
import java.io.ByteArrayInputStream;

import com.batyrov.ps.util.Utils;
import com.batyrov.ps.bean.Key;
import com.batyrov.ps.Dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

/**
 * Created by IntelliJ IDEA.
 * User: sulic
 * Date: 21.02.2008
 * Time: 22:25:06
 * To change this template use File | Settings | File Templates.
 */
public class IPriv {

    private boolean VERIFY_SELF_SIGNED = false;
    private String secretFileName = "c:\\ps\\secret.key";
    private String publicFileName = "c:\\ps\\pubkeys.key";

    private byte[] password = getDigest("tester");

    private SecretKeyInfo secretKeyInfo = null;

    private PublicKeyInfo publicKeyInfo = null;

    private String ownerKeyNumber;
    private IPrivSerial message = null;

    private static IPriv _kernel;

    private Hashtable publicKeyInfoTable = new Hashtable();


    public static IPriv getInstance() throws IOException {
        if (_kernel == null)
            _kernel = new IPriv();
        return _kernel;
    }

    private IPriv() throws IOException {
        // Загружаем приватный ключ из файла
        byte[] secretKey = getKeyData(secretFileName);
        // Загружаем публичный ключ из файла
        byte[] publicKey = getKeyData(publicFileName);

        SecretKeyInfo ski = getSecretKeyInfo(secretKey, password);
        if (ski == null)
            throw new IOException("ski is null");
        PublicKeyInfo pki = getPublicKeyInfo(publicKey, password);
        if (pki == null)
            throw new IOException("pki is null");
        publicKeyInfoTable.put(pki.getKeyNumber(), pki);
    }

    public IPrivSerial checkSignature(String inputmessage) throws IOException{
        IPrivSerial ips = getIPrivSerial(inputmessage.getBytes(), password);
        long id = Utils.getLong(ips.getOwnerKeyNumber());
        Key key = Dao.getEntityManager().find(Key.class, id);
        if (key == null) {
            throw new IOException("key is null");
        }
        byte[] b = key.getData();
        PublicKeyInfo pki = getPublicKeyInfo(b, password);
        if (pki == null)
            throw new IOException("pki is null");
        if (ips.getOwnerKeyNumber() == null)
            throw new IOException("owner key number is null");
        publicKeyInfoTable.put(ips.getOwnerKeyNumber(), pki);
        if (checkSignature(ips)) {
            return ips;
        }else{
            return null;
        }
    }

    public long processSignedMessage(String inputmessage) throws Exception {
        IPrivSerial ips = getIPrivSerial(inputmessage.getBytes(), password);
        long id = Utils.getLong(ips.getOwnerKeyNumber());
        Key key = Dao.getEntityManager().find(Key.class, id);
        if (key == null) {
            throw new IOException("key is null");
        }
        byte[] b = key.getData();
        if (b == null)
            throw new IOException("key body is null");
        PublicKeyInfo pki = getPublicKeyInfo(b, password);
        if (pki == null)
            throw new IOException("pki is null");
        if (ips.getOwnerKeyNumber() == null)
            throw new IOException("owner key number is null");
        publicKeyInfoTable.put(ips.getOwnerKeyNumber(), pki);
        if (checkSignature(ips)) {
            return pki.keyId;
        }else{
            return -1;
        }
    }

    /**
     * Данный метод читает ключ из файла, запихивает его в массив байтов и возвращает
     * Если в файле нечего прочесть, тогде возвращается массив пустой
     */

    private static byte[] getKeyData(String fileName) throws IOException {
        DataInputStream dis = new DataInputStream(new FileInputStream(fileName));
        if (dis.available() > 0) {
            byte[] b = new byte[dis.available()];
            dis.read(b);
            return b;
        } else {
            return new byte[0];
        }
    }


    public IPrivSerial getIPrivSerial(byte[] data, byte[] digest) throws IOException {
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
        return new IPrivSerial(dis, digest, publicKeyInfoTable);
    }

    public SecretKeyInfo getSecretKeyInfo(byte[] data, byte[] digest) throws IOException {
        IPrivSerial ips = getIPrivSerial(data, digest);
        PGPSecretKeyPacket sk = ips.getSecretKeyPacket();
        if (sk == null)
            throw new IOException("1010");
        SecretKeyInfo secretKeyInfo = sk.getInfo();
        secretKeyInfo.setSecretText(data);
        return secretKeyInfo;
    }

    public PublicKeyInfo getPublicKeyInfo(byte[] data, byte[] digest) throws IOException {
        IPrivSerial ips = getIPrivSerial(data, digest);
        PGPPublicKeyPacket pk = ips.getPublicKeyPacket();
        if (pk == null)
            throw new IOException("1010");
        PublicKeyInfo publicKeyInfo = pk.getInfo();
        publicKeyInfo.setText(data);
        return publicKeyInfo;
    }


    public boolean checkSignature(IPrivSerial sis) throws IOException {
        try {
            if (!sis.isSignedMessage())
                return false;

            PGPSignaturePacket sp = sis.getSignaturePacket();

            if (sp == null)
                return false;
            PublicKeyInfo signerPublicKey = (PublicKeyInfo) publicKeyInfoTable.get(sis.getOwnerKeyNumber());
            //PublicKeyInfo signerPublicKey = terminalPublicKeyInfo;
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
            throw new IOException("1015");

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

    // returns signed document
    public byte[] sign(byte[] data, int start, int ndata) throws IOException {
        SignatureInfo si = signData(data, start, ndata);
        IPrivSerial p = new IPrivSerial(si, secretKeyInfo.owner, ownerKeyNumber, data, start, ndata);
        Vector v = p.getOutVector();
        if (v == null)
            throw new IOException("1017");//IPriv.sign: error in resulting vector");
        return lib.VectorToByteArray(v);
    }

    public static byte[] getDigest(String s) {
        return IPrivIDEACipher.getDigest(ConvertText.toWin1251(s));
    }

    public static void main(String[] args) {

        try {
            /*byte [] b = getKeyData("c:\\ps\\tttt");
            EntityManager em =  Dao.getEntityManager();
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            Key key = em.find(Key.class, 1L);
            key.setData(b);
            tx.commit();*/
            byte[] b = getKeyData("c:\\ps\\test");
            System.out.println(IPriv.getInstance().processSignedMessage(new String(b)));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
