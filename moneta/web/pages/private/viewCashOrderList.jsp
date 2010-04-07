<%@page contentType="text/html" pageEncoding="UTF-8" language="java"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<s:actionerror/>

<s:form action="ViewCashOrderList" name="period" >
    Просмотреть платежи сделаные с&nbsp;
    <s:textfield name="day_begin" value="%{day_begin}" size="2" theme="simple" />
    <s:select name="month_begin" list="%{months}" listKey="number" listValue="name" value="%{month_begin}" theme="simple" />
    <s:select name="year_begin" list="%{year}" listKey="number" listValue="name" value="%{year_begin}" theme="simple" />
    &nbsp;по&nbsp;
    <s:textfield name="day_end" value="%{day_end}" size="2" theme="simple" />
    <s:select name="month_end" list="%{months}" listKey="number" listValue="name" value="%{month_end}" theme="simple" />
    <s:select name="year_end" list="%{year}" listKey="number" listValue="name" value="%{year_end}" theme="simple" />
    <s:hidden value="%{userId}" name="userId" theme="simple" />
    <s:submit value="Показать" theme="simple" />
</s:form>

<table border="1">
    <s:iterator value="%{cashOrders}" var="cashOrder">
        <tr>
            <td><s:property value="%{id}" /></td>
            <td><s:property value="%{account.id}" /></td>
            <td><s:property value="%{account.user.phone}" /></td>
            <td><s:a action="ViewCashOrder"><s:param name="cashOrderId" value="%{id}" /><s:text name="view_cashorder"  /></s:a></td>
        </tr>
    </s:iterator>
</table>