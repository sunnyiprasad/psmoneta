package com.batyrov.ps.action;

import org.cyberplat.util.Radix64;

import java.io.*;

import com.batyrov.ps.Const;
import com.batyrov.ps.bean.DealerUser;
import com.batyrov.ps.bean.User;
import com.batyrov.ps.bean.Point;
import com.opensymphony.xwork2.ActionContext;

import javax.servlet.ServletContext;

/**
 * Created by IntelliJ IDEA.
 * User: sulic
 * Date: 02.03.2008
 * Time: 11:14:28
 * To change this template use File | Settings | File Templates.
 */

public class MakeKeyCard extends Action {
    long dealerId;
    long pointId;
    InputStream inputStream = null;
    long id = -1;
    int keytype = -1;

    public long getDealerId() {
        return dealerId;
    }

    public void setDealerId(long dealerId) {
        this.dealerId = dealerId;
    }

    public long getPointId() {
        return pointId;
    }

    public void setPointId(long pointId) {
        this.pointId = pointId;
    }

    public int getKeytype() {
        return keytype;
    }

    public void setKeytype(int keytype) {
        this.keytype = keytype;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String execute() {
        try {
            super.execute();
            String version = "01";
            String userId = id + "";
            String userKey = "";
            /*
           Тип ключа показывает для кого создается ключ
           * */
            switch (keytype) {
                // Ключ создается для пользователя дилера
                // Я так понимаю это оператор дилера, который будет создавать
                // субдилеров т.е. управляющий пользователь
                case Const.DEALER_USER: {
                    DealerUser dealerUser = em.find(DealerUser.class, id);
                    if (dealerUser == null) {
                        addActionError(getText("dealer_user_not_found"));
                        return Const.ERROR;
                    }
                    userKey = dealerUser.getPublicKeyId() + "";
                    break;
                }
                // Ключ создается для пользователя,
                // данный пользователь работает через веб точку.
                case Const.USER: {
                    User user = em.find(User.class, id);
                    if (user == null) {
                        addActionError(getText("user_not_found"));
                        return Const.ERROR;
                    }
                    if (user.getPoint().getType() == Point.API) {
                        addActionError(getText("api_point_user_cannot_have_public_key"));
                        return Const.ERROR;
                    }
                    userKey = user.getPublicKeyId() + "";
                    break;
                }
                // Ключ создается для точки приема платежей stanalone applcation
                // Однако такой ключ может иметь только
                case Const.POINT: {
                    Point point = em.find(Point.class, id);
                    if (point == null) {
                        addActionError(getText("point_not_found"));
                        return Const.ERROR;
                    }
                    if (point.getType() != Point.API) {
                        addActionError(getText("web_point_cannot_have_public_key"));
                        return Const.ERROR;
                    }
                    userKey = point.getPublicKeyId() + "";
                    break;
                }
                default: {
                    addActionError(getText("invalid_keytype"));
                    return Const.ERROR;
                }
            }
            // userKey это id ключа в таблице t_Key
            // userId это id пользователя, пользователя дилера или точки.(t_User, t_Point, t_DealerUser)
            String srt = "Version: " + version + "\r\nUser ID: " + userId + "\r\nUser Key: " + userKey + "\r\n";
            byte[] in = srt.getBytes();
            byte[] checkSum = new Radix64().getCheckSum(in);
            try {

                File file = File.createTempFile("card", "key", (File) servletContext.getAttribute("javax.servlet.context.tempdir"));
                FileOutputStream out = new FileOutputStream(file);
                out.write((srt + "=" + new String(checkSum)).getBytes());
                out.close();
                inputStream = new FileInputStream(file.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
                addActionError(e.toString());
                return Const.ERROR;
            }
            return Const.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            addActionError(e.toString());
            return Const.ERROR;
        }
    }
}
