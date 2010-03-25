package com.batyrov.ps.action;

import com.batyrov.ps.Const;

/**
 * Created by IntelliJ IDEA.
 * User: sulic
 * Date: 21.12.2008
 * Time: 20:59:52
 * To change this template use File | Settings | File Templates.
 */
public class Exit extends Action{
    public String execute() throws Exception {
        session.clear();
        return Const.SUCCESS;
    }
}
