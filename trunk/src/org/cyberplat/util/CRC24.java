/*
 * CRC24.java
 *
 * Created on 20 Декабрь 2005 г., 11:36
 */

package org.cyberplat.util;

/**
 *
 * @author  Shutov
 */
public class CRC24 {
    private static int CRC24_INIT = 0x0b704ce;
    private static int CRC24_POLY = 0x1864cfb;
    
    private int crc = CRC24_INIT;
    
    public CRC24() {
    }
    
    public int update(
    int b) {
        crc ^= b << 16;
        for (int i = 0; i < 8; i++) {
            crc <<= 1;
            if ((crc & 0x1000000) != 0) {
                crc ^= CRC24_POLY;
            }
        }
        return crc;
    }
    
    public int update(byte[] b) {
        for (int i = 0; i < b.length; i++)
            update(b[i]);
        return crc;
    }
    
    public int update(byte[] b, int off, int len) {
        int end = off + len;
        for (int i = off; i < end; i++)
            update(b[i]);
        return crc;
    }
    
    
    public int getValue() {
        return crc;
    }
    
    public int getValue24() {
        return crc & 0x00FFFFFF;
    }
    
    public void reset() {
        crc = CRC24_INIT;
    }
    
}