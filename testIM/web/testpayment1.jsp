<%@page contentType="text/html" pageEncoding="UTF-8" language="java"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
        <s:form action="http://localhost:8084/moneta/Assistant" method="get">
            <!--<form method="post" action="/moneta/Assistant.html">-->
            <input type="hidden" name="MNT_ID" value="2">
            <input type="hidden" name="MNT_TRANSACTION_ID" value="<%
                out.print(example.Utils.generateNumber());
            %>">
            <input type="hidden" name="MNT_CURRENCY_CODE" value="RUB">
            <input type="hidden" name="MNT_AMOUNT" value="120.25">
            <input type="hidden" name="MNT_TEST_MODE" value="1">
            <input type="hidden" name="MNT_SUCCESS_URL"
                   value="http://localhost:8084/testIM/success1.jsp">
            <input type="hidden" name="MNT_FAIL_URL"
                   value="http://localhost:8084/testIM/fail.jsp">
            <input type="hidden" name="MNT_CUSTOM1" value="1234567890">
            <input type="hidden" name="MNT_CUSTOM2" value="abcdefghij">
            <input type="hidden" name="MNT_CUSTOM3" value="somebody@somewhere.com">
            <input type="hidden" name="contact.name" value="Suleyman" />
            <input type="hidden" name="contact.phone" value="+79882970412" />
            <input type="hidden" name="contact.email" value="sulic@batyrov.ru" />
            <input type="submit" value="Оплатить заказ">
            <!--</form>-->
        </s:form>
    </body>
</html>
