<%-- 
    Document   : newjsppay
    Created on : 20.04.2010, 21:35:33
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
        <form action="http://95.169.186.207:8080/tlsm/Platika.html" method="get">
            <input type="text" name="command" value="pay" />
            <input type="text" name="sum" />
            <input type="text" name="account" />
            <input type="text" name="txn_id" value="<%
                out.print(example.Utils.generateNumber());
            %>" />
            <input type="submit" name="Оплатить" value="" />
        </form>
    </body>
</html>
