<%-- 
    Document   : webmoney
    Created on : 10.05.2010, 22:36:30
    Author     : sulic
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
        <s:form>
            <s:input type="hidden" name="LMI_PAYMENT_AMOUNT" value="" />
            <s:input type="hidden" name="LMI_PAYMENT_DESC" value="тестовый платеж" />
            <s:input type="hidden" name="LMI_PAYMENT_NO" value="1" />
            <s:input type="hidden" name="LMI_PAYEE_PURSE" value="Z145179295679" />
            <s:input type="hidden" name="LMI_SIM_MODE" value="0" />
        </s:form>
    </body>
</html>
