<%@page contentType="text/html" pageEncoding="UTF-8" language="java"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><s:text name="title_main_public_page" /></title>
        <link href="<s:url value="/css/base_style.css"/>" type="text/css" />
        <script src="<s:url value="/js/jquery.js"/>" type="text/javascript"></script>
        <script type="text/javascript">
            $(document).ready(
            function (){
            <s:if test="reg">
                    $("#pswd").show();
                    $("#license").hide();
                    $("#submit_button").val("<s:text name="login_button"/>");
            </s:if>
            <s:else >
                    $("#pswd").hide();
            </s:else>
                    $("#checkbox").click(function(){
                        //alert($("#checkbox:checked").val());
                        if ($("#checkbox:checked").val() == "true"){
                            $("#pswd").show();
                            $("#license").hide();
                            $("#submit_button").val("<s:text name="login_button"/>");
                        }else{
                            $("#pswd").hide();
                            $("#license").show();
                            $("#submit_button").val("<s:text name="create_account_button"/>");
                        }
                    });
                }
            );
        </script>
    </head>
    <body>        
        <center>
            <img src="<s:url value="/images/big_logo.gif" />" alt="logotype" />
            <table width="600" id="license">
                <tr><td><s:text name="license_agreement" /></td></tr>
            </table>
            <s:actionerror/>
            <s:form action="RegisterOrLogin" method="get">
                <s:hidden name="paymentId" value="%{paymentOrder.id}" />
                <s:textfield name="email"  label="%{getText('enter_email')}" />
                <tr id="pswd" >
                    <td  class="tdLabel">
                        <s:label cssClass="label" value="%{getText('enter_password')}" label="%{getText('enter_password')}" theme="simple"/>
                    </td>
                    <td>
                        <s:password name="password" theme="simple"  />
                    </td>
                </tr>
                <s:submit id="submit_button" value="%{getText('create_account_button')}"/>
                <a href="<s:url action="SelectPaymentSystem">
                       <s:param name="paymentId" value="%{paymentOrder.id}"/>
                   </s:url>">
                </a>
                <s:checkbox name="reg" id="checkbox" label="%{getText('already_registered')}" value="%{reg}" />
            </s:form>            
        </center>
    </body>
</html>
