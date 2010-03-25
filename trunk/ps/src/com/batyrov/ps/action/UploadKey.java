package com.batyrov.ps.action;

import com.batyrov.ps.Const;
import com.batyrov.ps.bean.Key;
import com.batyrov.ps.bean.Point;
import com.batyrov.ps.bean.DealerUser;
import com.batyrov.ps.bean.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.DataInputStream;

/**
 * Created by IntelliJ IDEA.
 * User: sulic
 * Date: 19.03.2008
 * Time: 23:56:42
 * To change this template use File | Settings | File Templates.
 */

public class UploadKey extends Action {

    long dealerId;
    long pointId;
    int keytype = -1;
    long id = -1;
    File upload = null;
    String uploadFileName = null;
    String uploadContentType = null;

    public String execute() throws Exception {
        /*
        todo: Фактически здесь можно взломать систему. Достаточно указать левый keytype и можно будет менять key
        на тех юзеров у которых id совпадает с тем к которому у тебя есть доступ срочно исправить. На проверять
        действительно keytype совпадает реально с БД.
         */

        super.execute();
        System.out.println(uploadFileName);
        System.out.println(uploadContentType);
        System.out.println(upload.getAbsolutePath());
        FileInputStream file = new FileInputStream(upload);
        byte[] buffer = new byte[10024];
        int buf_len = file.read(buffer);
        long publicKeyId = -1;
        String result;
        switch (keytype){
            case Const.DEALER_USER:{
                DealerUser dealerUser = em.find(DealerUser.class, id);
                if (dealerUser == null){
                    addActionError(getText("dealer_user_not_found"));
                    return Const.ERROR;
                }
                if (dealerUser.getDealerId() != myDealerId && dealerUser.getDealer().getOwnerDealerId() != myDealerId)
                {
                    addActionError(getText("access_denied"));
                    return Const.ERROR;
                }
                publicKeyId = dealerUser.getPublicKeyId();
                result = Const.SUCCESS3;
                break;
            }
            case Const.USER:{
                User user = em.find(User.class, id);
                if (user == null){
                    addActionError(getText("user_not_found"));
                    return Const.ERROR;
                }
                if (user.getPoint().getDealerId() != myDealerId)
                {
                    addActionError(getText("access_denied"));
                    return Const.ERROR;
                }
                publicKeyId = user.getPublicKeyId();
                result = Const.SUCCESS2;
                break;
            }
            case Const.POINT:{
                Point point = em.find(Point.class, id);
                if (point == null){
                    addActionError(getText("point_not_found"));
                    return Const.ERROR;
                }
                if (point.getDealerId() != myDealerId)
                {
                    addActionError(getText("access_denied"));
                    return Const.ERROR;
                }
                publicKeyId = point.getPublicKeyId();
                result = Const.SUCCESS;
                break;
            }
            default:{
                addActionError(getText("invalid_keytype"));
                return Const.ERROR;
            }
        }
        Key key = em.find(Key.class, publicKeyId);
        if (key == null){
            addActionError(getText("key_not_found"));
            return Const.ERROR;
        }
        em.getTransaction().begin();
        try{
            key.setData(buffer);
            em.getTransaction().commit();
        }catch (Exception e){
            e.printStackTrace();
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            addActionError(e.toString());
            return Const.ERROR;
        }
        return result;
    }

    public File getUpload() {
        return upload;
    }

    public void setUpload(File upload) {
        this.upload = upload;
    }

    public String getUploadFileName() {
        return uploadFileName;
    }

    public void setUploadFileName(String uploadFileName) {
        this.uploadFileName = uploadFileName;
    }

    public String getUploadContentType() {
        return uploadContentType;
    }

    public void setUploadContentType(String uploadContentType) {
        this.uploadContentType = uploadContentType;
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
}
