<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<?xml version="1.0" encoding="UTF-8"?>
<MNT_RESPONSE>
    <MNT_ID><s:property value="%{MNT_ID}" /></MNT_ID>
    <MNT_TRANSACTION_ID><s:property value="%{MNT_TRANSACTION_ID}" /></MNT_TRANSACTION_ID>
    <MNT_RESULT_CODE><s:property value="%{MNT_RESULT_CODE}" /></MNT_RESULT_CODE>
    <MNT_DESCRIPTION><s:property value="%{MNT_DESCRIPTION}" /></MNT_DESCRIPTION>
    <MNT_AMOUNT><s:property value="%{MNT_AMOUNT}" /></MNT_AMOUNT>
    <MNT_SIGNATURE><s:property value="%{MNT_SIGNATURE}" /></MNT_SIGNATURE>
    <MNT_ATTRIBUTES>
       <ATTRIBUTE>
          <KEY>name</KEY>
          <VALUE>value</VALUE>
       </ATTRIBUTE>
    </MNT_ATTRIBUTES>
</MNT_RESPONSE>
