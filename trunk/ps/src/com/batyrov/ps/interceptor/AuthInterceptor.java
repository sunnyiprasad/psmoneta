package com.batyrov.ps.interceptor;

import com.opensymphony.xwork2.interceptor.Interceptor;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionContext;
import com.batyrov.ps.Const;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: sulic
 * Date: 05.04.2008
 * Time: 22:16:06
 * To change this template use File | Settings | File Templates.
 */

public class AuthInterceptor implements Interceptor {

    protected Map session = null;

    public void destroy() {

    }

    public void init() {

    }

    public String intercept(ActionInvocation actionInvocation) throws Exception {
        session = actionInvocation.getInvocationContext().getSession();                        
        if (session.get("myDealerId") == null || session.get("myKeyId") == null || session.get("myUserId") == null){
            return Const.LOGIN_FORWARD;
        }else{
            return actionInvocation.invoke();
        }
    }
}
