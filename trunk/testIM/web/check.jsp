<%-- 
    Document   : terminal.jsp
    Created on : 20.04.2010, 21:32:27
    Author     : sulic
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.Random"  %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <form action="http://95.169.186.207:8080/tlsm/Platika.html" method="get">
            <input type="text" name="command" value="check" />
            <label>Summa</label>
            <input type="text" name="sum" />
            <label>Account</label>
            <input type="text" name="account" />

            <input type="text" name="txn_id" value="<%
                Random r = new Random();
                long l = r.nextLong();
                out.print(String.format("%019d", l));
            %>" />
            <input type="submit"  value="Проверить" />
        </form>
    </body>
</html>
