package com.batyrov.ps.util;

import java.util.List;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: sulic
 * Date: 16.03.2008
 * Time: 19:21:37
 * To change this template use File | Settings | File Templates.
 */
public class CityType {

    private static String[] cityTypeValues = {"Город", "Деревня", "Село", "Поселок", "Поселок городского типа", "Рабочий (заводской) поселок", "Курортный поселок", "Дачный поселок", "Сельсовет", "Сельская администрация", "Сельское муницип. образование", "Сельский округ", "Волость", "Почтовое отделение", "Территория", "Сельская территория", "Станица"};
    private static String[] cityTypeKeys = {"г", "д", "с", "п", "пгт", "рп", "кп", "дп", "с/с", "с/а", "с/мо", "с/о", "волость", "п/о", "тер", "с/т", "ст-ца"};

    private String value;
    private String key;

    public static List getCityTypes(){
        List list  = new Vector();
        for (int i = 0; i<cityTypeValues.length;i++){
            CityType m = new CityType();
            m.key =  cityTypeKeys[i];
            m.value = cityTypeValues[i];
            list.add(m);
        }
        return list;
    }


    public static String[] getCityTypeKeys() {
        return cityTypeKeys;
    }

    public static void setCityTypeKeys(String[] cityTypeKeys) {
        CityType.cityTypeKeys = cityTypeKeys;
    }

    public static String[] getCityTypeValues() {
        return cityTypeValues;
    }

    public static void setCityTypeValues(String[] cityTypeValues) {
        CityType.cityTypeValues = cityTypeValues;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
