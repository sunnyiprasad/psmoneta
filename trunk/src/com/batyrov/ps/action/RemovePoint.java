package com.batyrov.ps.action;

import com.batyrov.ps.Const;
import com.batyrov.ps.Dao;
import com.batyrov.ps.bean.Point;

/**
 * Created by IntelliJ IDEA.
 * User: sulic
 * Date: 11.03.2008
 * Time: 22:12:20
 * To change this template use File | Settings | File Templates.
 */
public class RemovePoint extends Action{
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String execute() throws Exception {
        super.execute();
        Point point = em.find(Point.class, id);
        if (point == null){
            addActionError(getText("point_not_found"));
            return Const.ERROR;
        }
        if (dao.remove(point)){
            addActionError(getText("point_have_child"));
            return Const.ERROR;
        }
        return Const.SUCCESS;
    }
}
