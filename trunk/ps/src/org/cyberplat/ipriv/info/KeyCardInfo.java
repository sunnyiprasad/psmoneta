/*
 * KeyCardInfo.java
 *
 * Created on 23 январь 2006 г., 14:32
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


package org.cyberplat.ipriv.info;
import java.io.IOException;

import org.cyberplat.util.*;
import org.cyberplat.util.Radix64;

// Input file format:
// Version: 01
// User ID: 0m1024
// User Key: 73995
// =V89J


/**
 *
 * @author  Shutov
 */
public class KeyCardInfo {
    public static int USE_VERSION = 1;
    
    public static String VERSION = "Version";
    
    public static String USER_ID = "User ID";
    public static String USER_KEY = "User Key";
    
    public int    version = 0;
    public long   keyId = 0;
    public String userId = "";
    
    String image = null;
    // User ID: 0m1024
    // User Key: 73995
    /** Creates a new instance of KeyCardInfo */
    public KeyCardInfo() {
    }
    
    public void parse(byte[] in) throws IOException {
        image = ConvertText.toUnicode(in);
        int start = 0;
        String id = "";
        String value = "";
        try {
            for (int i = 0; i < 3; i++) {
                int p = image.indexOf(":", start);
                if (p == -1)
                    throw new IOException(Integer.toString(401));//Bad key card format - ':' not found .");
                
                int rn = image.indexOf("\r\n", p);
                if (rn == -1)
                    throw new IOException(Integer.toString(402));//Bad key card format - end of line not found.");
                
                id = image.substring(start, p).trim();
                value = image.substring(p + 1, rn).trim();
                start = rn + 2;
                
                if (id.equals(VERSION))
                    version = Integer.parseInt(value);
                else if (id.equals(USER_ID))
                    userId = value;
                else if (id.equals(USER_KEY))
                    keyId = Long.parseLong(value);
            }
        } catch (java.lang.NumberFormatException e) {
            throw new IOException(Integer.toString(403));//Bad key card format - " + e.getMessage());
        }
        if (version !=  USE_VERSION)
            throw new IOException(Integer.toString(404));//Bad key card format - incorrect version.");
        if (userId.equals(""))
            throw new IOException(Integer.toString(405));//Bad key card format - missed user id.");
        if (keyId == 0)
            throw new IOException(Integer.toString(406));//Bad key card format - bad key id.");
        
        if (image.charAt(start) != '=')
            throw new IOException(Integer.toString(407));//Bad key card format - missing check summa.");
        
        CRC24 crc = new CRC24();
        crc.update(in, 0, start);
        if (Radix64.parseCRC24(in, ++start) != crc.getValue24())
            throw new IOException(Integer.toString(408));//Bad key card format - bad check summa.");
    }
    public String toString() {
        return image;
    }
}
