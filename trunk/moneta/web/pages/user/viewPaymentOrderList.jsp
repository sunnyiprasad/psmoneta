<%-- 
    Document   : viewPaymentOrderList
    Created on : 16.04.2010, 11:03:51
    Author     : sulic
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" language="java"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<p>Сумма заказа составляет: <s:property value="%{sumAndCount.amount}" /></p>
<p>Количество заказов составляет: <s:property value="%{sumAndCount.count}" /></p>
<s:form action="ViewAccountList" name="period" >
    Просмотреть платежи сделаные с&nbsp;
    <s:textfield name="day_begin" value="%{day_begin}" size="2" theme="simple" />
    <s:select name="month_begin" list="%{months}" listKey="number" listValue="name" value="%{month_begin}" theme="simple" />
    <s:select name="year_begin" list="%{year}" listKey="number" listValue="name" value="%{year_begin}" theme="simple" />
    &nbsp;по&nbsp;
    <s:textfield name="day_end" value="%{day_end}" size="2" theme="simple" />
    <s:select name="month_end" list="%{months}" listKey="number" listValue="name" value="%{month_end}" theme="simple" />
    <s:select name="year_end" list="%{year}" listKey="number" listValue="name" value="%{year_end}" theme="simple" />
    <br/>
    Фильтр по статусу:
    <select name="status">
        <option selected value="0"> Открытый</option>
        <option value="1">Обработанный</option>
    </select>
    <s:submit value="Показать" theme="simple" />
</s:form>

<s:actionerror/>



<table border="1">
    <tr>
         <td><s:text name="id" /></td>
            <td><s:text name="amount" /></td>
            <td><s:text name="transactionId" /></td>
            <td><s:text name="marketId" /></td>
            <td><s:text name="date" /></td>
            <td><s:text name="test" /></td>
            <td><s:text name="status" /></td>
            <td><s:text name="description" /></td>
    </tr>
    <s:iterator value="%{paymentOrders}" var="paymentOrder">
        <tr>
            <td><s:property value="id" /></td>
            <td><s:property value="amount" /></td>
            <td><s:property value="transactionId" /></td>
            <td><s:property value="marketId" /></td>
            <td><s:property value="date" /></td>
            <td><s:property value="test" /></td>
            <td><s:property value="status" /></td>
            <td><s:property value="description" /></td>

        </tr>
    </s:iterator>
</table>
