/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsc.moneta.test.module;

import com.rsc.moneta.module.inputhandler.OSMPInputHandler;
import java.util.Map;
import java.util.Properties;
import junit.framework.Assert;
import org.junit.Test;


/**
 *
 * @author sulic
 */
public class TestOsmpHandler {

    @Test
    public void testOsmpCheck1(){
        OSMPInputHandler handler = new OSMPInputHandler();
        Map map = new Properties();
//     command=check&txn_id=1234567&account=4957835959&sum=10.45
        map.put("command", "check");
        map.put("txn_id", "1234567");
        map.put("account", "0000000000000002222");
        map.put("sum", "10.45");
        String xml = handler.check(map);
        Assert.assertEquals("<?xml version='1.0' encoding='UTF-8'?><response><osmp_txn_id>1234567</osmp_txn_id><prv_txn>0000000000000002222</prv_txn><result>0</result><comment>Этот платеж уже завершён</comment></response>",
                xml);
    }
}
