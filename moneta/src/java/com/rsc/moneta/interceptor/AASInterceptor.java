/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsc.moneta.interceptor;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;
import com.rsc.moneta.bean.User;

/**
 *
 * @author sulic
 */
public class AASInterceptor implements Interceptor {

    public void destroy() {
    }

    public void init() {
    }

    public String intercept(ActionInvocation ai) throws Exception {
        User user = (User) ActionContext.getContext().getSession().get("user");
        if (user == null) {
            return Action.LOGIN;
        } else {
            return ai.invoke();
        }
    }
}
