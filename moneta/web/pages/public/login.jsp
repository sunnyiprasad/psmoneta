<%@page contentType="text/html" pageEncoding="UTF-8" language="java"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page1</title>
    </head>
    <body>
        <s:form action="Login" method="get">
        <table>
            <s:textfield name="username" label="%{getText('phone')}" required="true" />
            <s:password name="password" label="%{getText('password')}" required="true" />
            <s:submit value="%{getText('login')}" />
        </table>
        </s:form>
    </body>
</html>
