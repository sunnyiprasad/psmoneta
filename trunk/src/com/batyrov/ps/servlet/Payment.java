package com.batyrov.ps.servlet;

import com.batyrov.ps.module.Cyberplat;
import com.batyrov.ps.module.Processor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: sulic
 * Date: 16.02.2008
 * Time: 0:26:19
 * To change this template use File | Settings | File Templates.
 */
public class Payment extends Servlet {
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        super.doPost(httpServletRequest, httpServletResponse);
        if (getAnswer() != null) {
            return;
        }
        if (getPayment() == null) {
            Properties res = Cyberplat.getErrorAnswer(1, 11);
            print(res);
        }
        try {
            Processor proc = (Processor) this.getClass().getClassLoader().loadClass(getProvider().getClassName()).newInstance();
            print(proc.payment(getPayment(), getReq_type()));
        } catch (Exception e) {
            print(Cyberplat.getErrorAnswer(1, 30));
            e.printStackTrace();
        }
    }
}
