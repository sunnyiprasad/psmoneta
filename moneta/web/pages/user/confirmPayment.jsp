<%-- 
    Document   : confirmPayment
    Created on : 27.04.2010, 17:24:20
    Author     : sulic
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" language="java"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<s:actionerror/>

<s:form action="PaymentOrderPay">
    <s:hidden name="paymentOrderId" value="%{paymentOrderId}" />
    <s:label name="" value="" />
    <s:label name="" value="" />
    <s:label name="" value="" />
    <s:submit  value="%{getText('confirm_payment')}" />
</s:form>