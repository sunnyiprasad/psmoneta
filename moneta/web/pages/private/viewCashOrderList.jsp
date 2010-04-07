<%@page contentType="text/html" pageEncoding="UTF-8" language="java"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<s:iterator value="%{cashOrders}" var="cashOrder">
    <tr>
        <td><s:property value="%{id}" /></td>
        <td><s:property value="%{account.id}" /></td>
        <td><s:property value="%{account.user.phone}" /></td>
        <td><s:a action="ViewCashOrder"><s:param name="cashOrderId" value="%{id}" /><s:text name="view_cashorder"  /></s:a></td>
        <td><s:a action="ChangeCashOrderStatus"><s:param name="cashOrderId" value="%{id}" /><s:text name="change_cashorder_status" /></s:a></td>

    </tr>
</s:iterator>