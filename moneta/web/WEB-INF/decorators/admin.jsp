<%@ page contentType="text/html; charset=windows-1251" language="java" %>
<%@ taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=windows-1251"/>
</head>
<body>
<table>
    <tr>        
        <td>Статьи</td>
        <td>Пункты меню</td>
        <td>Вопросы и ответы</td>
        <td>Пользователи</td>
        <td><a href="/Logout.html" >Выход </a> </td>
    </tr>
    <tr>
        <td><a href="/admin/ArticleForm.html">Добавить</a></td>
        <td><a href="/admin/MenuItemForm.html">Добавить</a></td>
        <td><a href="/admin/faq/QuestionForm.html">Добавить вопрос</a></td>
        <td><a href="/admin/UserForm.html">Добавить</a></td>
        <td></td>
    </tr>
    <tr>
        <td><a href="/admin/ViewArticleList.html">Просмотр</a></td>
        <td><a href="/admin/ViewMenuItemList.html">Просмотр</a></td>
        <td><a href="/admin/faq/ViewQuestionList.html">Просмотр вопросов</a></td>
        <td><a href="/admin/ViewUserList.html">Просмотр</a></td>
        <td></td>
    </tr>
</table>

<decorator:body/>
</body>

</html>
