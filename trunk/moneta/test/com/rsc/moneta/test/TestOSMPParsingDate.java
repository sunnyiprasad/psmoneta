/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsc.moneta.test;

import com.rsc.moneta.module.inputhandler.OSMPInputHandler;
import java.text.ParseException;
import java.util.Date;
import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author sulic
 */
public class TestOSMPParsingDate {
    @Test
    public void testdateParse() throws ParseException{
        OSMPInputHandler handler = new OSMPInputHandler();
        Date date  = handler.convertOSMPDateTimeToDateTime("20100421113029");
        Assert.assertNotNull(date);
        System.out.println(date);
    }
}
