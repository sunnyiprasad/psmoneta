/*
 * UserIdInfo.java
 *
 * Created on 5 ������� 2005 �., 12:38
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

import org.cyberplat.util.*;

/**
 *
 * @author  Shutov
 */
public class UserIdInfo {
    public byte[] data = null;
    /** Creates a new instance of UserIdInfo */
    public UserIdInfo() {
    }
    /** Creates a new instance of UserIdInfo */
    public UserIdInfo(byte [] arr, int start, int size) {
        data = new byte[size];
        System.arraycopy(arr, start, data, 0, size);
    }
    /** Creates a new instance of UserIdInfo */
    public UserIdInfo(String id) {
        data = ConvertText.toWin1251(id);
    }
}