<%@ page contentType="text/html;charset=windows-1251" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<s:form action="SaveSubDealer">
    <s:hidden name="id" value="%{id}" />
    <s:textfield label="Название" name="name" value="%{dealer.name}" required="true"></s:textfield>
    <s:textfield label="Телефон" name="phone" value="%{dealer.phone}"></s:textfield>
    <s:textfield label="Email" name="email" value="%{dealer.email}"></s:textfield>
    <s:submit name="submit" value="Submit"></s:submit>
    <s:reset name="reset" value="Reset"></s:reset>
</s:form>
