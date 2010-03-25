package com.batyrov.ps.action;

import com.batyrov.ps.bean.Dealer;
import com.batyrov.ps.bean.User;
import com.batyrov.ps.Const;
import com.batyrov.ps.Dao;

/**
 * Created by IntelliJ IDEA.
 * User: sulic
 * Date: 11.03.2008
 * Time: 22:12:34
 * To change this template use File | Settings | File Templates.
 */
public class RemoveUser extends Action{
    private long id;
    private long pointId;

    public long getPointId() {
        return pointId;
    }

    public void setPointId(long pointId) {
        this.pointId = pointId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String execute() throws Exception {
        super.execute();
        User user = em.find(User.class, id);
        if (user == null){
            addActionError(getText("user_not_found"));
            return Const.ERROR;
        }
        dao.remove(user);
        return Const.SUCCESS;
    }
}
