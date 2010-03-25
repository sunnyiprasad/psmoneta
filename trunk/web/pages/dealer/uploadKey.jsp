<%@ page contentType="text/html;charset=windows-1251" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<s:form action="UploadKeySubmit" enctype="multipart/form-data">

    <s:hidden name="keytype" value="%{keytype}" />
    <s:hidden name="id" value="%{id}" />
    <s:hidden name="dealerId" value="%{dealerId}" />
    <s:hidden name="pointId" value="%{pointId}" />

    <s:file name="upload" label="Выберите файл с публичным ключом" />
    <s:submit name="submit" value="Submit"></s:submit>
    <s:reset name="reset" value="Reset"></s:reset>
</s:form>
