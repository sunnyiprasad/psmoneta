package com.batyrov.ps.util;

import java.util.List;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: sulic
 * Date: 19.03.2008
 * Time: 22:17:46
 * To change this template use File | Settings | File Templates.
 */
public class PointType {
    private static String[] pointTypeValues = {"API", "WEB"};
    private static int [] pointTypeKeys = {0 , 1};
    private String value;
    private int key;

    public static List getPointTypes(){
        List list  = new Vector();
        for (int i = 0; i<pointTypeValues.length;i++){
            PointType m = new PointType();
            m.key =  pointTypeKeys[i];
            m.value = pointTypeValues[i];
            list.add(m);
        }
        return list;
    }

    public static String[] getPointTypeValues() {
        return pointTypeValues;
    }

    public static void setPointTypeValues(String[] pointTypeValues) {
        PointType.pointTypeValues = pointTypeValues;
    }

    public static int[] getPointTypeKeys() {
        return pointTypeKeys;
    }

    public static void setPointTypeKeys(int[] pointTypeKeys) {
        PointType.pointTypeKeys = pointTypeKeys;
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
