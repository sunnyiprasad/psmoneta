<%@page contentType="text/html" pageEncoding="UTF-8" language="java"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<table>
    <tr>
        <td>Номер заказа на обналичивание</td>
        <td><s:property value="%{cashOrder.id}" /></td>
    </tr>
    <tr>
        <td>Статус</td>
        <td><s:property value="%{cashOrder.status}" /></td>
    </tr>
    <tr>
        <td>Дата создания</td>
        <td><s:property value="%{cashOrder.date}" /></td>
    </tr>
    <tr>
        <td>Сумма</td>
        <td><s:property value="%{cashOrder.amount}" /></td>
    </tr>
    <tr>
        <td>Номер счета клиента</td>
        <td><s:property value="%{cashOrder.account.id}" /></td>
    </tr>
    <tr>
        <td>Телефон (логин) клиента</td>
        <td><s:property value="%{cashOrder.user.phone}" /></td>
    </tr>
</table>
<s:if test="%{cashOrder.status==0}">
    <s:form action="ChangeCashOrderStatus" method="get">
        <table>
            <s:hidden name="cashOrderId" value="%{cashOrder.id}" />
            <select name="status">
                <option selected value="0"> Открытый</option>
                <option value="1">Обработанный</option>
            </select>
            <s:submit value="%{getText('change_order_status')}" name="change" />
            <s:submit value="%{getText('change_order_status')}" name="changeAndNext"/>
        </table>
    </s:form>
</s:if>
