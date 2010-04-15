<%@page contentType="text/html" pageEncoding="UTF-8" language="java"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page1</title>
    </head>
    <body>
        <p align="center">
            <s:form action="Login" method="get" >
                <s:textfield name="username" label="%{getText('phone')}" required="true" />
                <s:password name="password" label="%{getText('password')}" required="true" />
                <s:submit value="%{getText('login')}" />            
            </s:form>
        </p>
    </body>
</html>
