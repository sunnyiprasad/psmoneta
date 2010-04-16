/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsc.moneta.module.inputhandler;

/**
 *
 * @author sulic
 */
public class InputHandlerConfig {
    private String login;
    private String password;
    private String ip;
    private String handlerClass;
    private String pem;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setHandler(String handlerClass) {
        this.handlerClass = handlerClass;
    }

    public void setPem(String pem) {
        this.pem = pem;
    }



}
