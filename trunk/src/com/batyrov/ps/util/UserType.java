package com.batyrov.ps.util;

import java.util.List;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: sulic
 * Date: 19.03.2008
 * Time: 22:16:20
 * To change this template use File | Settings | File Templates.
 */
public class UserType {
    private static String[] userTypeValues = {"Администратор", "Оператор"};
    private static int [] userTypeKeys = {0 , 1};
    private String value;
    private int key;

    public static List getUserTypes(){
        List list  = new Vector();
        for (int i = 0; i<userTypeValues.length;i++){
            UserType m = new UserType();
            m.key =  userTypeKeys[i];
            m.value = userTypeValues[i];
            list.add(m);
        }
        return list;
    }

    public static String[] getUserTypeValues() {
        return userTypeValues;
    }

    public static void setUserTypeValues(String[] userTypeValues) {
        UserType.userTypeValues = userTypeValues;
    }

    public static int[] getUserTypeKeys() {
        return userTypeKeys;
    }

    public static void setUserTypeKeys(int[] userTypeKeys) {
        UserType.userTypeKeys = userTypeKeys;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }
}
