package com.rsc.moneta.test;

import com.rsc.moneta.Config;

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
        Config.addOutputHandler(0, "com.rsc.moneta.module.MonetaOutputHandler");
    }
}
