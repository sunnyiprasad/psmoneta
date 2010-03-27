package com.rsc.moneta.util;

import java.util.List;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: sulic
 * Date: 28.06.2007
 * Time: 0:10:09
 * To change this template use File | Settings | File Templates.
 */
public class Role {

    private static String [] roleNames = {"������", "��������", "�������������"};
    private static int [] roleNumber = {0, 1, 2};

    public static List getRoles(){
        List list  = new Vector();
        for (int i = 0; i<roleNames.length;i++){
            Role m = new Role();
            m.name =  roleNames[i];
            m.number = roleNumber[i];
            list.add(m);
        }
        return list;
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
