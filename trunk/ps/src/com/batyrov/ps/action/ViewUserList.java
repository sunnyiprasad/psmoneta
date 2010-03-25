package com.batyrov.ps.action;

import com.batyrov.ps.bean.User;
import com.batyrov.ps.bean.Point;
import com.batyrov.ps.Const;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: sulic
 * Date: 11.03.2008
 * Time: 22:09:43
 * To change this template use File | Settings | File Templates.
 */
public class ViewUserList extends Action{
    private long pointId = -1;
    private Point point = null;
    private Collection<User> users = null;
    private int pointType;

    public int getPointType() {
        return pointType;
    }

    public void setPointType(int pointType) {
        this.pointType = pointType;
    }

    public long getPointId() {
        return pointId;
    }

    public void setPointId(long pointId) {
        this.pointId = pointId;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public Collection<User> getUsers() {
        return users;
    }

    public void setUsers(Collection<User> users) {
        this.users = users;
    }

    public String execute() throws Exception {
        super.execute();
        point = em.find(Point.class, pointId);
        if (point == null){
            addActionError(getText("point_not_found"));
            return Const.ERROR;
        }
        pointType = point.getType();
        users = point.getUsers();
        return Const.SUCCESS;
    }
}
