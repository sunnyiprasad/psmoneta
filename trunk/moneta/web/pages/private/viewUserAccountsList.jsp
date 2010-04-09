<%-- 
    Document   : viewUserAccountsList
    Created on : 09.04.2010, 14:29:46
    Author     : abdulaev rashid
--%>
<%@page contentType="text/html" pageEncoding="UTF-8" language="java"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<s:actionerror/>

<table border="1">
    <tr>
        <td>Номер</td>
        <td>Состояние</td>
        <td>Тип валюты</td>
    </tr>
    <s:iterator value="%{accounts}" var="account">
        <tr>
            <td><s:property value="%{id}" /></td>
            <td><s:property value="%{balance}" /></td>
            <td><s:property value="%{type}" /></td>
        </tr>
    </s:iterator>
</table>
