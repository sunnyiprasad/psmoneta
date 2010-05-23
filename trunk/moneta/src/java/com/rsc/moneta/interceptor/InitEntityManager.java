/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsc.moneta.interceptor;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;
import com.rsc.moneta.Config;
import com.rsc.moneta.action.BaseAction;
import com.rsc.moneta.bean.User;
import com.rsc.moneta.dao.EMF;
import com.rsc.moneta.bean.Provider;
import java.util.Collection;

/**
 *
 * @author sulic
 */
public class InitEntityManager implements Interceptor {

    /**
     * 
     */
    public void destroy() {
    }

    /**
     *
     */
    public void init() {
    }

    /**
     *
     * @param actionInvocation
     * @return
     * @throws Exception
     */
    public String intercept(ActionInvocation actionInvocation) throws Exception {
        if (actionInvocation.getAction() instanceof BaseAction) {
            BaseAction action = (BaseAction) actionInvocation.getAction();
            Object obj = action.getSession().get("user");
            if (obj != null) {
                action.setUser((User) obj);
            }
            Collection<Provider> providers = Config.getProviderList();
            action.setProviderList(providers);
            action.setEm(EMF.getEntityManager());
        }
        return actionInvocation.invoke();
    }
}
