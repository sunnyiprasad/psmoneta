<%-- 
    Document   : testpayment2
    Created on : 28.03.2010, 16:05:57
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
        <form method="post" action="https://www.moneta.ru/assistant.htm">
            <input type="hidden" name="MNT_ID" value="00000001">
            <input type="hidden" name="MNT_TRANSACTION_ID" value="FF790ABCD">
            <input type="hidden" name="MNT_CURRENCY_CODE" value="RUB">
            <input type="hidden" name="MNT_AMOUNT" value="120.25">
            <input type="submit" value="Оплатить заказ">
        </form>
    </body>
</html>
