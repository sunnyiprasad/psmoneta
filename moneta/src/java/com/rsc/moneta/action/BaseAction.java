package com.rsc.moneta.action;


import com.opensymphony.xwork2.ActionSupport;
import com.rsc.moneta.bean.User;
import com.rsc.moneta.bean.Provider;
import java.util.Collection;
import javax.servlet.ServletContext;
import org.apache.struts2.interceptor.SessionAware;

import javax.persistence.EntityManager;
import java.util.Map;
import org.apache.struts2.util.ServletContextAware;

/**
 * Created by IntelliJ IDEA.
 * User: sulic
 * Date: 06.06.2007
 * Time: 21:36:03
 * To change this template use File | Settings | File Templates.
 */
public class BaseAction  extends ActionSupport implements SessionAware, ServletContextAware {
    protected Map session = null;
    protected EntityManager em;
    protected ServletContext servletContext;
    protected User user;
    protected Collection<Provider> providerList;

    public BaseAction() {

    }

    public Collection<Provider> getProviderList() {
        return providerList;
    }

    public void setProviderList(Collection<Provider> providerList) {
        this.providerList = providerList;
    }

    public Map getSession() {
        return session;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }



    public void setSession(Map map) {
        session = map;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public EntityManager getEm() {
        return em;
    }

    public void setServletContext(ServletContext sc) {
        this.servletContext = sc;
    }

}
