<%@ page contentType="text/html;charset=windows-1251" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<s:form action="SavePoint">
    <s:hidden name="id" value="%{id}" />
    <s:hidden name="dealerId" value="%{dealerId}" />

    <s:textfield label="Название" name="name" value="%{point.name}" required="true"></s:textfield>
    <s:if test="id < 1">
        <s:select label="Тип точки" name="type" list="%{types}" listKey="key" listValue="value" value="%{point.type}" />
    </s:if>
    <s:else>
        <s:if test="point.type == 0">
            <s:label value="API" />
        </s:if>
        <s:else>
            <s:label value="WEB" />
        </s:else>
    </s:else>
    <s:select label="Регион" name="region" list="%{regions}" listKey="key" listValue="value" value="%{point.region}" />
    <s:textfield label="Адрес" name="address" value="%{point.address}"></s:textfield>
    <s:textfield label="Телефон" name="phone" value="%{point.phone}"></s:textfield>
    <s:submit name="submit" value="Submit"></s:submit>
    <s:reset name="reset" value="Reset"></s:reset>
</s:form>
