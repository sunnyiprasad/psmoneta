<%-- 
    Document   : index
    Created on : 27.03.2010, 15:52:42
    Author     : sulic
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.rsc.moneta.bean.Mail" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Index Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
        <% 
            Mail mail = new Mail("suleyman.batyrov@gmail.com", "test1");
            mail.start();
        %>
    </body>
</html>
