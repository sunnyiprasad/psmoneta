<%@page contentType="text/html" pageEncoding="UTF-8" language="java"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <p>Ваш заказ зарегистрирован. <br/>
            Сейчас вы должна направиться к любому терминалу платежных систем: Платика<br/>
            При оплате билета у вас будет запрошен код заказа.
            Введите код:<s:property value="%{paymentKey.key}" />
        </p>
        <p><s:a href="%{paymentKey.successUrl}" >Вернуться в магазин</s:a></p>
    </body>
</html>
