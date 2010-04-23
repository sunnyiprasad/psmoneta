package com.rsc.moneta.test;

import com.rsc.moneta.Config;
import com.rsc.moneta.bean.Mail;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import org.junit.Test;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author sulic
 */
public class TestConf {
    static String serverUrl = "http://localhost:8084/moneta";
    public static void initConfig(){
        Config.addOutputHandler(0, "com.rsc.moneta.module.outputhandler.TaisOutputHandler");
    }

    @Test
    public void testMail() throws NoSuchProviderException, MessagingException{
        Mail mail = new Mail("suleyman.batyrov@gmail.com", "testsubject", "test");
        mail.start();
    }
}
