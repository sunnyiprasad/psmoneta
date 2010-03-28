/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsc.moneta.interceptor;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;
import com.rsc.moneta.action.BaseAction;
import com.rsc.moneta.dao.EMF;

/**
 *
 * @author sulic
 */
public class InitEntityManager implements Interceptor{

    public void destroy() {

    }

    public void init() {
        
    }

    public String intercept(ActionInvocation actionInvocation) throws Exception {
        if (actionInvocation.getAction() instanceof BaseAction) {
            BaseAction action = (BaseAction) actionInvocation.getAction();
            action.setEm(EMF.getEntityManager());
        }
        return actionInvocation.invoke();
    }

}
