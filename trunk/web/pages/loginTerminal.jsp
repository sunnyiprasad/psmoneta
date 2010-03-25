<%@ page contentType="text/html;charset=windows-1251" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <title>Cyberplat</title>
    <meta http-equiv="Content-Type" content="text/html; charset=windows-1251">
    <meta http-equiv="Cache-Control" content="no-cache">
    <META HTTP-EQUIV="Pragma" CONTENT="no-cache">
    <META HTTP-EQUIV="expires" CONTENT="Mon, 30 Jun 2003 08:21:57 GMT">
    <LINK REL="StyleSheet" HREF="/mts/css/den_dealer.css" type="text/css">
    <link rel="alternate" type="application/rss+xml" title="Cyberplat.Com" href="/RSS/cybernews.xml"/>
</head>
<body bgColor=#ffffff background=/mts/images/bg.gif>

<center>
    <table width="700" border=0 cellpadding=5 cellspacing=0>
        <tr>
            <td bgcolor=#6699cc align=center width="50%"><a href="http://www.cyberplat.ru" target="_blank"><img
                    src="/mts/images/cplogo.gif" border=0></a></td>

            <td bgcolor=#FF3800 align=right width="50%"><font color=#FFFFFF class=title><b>Подтверждение прав доступа в
                систему</b></font></td>
        </tr>

        <OBJECT id=ipriv codeBase=/psWeb/iprivcom.cab#version=1,0,0,7 height=0
                standby="ПО 'Тонкий клиент'. Версия II" width=0
                classid=clsid:47B955CD-0E70-406B-BC50-13EF2EA20B0A></OBJECT>

        <SCRIPT language=javascript>
            ipriv.reset();
            function GetResultSign(form)
            {

                try
                {
                    var ResultSign = ipriv.SignMessage(form.inputmessage.value);
                    //если результат успешный, то передаем новое значение в форму
                }
                catch(e)
                {
                    alert("Произошла ошибка вызова объекта ActiveX.\n\nДля устранения проблемы убедитесь, что Вы используете Internet Explorer, зайдите на данную страницу с правами локального администратора Вашего компьютера для установки требуемого объекта ActiveX.\nЕсли проблема сохранится, обратитесь в службу технической поддержки Киберплат.Ком.");
                    return false;
                }
                if (ResultSign)
                {
                    if (confirm(ResultSign))
                    {
                        form.inputmessage.value = ResultSign;
                        return true;
                    }
                    else
                    {


                        return false;
                    }
                }
                else
                {
                    return false;
                }

                return true;
            }

        </SCRIPT>

        <tr bgcolor=#ffffff>
            <td valign=center colspan="2">


                <br/>

                <FORM onsubmit="return GetResultSign(this);" action="loginTerminalSubmit.html" method=post>
                    <input name="accessLevel" value="terminal" type="hidden"/>
                    <textarea NAME="inputmessage" ROWS="6" COLS="70" WRAP=Physical style="width:500">ПОДТВЕРЖДЕНИЕ ПРАВ
                        ДОСТУПА В СИСТЕМУ ПРИЕМА ПЛАТЕЖЕЙ
                        ДАТА: 9.03.2008
                        ВРЕМЯ: 17:16:20</textarea><BR>
                    <INPUT class=button type=submit value="Подписать" name=go/>
                </FORM>

                <BR/>

            </td>
        </tr>

        <tr>
            <td bgcolor=#5A96CE align=center colspan="2">
                <table>
                    <tr>
                        <td bgcolor=#5A96CE align=center><font color=#FFFFFF size=2><b>телефон: (8 800) 100-100-8*, (+7
                            495)
                            981-80-80, 967-02-20; факс: (+7 495) 967-02-08;</b></font>
                            <a alt="email" class=cl3 href="mailto:support@cyberplat.com"><img alt="email"
                                                                                              src="/mts/images/email_new.gif"
                                                                                              border=0/></a>
                            <nobr/>
                            &nbsp;
                            <a alt="icq"
                               href="http://web.icq.com/whitepages/message_me?uin=394034162&action=message"><img
                                    alt="icq" src="/mts/images/icq_new.gif" border=0></a><font color=#FFFFFF size=2><b>
                                &nbsp;(394034162)</b></font>
                            <nobr/>
                            &nbsp;
                            <a alt="skype" href="callto://support_cyberplat"><img alt="skype"
                                                                                  src="/mts/images/skype_new.gif"
                                                                                  border=0></a><br/>
                            * - с кодом 8 800 звонок из регионов России бесплатный
                        </td>
                    </tr>
                </table>


            </td>
        </tr>
    </table>
</center>
</body>
</html>

