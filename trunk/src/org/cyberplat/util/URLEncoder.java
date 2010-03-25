/*
 * URLCoder.java
 *
 * Created on 6 Февраль 2006 г., 16:33
 */

package org.cyberplat.util;

import java.io.*;
import org.bouncycastle.util.encoders.Encoder;
/**
 *
 * @author  Shutov
 */
public class URLEncoder implements org.bouncycastle.util.encoders.Encoder {
    private static final String forbiddenCharacters = "?= ()&@\r\n\t\\/|+";
/*
'?' => %3F
'=' => %3D
' ' => %20
'(' => %28
')' => %29
'&' => %26
'@' => %40
 */
    
    /** Creates a new instance of URLCoder */
    public URLEncoder() {
    }
    
    public int decode(String data, OutputStream out) throws IOException {
        //return decode(data.getBytes(), 0, data.length(), out);
        byte[] d = org.cyberplat.util.ConvertText.toWin1251(data);
        return decode(d, 0, d.length, out);
    }
    
    public int decode(byte[] data, int off, int length, OutputStream out) throws IOException {
        int count = 0;
        ByteArrayInputStream b = new ByteArrayInputStream(data, off, length);
        byte[] c = new byte[]{0, 0};
        boolean skeep = false;
        while (b.available() > 0) {
            if (!skeep)
            {
                c[0] = (byte)b.read();
            }
            count++;
            skeep = false;
            switch (c[0]) {
                case '%':
                    if (b.available() < 1) {
                        out.write('%');
                        break;
                    }
                    try {
                        c[0] = (byte)b.read();
                        count++;
                        if (lib.isHex(c[0])) {
                            if (b.available() < 1) {
                                c[1] = c[0];
                                c[0] = '0';
                                out.write(Integer.parseInt(org.cyberplat.util.ConvertText.toUnicode(c), 16));
                                skeep = true;
                                c[0] = -1;
                            }  else {
                                c[1] = (byte)b.read();
                                count++;
                                if (lib.isHex(c[1])) {
                                    count++;
                                    out.write(Integer.parseInt(org.cyberplat.util.ConvertText.toUnicode(c), 16));
                                }  else {
                                    byte c1 = c[1];
                                    c[1] = c[0];
                                    c[0] = '0';
                                    out.write(Integer.parseInt(org.cyberplat.util.ConvertText.toUnicode(c), 16));
                                    skeep = true;
                                    c[0] = c1;
                                }
                            }
                        } else
                            out.write(c[0]);
                    } catch (Exception e) {
                        throw new IOException(e.getMessage() + " in urldecode");
                    }
                    break;
                case '+':
                    out.write(' ');
                    break;
                default:
                    out.write(c[0]);
            }
        }
        return count;
    }
    
    public int encode(byte[] data, int off, int length, OutputStream out) throws IOException {
        int count = 0;
        ByteArrayInputStream b = new ByteArrayInputStream(data, off, length);
        int s;
        for (byte c; b.available() > 0; count++) {
            c = (byte)b.read();
            if (c < 0 || forbiddenCharacters.indexOf(c) != -1) {
                out.write((byte)'%');
                s = (int)(c & 0xFF);
                if (s < 16)
                    out.write((byte)'0');
                out.write(Integer.toHexString(s).getBytes());
                count += 2;
            } else {
                out.write(c & 0xFF);
            }
        }
        return count;
    }
    
    public int encode(String data, OutputStream out) throws IOException {
        return encode(org.cyberplat.util.ConvertText.toWin1251(data), 0, data.length(), out);
    }
}
