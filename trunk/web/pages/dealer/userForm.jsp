<%@ page contentType="text/html;charset=windows-1251" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<s:form action="SaveUser">
    <s:hidden name="dealerUser" value="%{dealerUser}" />
    <s:hidden name="id" value="%{id}" />
    <s:hidden name="pointId" value="%{pointId}" />
    <s:hidden name="dealerId" value="%{dealerId}" />
    <s:select name="type" list="%{types}" listKey="key" listValue="value" value="%{user.type}" label="Тип оператора" />
    <s:textfield name="name" value="%{user.name}" required="true" label="ФИО"></s:textfield>
    <s:textfield name="phone" value="%{user.phone}" label="Телефон"></s:textfield>
    <s:submit name="submit" value="Submit"></s:submit>
    <s:reset name="reset" value="Reset"></s:reset>
</s:form>
