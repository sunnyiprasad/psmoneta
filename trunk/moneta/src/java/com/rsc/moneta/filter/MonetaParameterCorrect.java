/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsc.moneta.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 *
 * @author sulic
 * В данном классе производится небольшая коррекция. Дело в том, что струтс
 * не позволяет определять переменные с
 *
 */
public class MonetaParameterCorrect implements Filter {
    private FilterConfig filterConfig = null;

    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String monetaLocale = request.getParameter("moneta.locale");
        if (monetaLocale != null) {
            request.getParameterMap().put("monetaLocale", monetaLocale);
        }
        String paymentSystemUnitId = request.getParameter("paymentSystem.unitId");
        if (monetaLocale != null) {
            request.getParameterMap().put("paymentSystemUnitId", paymentSystemUnitId);
        }
        String paymentSystemLimitIds = request.getParameter("paymentSystem.limitIds");
        if (monetaLocale != null) {
            request.getParameterMap().put("paymentSystemLimitIds", paymentSystemLimitIds);
        }
        chain.doFilter(request, response);
    }

    public void destroy() {
        this.filterConfig = null;
    }

}
