<%@page contentType="text/html" pageEncoding="UTF-8" language="java"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<s:form action="ChangeCashOrderStatus" method="get">
    <table>
        <s:hidden name="cashOrderId" />
        <select name="status">
            <option selected value="0"> Открытый</option>
            <option value="1">Обработанный</option>
        </select>
        <s:submit value="%{getText('change_order_status')}" />
    </table>
</s:form>