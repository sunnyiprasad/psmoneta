/*
 * DataStorage.java
 *
 * Created on 20 январь 2006 г., 14:38
 */

package org.cyberplat.rms;

/**
 *
 * @author  Shutov
 */
import java.io.*;
import java.io.IOException;

import org.bouncycastle.crypto.digests.MD5Digest;

import org.cyberplat.util.ConvertText;

public class DataStorage {

    private int maxAvailable = -1;
    private int allocatedSize = -1;

    protected String rmsName;

    private static String[] names = null;

    private static final boolean USE_PASSWORD_HASH_IN_FILE_NAMES = false;
    private byte[] data = new byte[] {-1};

    static String getRecordName(String userName, String password, String fileName) {
    String result = "";
    if (!USE_PASSWORD_HASH_IN_FILE_NAMES) {
        result =
            userName.substring(0, ((userName.length() > 8)?8:userName.length())) + "-" + fileName;
    } else {
        MD5Digest md5 = new MD5Digest();
        byte [] key = ConvertText.toWin1251(password);
        md5.update(key, 0, key.length);
        byte [] param = new byte[md5.getDigestSize()];
        md5.doFinal(param, 0);
        int hash = 0;
        for(int i = 0; i < param.length; i++)
            hash += param[i];
        param = ConvertText.toWin1251(userName.toLowerCase());
        for(int i = 0; i < param.length; i++)
            hash += param[i];
        hash &= 0x7F;
        result =
            userName.substring(0, ((userName.length() > 8)?8:userName.length())) + "-" + Integer.toHexString(hash) + "-" + fileName;
    }
        //RST: org.cyberplat.util.lib.message("getRecordName:fileName = " + result.toLowerCase());
        return result.toLowerCase();
    }
    
    public DataStorage(String userName, String password, String fileName) {
        rmsName = getRecordName(userName, password, fileName);
    }
    

    public static boolean storageExists(String userName, String password, String fileName) {
        return exists(getRecordName(userName, password, fileName));
    }
    
    public byte[] loadStoredData() throws IOException {
        return new byte[0];
    }
    
    public byte[] getStoredData() {
        return data;
    }
    
    public InputStream getInputStream() {
        return new ByteArrayInputStream(data);
    }
    public void updateStoredData(byte[] data) throws IOException {

    }
    
    protected void loadData() throws IOException {

    }
    
    protected void createDefaultData() throws IOException {

    }
    
    protected void updateData() throws IOException {

    }
    
    public boolean isEmpty()
    {
        return ((data == null) ||
            (data.length == 1 && data[0] == -1));
    }

    public void setSizeAvailable() {

    }

    public int getSizeAvailable() {
        return maxAvailable;
    }

    public int getSizeAllocated() {
        return allocatedSize;
    }

    public static boolean exists(String name) {
        return false;
    }

    public void open() throws IOException {

    }

    public void close() throws IOException {

    }


    public String getRMSName() {
        return rmsName;
    }

    public void delete() throws IOException {

    }
}
