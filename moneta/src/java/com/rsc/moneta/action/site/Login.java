/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsc.moneta.action.site;

import com.opensymphony.xwork2.Action;
import com.rsc.moneta.action.BaseAction;
import com.rsc.moneta.dao.UserDao;
import com.rsc.moneta.bean.User;

/**
 *
 * @author sulic
 */
public class Login extends BaseAction {

    private String email;
    private String password;

    @Override
    public String execute() throws Exception {
        if(email == null || password == null || "".equals(email) || "".equals(password)){
            return Action.LOGIN;
        }
        setUser(new UserDao(em).getUserByEmailAndPassword(email, password));
        if (user == null)
            return "login";
        session.put("user", user);
        switch (user.getRole()){
            case User.USER: return "user";
            case User.IMOWNER: return "user";
            case User.BUHGALTER: return "admin";
            case User.ADMINISTRATOR: return "admin";
            default: return "login";
        }        
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }   

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



}
