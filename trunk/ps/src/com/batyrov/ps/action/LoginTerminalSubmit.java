package com.batyrov.ps.action;

import com.batyrov.ps.Const;
import com.batyrov.ps.ipriv.IPriv;
import com.batyrov.ps.bean.DealerUser;
import com.batyrov.ps.bean.User;

import org.cyberplat.ipriv.data.IPrivSerial;

/**
 * Created by IntelliJ IDEA.
 * User: sulic
 * Date: 09.03.2008
 * Time: 18:47:57
 * To change this template use File | Settings | File Templates.
 */
public class LoginTerminalSubmit extends Action {

    private String inputmessage = null;
    private String accessLevel = null;

    public String getInputmessage() {
        return inputmessage;
    }

    public void setInputmessage(String inputmessage) {
        this.inputmessage = inputmessage;
    }

    public String getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(String accessLevel) {
        this.accessLevel = accessLevel;
    }

    public String execute() {
        try {
            super.execute();
            if (inputmessage != null) {
                System.out.println(inputmessage);
                long id = IPriv.getInstance().processSignedMessage(inputmessage);
                if (id == -1) {
                    addActionError(getText("invalid_serial_key_format"));
                    return Const.ERROR;
                }
                User user = dao.getUserBySerialKey(id);
                if (user == null) {
                    addActionError(getText("user_not_found"));
                    return Const.ERROR;
                }
                session.put("myDealerId", user.getPoint().getDealerId());
                session.put("myPointId", user.getPointId());
                session.put("myKeyId", id);
                session.put("myUserId", user.getId());
                return Const.TERMINAL;
            } else
                return Const.ERROR;
        } catch (Exception e) {
            e.printStackTrace();
            addActionError(e.toString());
            return Const.ERROR;
        }
    }
}