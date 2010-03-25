package com.batyrov.ps.util;

import java.util.List;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: sulic
 * Date: 19.03.2008
 * Time: 22:16:29
 * To change this template use File | Settings | File Templates.
 */
public class DealerUserType {
    private static String[] dealerUserTypeValues = {"Администратор", "Оператор"};
    private static int [] dealerUserTypeKeys = {0 , 1};
    private String value;
    private int key;

    public static List getUserTypes(){
        List list  = new Vector();
        for (int i = 0; i<dealerUserTypeValues.length;i++){
            DealerUserType m = new DealerUserType();
            m.key =  dealerUserTypeKeys[i];
            m.value = dealerUserTypeValues[i];
            list.add(m);
        }
        return list;
    }


    public static String[] getDealerUserTypeValues() {
        return dealerUserTypeValues;
    }

    public static void setDealerUserTypeValues(String[] dealerUserTypeValues) {
        DealerUserType.dealerUserTypeValues = dealerUserTypeValues;
    }

    public static int[] getDealerUserTypeKeys() {
        return dealerUserTypeKeys;
    }

    public static void setDealerUserTypeKeys(int[] dealerUserTypeKeys) {
        DealerUserType.dealerUserTypeKeys = dealerUserTypeKeys;
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
