package com.rsc.moneta.test;


import com.rsc.moneta.util.Utils;
import java.security.NoSuchAlgorithmException;
import org.junit.Assert;
import org.junit.Test;



/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author sulic
 */
public class TestMd5Signature {

    @Test
    public void testCalcMd5AndGetHexString() throws NoSuchAlgorithmException{
        String signature = "c8222aef6362c7f1239ccdc729d1a200";
        Assert.assertTrue(signature.equalsIgnoreCase(Utils.getMd5InHexString("54600817FF790ABCD120.25RUB0QWERTY")));
    }

}
