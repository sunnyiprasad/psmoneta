/*
 * Radix64.java
 *
 * Created on 20 Декабрь 2005 г., 11:23
 */

package org.cyberplat.util;

/**
 *
 * @author  Shutov
 */
public class Radix64 {
    public CRC24 crc = new CRC24();
    
    private final static int RADIX64_MAX_LINE_LENGTH = 64;
    private final static byte[] bin2ascii =
    { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
      'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
      'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/' };
      
      private final static byte[] ascii2bin = {
          (byte)0x80, (byte)0x80, (byte)0x80, (byte)0x80, (byte)0x80, (byte)0x80, (byte)0x80, (byte)0x80,
          (byte)0x80, (byte)0x80, (byte)0x80, (byte)0x80, (byte)0x80, (byte)0x80, (byte)0x80, (byte)0x80,
          (byte)0x80, (byte)0x80, (byte)0x80, (byte)0x80, (byte)0x80, (byte)0x80, (byte)0x80, (byte)0x80,
          (byte)0x80, (byte)0x80, (byte)0x80, (byte)0x80, (byte)0x80, (byte)0x80, (byte)0x80, (byte)0x80,
          (byte)0x80, (byte)0x80, (byte)0x80, (byte)0x80, (byte)0x80, (byte)0x80, (byte)0x80, (byte)0x80,
          (byte)0x80, (byte)0x80, (byte)0x80, (byte)0x3e, (byte)0x80, (byte)0x80, (byte)0x80, (byte)0x3f,
          (byte)0x34, (byte)0x35, (byte)0x36, (byte)0x37, (byte)0x38, (byte)0x39, (byte)0x3a, (byte)0x3b,
          (byte)0x3c, (byte)0x3d, (byte)0x80, (byte)0x80, (byte)0x80, (byte)0x80, (byte)0x80, (byte)0x80,
          (byte)0x80, (byte)0x00, (byte)0x01, (byte)0x02, (byte)0x03, (byte)0x04, (byte)0x05, (byte)0x06,
          (byte)0x07, (byte)0x08, (byte)0x09, (byte)0x0a, (byte)0x0b, (byte)0x0c, (byte)0x0d, (byte)0x0e,
          (byte)0x0f, (byte)0x10, (byte)0x11, (byte)0x12, (byte)0x13, (byte)0x14, (byte)0x15, (byte)0x16,
          (byte)0x17, (byte)0x18, (byte)0x19, (byte)0x80, (byte)0x80, (byte)0x80, (byte)0x80, (byte)0x80,
          (byte)0x80, (byte)0x1a, (byte)0x1b, (byte)0x1c, (byte)0x1d, (byte)0x1e, (byte)0x1f, (byte)0x20,
          (byte)0x21, (byte)0x22, (byte)0x23, (byte)0x24, (byte)0x25, (byte)0x26, (byte)0x27, (byte)0x28,
          (byte)0x29, (byte)0x2a, (byte)0x2b, (byte)0x2c, (byte)0x2d, (byte)0x2e, (byte)0x2f, (byte)0x30,
          (byte)0x31, (byte)0x32, (byte)0x33, (byte)0x80, (byte)0x80, (byte)0x80, (byte)0x80, (byte)0x80
      };
      
      /** Creates a new instance of Radix64 */
      public Radix64() {
      }
      
      private static int enc_block(byte[] src, int sOff, int nsrc, byte[] dst, int dOff) {
          dst[dOff + 0] = bin2ascii[ (src[sOff + 0] >> 2) & 0x3f];
          if (nsrc > 2) {
              dst[dOff + 1] = bin2ascii[((src[sOff + 0] << 4) & 0x30) | ((src[sOff + 1] >> 4) & 0x0f)];
              dst[dOff + 2] = bin2ascii[((src[sOff + 1] << 2) & 0x3c) | ((src[sOff + 2] >> 6) & 0x03)];
              dst[dOff + 3] = bin2ascii[src[sOff + 2] & 0x3f];
          } else if (nsrc > 1) {
              dst[dOff + 1] = bin2ascii[((src[sOff + 0] << 4) & 0x30) | ((src[sOff + 1] >> 4) & 0x0f)];
              dst[dOff + 2] = bin2ascii[(src[sOff + 1] << 2) & 0x3c];
              dst[dOff + 3] = '=';
          }else if(nsrc > 0) {
              dst[dOff + 1] = bin2ascii[(src[sOff + 0] << 4) & 0x30];
              dst[dOff + 2] = '=';
              dst[dOff + 3] = '=';
          }
          return 4;
      }
      
      
      public int encode(byte[] src, byte[] dst) {
          int nsrc = src.length;
          int ndst = dst.length;
          crc.reset();
          
          for (int i = 0; i < ndst; i++)
              dst[i] = 0;
          
          int rc = ndst;
          int x = 0;
          int s = 0;
          int d = 0;
          
          while (nsrc > 2) {
              if (ndst < 4)
                  return -1;
              
              enc_block(src, s, nsrc, dst, d);
              crc.update(src, s, 3);
              s += 3;
              nsrc -= 3;
              d += 4;
              ndst -= 4;
              x += 4;
              if( RADIX64_MAX_LINE_LENGTH == x) {
                  if (ndst < 2)
                      return -1;
                  dst[d++] = '\r';
                  dst[d++] = '\n';
                  ndst -= 2;
                  x = 0;
              }
          }
          
          if (nsrc > 0) {
              x += 4;
              enc_block(src, s, nsrc, dst, d);
              crc.update(src, s, nsrc);
              s += 3;
              nsrc -= 3;
              d += 4;
              ndst -= 4;
          }
          
          if (x != 0) {
              if(ndst < 2)
                  return -1;
              dst[d++] = '\r';
              dst[d++] = '\n';
              ndst -= 2;
          }
          
          if (src.length == 0) {
              dst[d++] = '\r';
              dst[d++] = '\n';
              ndst -= 2;
          }
          //    if(ndst<8)
          
          if (ndst < 3)
              return -1;
          
          dst[d++] = '=';
          ndst--;
          enc_block(lib.fillUnsigned3(crc.getValue24(), null, 0), 0, 3, dst, d);
          d += 4;
          ndst -= 4;
          return rc - ndst;
      }
      
      
      private static int dec_block(byte[] src,int sOff, int nsrc, byte[] dst, int dOff) {
          byte src2 = ((sOff + 2) < nsrc)?(src[sOff + 2]):(0);
          byte src3 = ((sOff + 3) < nsrc)?(src[sOff + 3]):(0);
          
          byte b1 = ascii2bin[src[sOff + 0]];
          byte b2 = ascii2bin[src[sOff + 1]];
          
          byte b3 = ((sOff + 2) < src.length)?(ascii2bin[src2]):(0);
          byte b4 = ((sOff + 3) < src.length)?(ascii2bin[src3]):(0);
          
          if (src2 == '=') {
              if ((b1 & 0x80) != 0 || (b2 & 0x80) != 0)
                  return -1;
              dst[dOff + 0] = (byte)(((b1 << 2) & 0xfc) | ((b2 >> 4) & 0x03));
              return 1;
          }else if (src3 == '=') {
              if((b1 & 0x80) != 0 || (b2 & 0x80) != 0 || (b3 & 0x80) != 0)
                  return -1;
              dst[dOff + 0] = (byte)(((b1 << 2) & 0xfc) | ((b2 >> 4) & 0x03));
              dst[dOff + 1] = (byte)(((b2 << 4) & 0xf0) | ((b3 >> 2) & 0x0f));
              return 2;
          }
          
          if((b1 & 0x80) != 0 || (b2 & 0x80) != 0 || (b3 & 0x80) != 0 || (b4 & 0x80) != 0)
              return -1;
          
          dst[dOff + 0] = (byte)(((b1 << 2) & 0xfc) | ((b2 >> 4) & 0x03));
          dst[dOff + 1] = (byte)(((b2 << 4) & 0xf0) | ((b3 >> 2) & 0x0f));
          dst[dOff + 2] = (byte)(((b3 << 6) & 0xc0) | (b4 & 0x3f));
          
          return 3;
      }
      
      public int decode(byte[] src, int src_length,  byte[] dst) {
          
          int nsrc = src.length;
          int ndst = dst.length;
          crc.reset();
          
          for (int i = 0; i < ndst; i++)
              dst[i] = 0;
          
          int rc = ndst;
          int d = 0;
          int s = 0;
          
          byte[] crcbuf = new byte[4];
          int ncrcbuf = 0;
          
          boolean newline = true;
          int iscrc = 0;
          
          byte[] data = new byte[4];
          int ndata = 0;
          
          for( int i = 0; i < nsrc; i++) {
              int si = s + i;
              if ((src[si] & 0x80) != 0)
                  return -1;
              
              if (src[si] == (byte)' ' ||
              src[si] == (byte)'\t' ||
              src[si] == (byte)'\r')
                  continue;
              if(src[si] == (byte)'\n') {
                  newline = true;
                  continue;
              }
              
              if (iscrc != 0) {
                  if (newline)
                      break;
                  if( ncrcbuf >= crcbuf.length )
                      return -1;
                  crcbuf[ncrcbuf++] = src[si];
                  continue;
              }
              
              if (newline) {
                  newline = false;
                  if (src[si] == '=') {
                      iscrc = 1;
                      continue;
                  }
              }
              
              data[ndata++] = src[si];
              if( ndata >= data.length) {
                  if (ndst < 1)
                      return -1;
                  int n = dec_block(data, 0, data.length, dst, d);
                  if ( n <= 0)
                      return -1;
                  crc.update(dst, d, n);
                  d += n;
                  ndst -= n;
                  ndata = 0;
              }
          }
          
          if( ndata > 0) {
              if ( ndata != data.length)
                  return -1;
              
              if( ndst < 3)
                  return -1;
              int n = dec_block(data, 0, data.length, dst, d);
              if (n <= 0)
                  return -1;
              crc.update(dst, d, n);
              d += n;
              ndst -= n;
          }
          
          if (ncrcbuf != crcbuf.length)
              return -1;
          
          byte[] inCrc = new byte[3];
          
          dec_block(crcbuf, 0, crcbuf.length, inCrc, 0);
          
          if (crc.getValue24() != lib.unsigned3(inCrc, 0))
              return -1;
          
          return rc-ndst;
      }

      public byte [] getCheckSum(byte [] in){
          crc.reset();
          crc.update(in);
          byte [] dst = new byte[4];
          enc_block(lib.fillUnsigned3(crc.getValue24(), null, 0), 0, 3, dst, 0);
          crc.reset();
          return dst;
      }

      public static int parseCRC24(byte[] in, int start) {
          byte[] input = new byte[4];
          System.arraycopy(in, start, input, 0, 4);
          byte[] result = new byte[3];
          dec_block(input, 0, 4, result, 0);
          return lib.unsigned3(result, 0);
      }
}
