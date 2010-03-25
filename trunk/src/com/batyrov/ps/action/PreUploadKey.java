package com.batyrov.ps.action;

/**
 * Created by IntelliJ IDEA.
 * User: sulic
 * Date: 30.03.2008
 * Time: 11:44:04
 * To change this template use File | Settings | File Templates.
 */
public class PreUploadKey extends Action{
    long dealerId;
    long pointId;
    long id;
    int keytype;

    public long getDealerId() {
        return dealerId;
    }

    public void setDealerId(long dealerId) {
        this.dealerId = dealerId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getKeytype() {
        return keytype;
    }

    public void setKeytype(int keytype) {
        this.keytype = keytype;
    }

    public long getPointId() {
        return pointId;
    }

    public void setPointId(long pointId) {
        this.pointId = pointId;
    }
}
