package com.rsc.moneta.util;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

/**
 * User: Suleyman Batyrov
 * Date: 25.01.2007
 * Time: 17:25:36
 */
public class Month {

    private static int[] monthNumber = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};

    public static List getMonths(Locale locale) {
        try {
            String[] monthNames = new String[12];
            ResourceBundle bundle = ResourceBundle.getBundle("month", locale);
            for (int i = 1; i < 12; i++) {
                monthNames[i - 1] = bundle.getString(i + "");
            }
            List list = new Vector();
            for (int i = 0; i < monthNames.length; i++) {
                Month m = new Month();
                m.name = monthNames[i];
                m.number = monthNumber[i];
                list.add(m);
            }
            return list;
        } catch (Exception exception) {
            exception.printStackTrace();
            return new Vector();
        }
    }
    private String name;
    private int number;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
