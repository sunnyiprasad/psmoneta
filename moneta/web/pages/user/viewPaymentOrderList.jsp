<%-- 
    Document   : viewPaymentOrderList
    Created on : 16.04.2010, 11:03:51
    Author     : sulic
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" language="java"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<p>Сумма заказа составляет: <s:property value="%{sumAndCount.amount}" /></p>
<p>Количество заказов составляет: <s:property value="%{sumAndCount.count}" /></p>
<s:form action="ViewPaymentOrderList" name="period" >
    Просмотреть платежи сделаные с&nbsp;
    <s:textfield name="day_begin" value="%{day_begin}" size="2" theme="simple" />
    <s:select name="month_begin" list="%{months}" listKey="number" listValue="name" value="%{month_begin}" theme="simple" />
    <s:select name="year_begin" list="%{year}" listKey="number" listValue="name" value="%{year_begin}" theme="simple" />
    &nbsp;по&nbsp;
    <s:textfield name="day_end" value="%{day_end}" size="2" theme="simple" />
    <s:select name="month_end" list="%{months}" listKey="number" listValue="name" value="%{month_end}" theme="simple" />
    <s:select name="year_end" list="%{year}" listKey="number" listValue="name" value="%{year_end}" theme="simple" />
    <br/>
    Фильтр по статусу:    
    <s:select name="status2" list="%{statusList}" listKey="id" listValue="description" value="%{status}" theme="simple" />
    <s:submit value="Показать" theme="simple" />
</s:form>

<s:actionerror/>



<table border="1">
    <tr>
        <td><s:text name="id" /></td>
        <td><s:text name="amount" /></td>
        <td><s:text name="transactionId" /></td>
        <td><s:text name="marketId" /></td>
        <td><s:text name="date" /></td>
        <td><s:text name="test" /></td>
        <td><s:text name="status" /></td>
        <td><s:text name="description" /></td>
    </tr>
    <s:iterator value="%{paymentOrders}" var="paymentOrder">
        <tr>
            <td><s:property value="id" /></td>
            <td><s:property value="amount" /></td>
            <td><s:property value="transactionId" /></td>
            <td><s:property value="marketId" /></td>
            <td><s:property value="date" /></td>
            <td><s:property value="test" /></td>
            <td><s:property value="status" /></td>
            <td><s:property value="description" /></td>

        </tr>
    </s:iterator>
</table>

<s:if test="%{showNavigation}">
    <div id="pageNum">
        <s:if test="!prevPages.isDisable">
            <span>
                <s:a action="ViewPaymentOrderList">
                    <s:param name="page" value="%{prevPages.page}" />
                    <s:param name="day_begin" value="%{day_begin}" />
                    <s:param name="month_begin" value="%{month_begin}" />
                    <s:param name="year_begin" value="%{year_begin}" />
                    <s:param name="day_end" value="%{day_end}" />
                    <s:param name="month_end" value="%{month_end}" />
                    <s:param name="year_end" value="%{year_end}" />
                    Предыдущие 10 страниц
                </s:a>
            </span>
        </s:if>
        <s:iterator id="_page" value="%{pages}">
            <s:if test="#_page.isSelect"><span class="ono"><s:property value="%{_page.value}" /></span></s:if>
            <s:elseif test="!#_page.isDisable">
                <span>
                    <s:a action="ViewPaymentOrderList">
                        <s:param name="page" value="%{_page.page}" />
                        <s:param name="day_begin" value="%{day_begin}" />
                        <s:param name="month_begin" value="%{month_begin}" />
                        <s:param name="year_begin" value="%{year_begin}" />
                        <s:param name="day_end" value="%{day_end}" />
                        <s:param name="month_end" value="%{month_end}" />
                        <s:param name="year_end" value="%{year_end}" />
                        <s:property value="%{_page.value}" />
                    </s:a>
                </span>
            </s:elseif>
        </s:iterator>
        <s:if test="!nextPages.isDisable">
            <span>
                <s:a action="ViewPaymentOrderList">
                    <s:param name="page" value="%{nextPages.page}" />
                    <s:param name="day_begin" value="%{day_begin}" />
                    <s:param name="month_begin" value="%{month_begin}" />
                    <s:param name="year_begin" value="%{year_begin}" />
                    <s:param name="day_end" value="%{day_end}" />
                    <s:param name="month_end" value="%{month_end}" />
                    <s:param name="year_end" value="%{year_end}" />
                    Следующие 10 страниц
                </s:a>
            </span>
        </s:if>
    </div>
</s:if>
