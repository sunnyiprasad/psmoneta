<%@page contentType="text/html" pageEncoding="UTF-8" language="java"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Страница ошибки</title>
    </head>
    <body>
        <s:actionerror />
        <p>
            <s:a action="public/"><s:text name="redirect_to_main" /></s:a>
        </p>
    </body>
</html>
