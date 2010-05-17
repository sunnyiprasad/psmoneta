<%@page contentType="text/html" pageEncoding="UTF-8" language="java"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><s:text name="select_payment_system" /></title>
        <link href="/css/base_style.css" />
    </head>
    <body>
        <table width="600" align="center">
            <tr>
                <td align="center">
                    <img src="<s:url value="/images/big_logo.gif" />" alt="logotype" />
                </td>
            </tr>
            <tr>
                <td>
                    <p><s:text name="%{msg}" /> 
                    </p>
                    <p><s:a href="%{paymentOrder.successUrl}" ><s:text name="return_to_emarketplace" /></s:a></p>
                    <p><s:a action="user/"><s:text name="my_cabinet" /></s:a></p>
                    <form action="https://merchant.webmoney.ru/lmi/payment.asp" method="post">
                        <s:hidden name="LMI_PAYMENT_AMOUNT" value="%{paymentOrder.amount}" />
                        <s:hidden name="LMI_PAYMENT_DESC" value="%{paymentOrder.description}"/>
                        <s:hidden name="LMI_PAYMENT_NO" value="%{paymentOrder.id}" />
                        <s:hidden name="LMI_PAYEE_PURSE" value="%{webmoneyAccount}" />
                        <s:if test="%{paymentOrder.test}">
                            <s:hidden name="LMI_SIM_MODE" value="2" />
                        </s:if>
                        <s:submit value="WEBMONEY" />
                    </form>
                </td>
            </tr>
        </table>
    </body>
</html>
