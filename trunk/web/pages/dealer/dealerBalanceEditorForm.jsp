<%@ page contentType="text/html;charset=windows-1251" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<s:form action="AddDealerBalance-Submit">
    <s:hidden name="dealerId" value="%{dealer.id}" />
    <s:label label="Текущий баланс" value="%{dealer.balance}" />
    <s:textfield label="Сумма" name="summa" value="0.00" required="true" />
    <s:submit name="submit" value="Submit"></s:submit>
    <s:reset name="reset" value="Reset"></s:reset>
</s:form>
