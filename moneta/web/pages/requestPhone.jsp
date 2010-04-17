<%@page contentType="text/html" pageEncoding="UTF-8" language="java"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Вход в ПС ТЛСМ</title>
    </head>
    <body>
        <p>
            <s:text name="license_agreement" />
        </p>
        <p align="center">
            <s:form action="RegisterByPhone" method="post">
                <s:hidden name="paymentId" value="%{paymentOrder.id}" />
                <s:textfield name="phone" label="%{getText('assitant.enter_phone')}"/>
                <s:password name="password" label="%{getText('enter_password')}" />
                <s:submit value="Registation"/>
                <a href="<s:url action="SelectPaymentSystem"><s:param name="paymentId" value="%{paymentOrder.id}"/></s:url>"></a>
            </s:form>
        </p>
    </body>
</html>
