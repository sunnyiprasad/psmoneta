<%--
    Document   : userPrivateOffice
    Created on : 08.04.2010, 14:53:19
    Author     : abdulaev rashid
--%>
<%@page contentType="text/html" pageEncoding="UTF-8" language="java"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<s:actionerror/>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Кабинет пользователя</title>
    </head>
    <body>
        <dl>
            <dt>Личный кабинет пользователя</dt>
            <dd><s:a action="ViewUserAccountsList" >Счета</s:a></dd>
        </dl>
    </body>
</html>
