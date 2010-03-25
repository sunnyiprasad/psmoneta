<%@ page contentType="text/html;charset=windows-1251" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<a href="<s:url action="GetSubDealer" />">Добавить субдилера</a>
<s:actionerror />
<table border="1">
    <tr>
        <td>Номер</td>
        <td>ФИО</td>
        <td>Баланс</td>
        <td>Телефон</td>
        <td>Email</td>
        <td>Редактировать</td>
        <td>Удалить</td>
        <td>Пользователи</td>
        <td>Блокировать</td>
        <td>Пополнить баланс</td>
    </tr>
    <s:iterator id="d" value="%{dealers}">
        <tr>
            <td><s:property value="id"/></td>
            <td><s:property value="name"/></td>
            <td><s:property value="balance"/></td>
            <td><s:property value="phone"/></td>
            <td><s:property value="email"/></td>
            <td><a href="<s:url action="GetSubDealer" includeParams="none"><s:param name="id" value="id"/></s:url>"><img src="" alt="edit" /></a></td>
            <td><a href="<s:url action="RemoveSubDealer" includeParams="none"><s:param name="id" value="id"/></s:url>"><img src="" alt="del" /></a></td>
            <td><a href="<s:url action="ViewDealerUserList" includeParams="none"><s:param name="dealerId" value="id"/></s:url>"><img src="" alt="manage" /></a></td>
            <td><a href="<s:url action="LockSubDealer"><s:param name="id" value="id"/></s:url>"><img src="" alt="lock dealer" /></a></td>
            <td><a href="<s:url action="AddDealerBalance"><s:param name="id" value="id"/></s:url>"><img src="" alt="add dealer balance" /></a></td>
        </tr>
    </s:iterator>
</table>