<%@ page contentType="text/html;charset=windows-1251" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<s:head theme="ajax"/>

<s:form>

    <s:textfield name="startDate" value="%{startDate}" label="От"/>
    <s:textfield name="endDate" value="%{endDate}" label="До"/>
    <s:submit value="Показать" />
    <s:actionerror/>
    <table border="1">
        <tr>
            <td>Номер</td>
            <td>Точка</td>
            <td>Сумма</td>
            <td>Дата</td>
        </tr>
        <s:iterator id="payment" value="%{payments}">
            <tr>
                <td><s:property value="id"/></td>
                <td><s:property value="point.name"/></td>
                <td><s:property value="summa"/></td>
                <td><s:property value="date"/></td>
            </tr>
        </s:iterator>
    </table>
</s:form>