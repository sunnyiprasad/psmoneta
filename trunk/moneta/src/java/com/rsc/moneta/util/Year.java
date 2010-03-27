package com.rsc.moneta.util;

import java.util.List;
import java.util.Vector;

/**
 * User: Suleyman Batyrov
 * Date: 25.01.2007
 * Time: 17:33:56
 */
public class Year {
    private static String [] yearNames = {"2007", "2008", "2009", "2010"};
    private String name = null;
    private int number = -1;



    public static List getYears(){
        List list  = new Vector();
        for (int i = 0; i<yearNames.length;i++){
            Year m = new Year();
            m.name =  yearNames[i];
            m.number = Integer.parseInt(yearNames[i]);
            list.add(m);
        }
        return list;
    }


    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getYearNames() {
        return yearNames;
    }

    public void setYearNames(String[] yearNames) {
        this.yearNames = yearNames;
    }
}
