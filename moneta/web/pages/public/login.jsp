<%@page contentType="text/html" pageEncoding="UTF-8" language="java"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<center>
    <img src="<s:url value="/images/big_logo.gif"/>" alt="logotype" />
    <s:form action="Login" method="get" >
        <s:textfield name="email" label="%{getText('email')}" required="true" />
        <s:password name="password" label="%{getText('password')}" required="true" />
        <s:submit value="%{getText('login_button')}" />
    </s:form>
</center>
