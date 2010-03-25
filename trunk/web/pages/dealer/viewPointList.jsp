<%@ page contentType="text/html;charset=windows-1251" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>


<a href="<s:url action="GetPoint" />">Добавить точку</a>
<s:actionerror/>
<table border="1">
    <tr>
        <td>Номер</td>
        <td>Имя</td>
        <td>Баланс</td>
        <td>Адрес</td>
        <td>Редактировать</td>
        <td>Удалить</td>
        <td>Операторы точки</td>
        <td>Карточка ключа</td>
        <td>Загрузить ключ</td>
        <td>Статус</td>
        <td>Добавить баланс</td>
    </tr>
    <s:iterator value="%{points}" id="point">
        <tr>
            <td><s:property value="id"/></td>
            <td><s:property value="name"/></td>
            <td><s:property value="balance"/></td>
            <td><s:property value="address"/></td>
            <td>
                <a href="<s:url action="GetPoint" ><s:param value="id" name="id" /></s:url>">
                    <img src="" alt="edit"/>
                </a>
            </td>

            <td><a href="<s:url action="RemovePoint"><s:param name="id" value="id"/></s:url>"><img src=""
                                                                                                            alt="del"/></a>
            </td>
            <td><a href="<s:url action="ViewUserList"><s:param name="pointId" value="id"/></s:url>"><img src=""
                                                                                                                  alt="manage user"/></a>
            </td>
            <s:if test="type == 0">
                <td>
                    <a href="<s:url action="MakeKeyCard"><s:param name="keytype" value="2"/><s:param name="id" value="id"/></s:url>"><img
                            src="" alt="make key card"/></a></td>
                <td>
                    <a href="<s:url action="UploadKey"><s:param name="keytype" value="2"/><s:param name="id" value="id"/></s:url>"><img
                            src="" alt="upload key"/></a></td>
            </s:if>
            <s:else>
                <td></td>
                <td></td>
            </s:else>
            <td><a href="<s:url action="LockPoint"><s:param name="id" value="id"/></s:url>"><img src=""
                                                                                                          alt="lock point"/></a>
            </td>
            <td><a href="<s:url action="AddPointBalance"><s:param name="id" value="id"/></s:url>"><img src=""
                                                                                                                alt="add point balance"/></a>
            </td>

        </tr>
    </s:iterator>
</table>