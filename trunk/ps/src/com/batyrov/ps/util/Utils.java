package com.batyrov.ps.util;

/**
 * Created by IntelliJ IDEA.
 * User: sulic
 * Date: 09.03.2008
 * Time: 15:57:05
 * To change this template use File | Settings | File Templates.
 */
public class Utils {
    public static int getInt(String s){
        try{
            return Integer.parseInt(s);
        }catch (NumberFormatException e){
            return -1;
        }
    }

    public static long getLong(String s){
        try{
            return Long.parseLong(s);
        }catch (NumberFormatException e){
            return -1;
        }
    }
}
