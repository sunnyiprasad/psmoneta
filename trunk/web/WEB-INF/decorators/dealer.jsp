<%@ page contentType="text/html; charset=windows-1251" language="java" %>
<%@ taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>

<title>Dealer</title>    
    
</head>
<body>
<table width="200" border="1">
  <tr>
    <td>Субдилеры</td>
    <td>Точки</td>
    <td>Дилер Пользователи</td>
    <td>Справка</td>
    <td><a href="Exit.html">Выход</a></td>
  </tr>
  <tr>
    <td><a href="GetSubDealer.html">Добавить</a></td>
    <td><a href="GetPoint.html">Добавить</a></td>
    <td><a href="GetDealerUser.html">Добавить</a></td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td><a href="ViewSubDealerList.html">Просмотр</a></td>
    <td><a href="ViewPointList.html">Просмотр</a></td>
    <td><a href="ViewDealerUserList.html?dealerId=${myDealerId}">Просмотр моих пользователей</a></td>
    <td>&nbsp;</td>    
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td><a href="ViewDealerPaymentList.html">Просмотр платежей</a></td>
    <td><a href="ViewPointPaymentList.html">Просмотр платежей</a></td>
    <td></td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
</table>
<decorator:body />
</body>
</html>
