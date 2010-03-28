<%@page contentType="text/html" pageEncoding="UTF-8" language="java"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <table>
            <s:textfield name="username" label="Login" required="true" />
            <s:password name="password" label="Password" required="true" />
            <s:submit value="Вход" />
        </table>
    </body>
</html>
