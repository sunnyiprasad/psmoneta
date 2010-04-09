<%@ page contentType="text/html; charset=windows-1251" language="java" %>
<%@ taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1251"/>
        <title><s:text name="title_for_my_cabinet" /></title>
    </head>
    <body>
        <s:a action="ViewUserList"><s:text name="viewuserlist" /></s:a>
        <s:a action="ViewCashOrderList"><s:text name="viewcashorderlist" /></s:a>
        <decorator:body/>
    </body>

</html>
