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
        <table>
            <tr>
                <td id="header"></td>
            </tr>
            <tr>
                <td id="main">
                    <table>
                        <tr>
                            <td id="left" valign="top" width="100px">
                                <s:iterator value="%{providerList}" var="provider">
                                    <table>
                                        <tr>
                                            <td valign="top">
                                                <s:a action="PaymentProviderForm"><s:property value="name" /></s:a>
                                            </td>
                                        </tr>
                                    </table>
                                </s:iterator>
                            </td>
                            <td id="right"><decorator:body/></td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td id="bottom"></td>
            </tr>
        </table>        
    </body>
</html>
