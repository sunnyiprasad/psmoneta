<%@page contentType="text/html" pageEncoding="UTF-8" language="java"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Выбор платежной системы</title>
    </head>
    <body>
        <p><s:text name="selectpayment.order_registered" /> <br/>
            <s:text name="selectpayment.main_message" />
            <s:text name="selectpayment.enter_payment_key" /><s:property value="%{paymentOrderId}" />
        </p>
        <p><s:a href="%{paymentOrder.successUrl}" ><s:text name="return_to_emarketplace" /></s:a></p>
        <p><s:a action="public/"><s:text name="my_cabinet" /></s:a></p>
        <p>
            <form action="https://merchant.webmoney.ru/lmi/payment.asp">
                <s:hidden name="LMI_PAYMENT_AMOUNT" value="%{paymentOrder.amount}" />
                <s:hidden name="LMI_PAYMENT_DESC" value="%{paymentOrder.description}"/>
                <s:hidden name="LMI_PAYMENT_NO" value="%{paymentOrder.id}" />
                <s:hidden name="LMI_PAYEE_PURSE" value="%{webmoneyAccount}" />
                <s:if test="%{paymentOrder.test}">
                    <s:hidden name="LMI_SIM_MODE" value="2" />
                </s:if>
                <s:submit value="WEBMONEY" />
            </form>
        </p>
    </body>
</html>
