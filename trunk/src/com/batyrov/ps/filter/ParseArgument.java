package com.batyrov.ps.filter;

import javax.servlet.*;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 * User: sulic
 * Date: 02.03.2008
 * Time: 11:05:00
 * To change this template use File | Settings | File Templates.
 */
public class ParseArgument implements Filter {
    private FilterConfig filterConfig = null;

    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        InputStream in = servletRequest.getInputStream();
                        
        filterChain.doFilter(servletRequest, servletResponse);
    }

    public void destroy() {
        this.filterConfig = null;
    }
}
