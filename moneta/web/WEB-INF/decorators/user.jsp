<%-- 
    Document   : private
    Created on : 28.03.2010, 14:10:56
    Author     : sulic
--%>

<%@ page contentType="text/html; charset=windows-1251" language="java" %>
<%@ taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Личный кабинет: <s:property value="user.name"/></title>
    </head>
    <body>
        <ul>
            <li><s:a action="ViewPaymentOrderList"><s:text name="view_payment_order_list" /></s:a></li>
            <li><s:a action="ViewAccountList"><s:text name="view_account_list" /></s:a></li>
        </ul>
            
        <decorator:body/>
    </body>
</html>
