<%@ page contentType="text/html;charset=windows-1251" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<a href="<s:url action="GetDealerUser" ><s:param name="dealerId" value="dealerId" /> </s:url>">Добавить пользователя</a>
<s:actionerror/>
<table border="1">
    <tr>
        <td>Номер</td>
        <td>ФИО</td>
        <td>Редактировать</td>
        <td>Удалить</td>
    </tr>
    <s:iterator id="user" value="%{users}">
        <tr>
            <td><s:property value="id"/></td>
            <td><s:property value="name"/></td>
            <td><a href="<s:url action="GetDealerUser"><s:param name="dealerId" value="dealerId"/> <s:param name="id" value="id"/></s:url>"><img src=""
                                                                                                             alt="edit"/></a>
            </td>
            <td><a href="<s:url action="RemoveDealerUser"><s:param name="dealerId" value="dealerId"/><s:param name="id" value="id"/></s:url>"><img src=""
                                                                                                                alt="del"/></a>
            </td>
            <td>
                <a href="<s:url action="MakeKeyCard"><s:param name="keytype" value="0"/><s:param name="id" value="id"/></s:url>"><img
                        src="" alt="make key card"/></a></td>
            <td>
                <a href="<s:url action="UploadKey"><s:param name="keytype" value="0"/><s:param name="id" value="id"/></s:url>"><img
                        src="" alt="upload key"/></a></td>
            <td><a href="<s:url action="LockDealerUser"><s:param name="id" value="id"/></s:url>"><img src=""
                                                                                                              alt="lock dealer user"/></a>
            </td>
        </tr>
    </s:iterator>
</table>