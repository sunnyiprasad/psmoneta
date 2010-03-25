<%@ page contentType="text/html;charset=windows-1251" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>


<a href="<s:url action="GetUser"><s:param name="pointId" value="pointId" /> </s:url> ">Добавить пользователя</a>
<s:actionerror/>
<table border="1">
    <tr>
        <td>Номер</td>
        <td>ФИО</td>
        <td>Баланс</td>
        <td>Редактировать</td>
        <td>Удалить</td>
    </tr>
    <s:iterator id="user" value="%{users}">
        <tr>
            <td><s:property value="id"/></td>
            <td><s:property value="name"/></td>
            <td><s:property value="balance"/></td>
            <td><a href="<s:url action="GetUser"><s:param name="pointId" value="pointId"/> <s:param name="id" value="id"/></s:url>"><img src="" alt="edit"/></a>
            </td>
            <td><a href="<s:url action="RemoveUser"><s:param name="id" value="id"/><s:param name="pointId" value="pointId"/></s:url>"><img src="" alt="del"/></a>
            </td>
            <s:if test="pointType != 0">
                <td>
                    <a href="<s:url action="MakeKeyCard"><s:param name="keytype" value="1"/><s:param name="id" value="id"/></s:url>"><img
                            src="" alt="make key card"/></a></td>
                <td>
                    <a href="<s:url action="UploadKey"><s:param name="keytype" value="1"/><s:param name="id" value="id"/></s:url>"><img
                            src="" alt="upload key"/></a></td>
            </s:if>
            <s:else>
                <td></td>
                <td></td>
            </s:else>
            <td><a href="<s:url action="LockUser"><s:param name="pointId" value="pointId"/><s:param name="id" value="id"/></s:url>"><img src="" alt="lock user"/></a>
            </td>
        </tr>
    </s:iterator>
</table>