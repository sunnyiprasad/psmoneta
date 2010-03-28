/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsc.moneta.action.site;

import com.rsc.moneta.action.BaseAction;

/**
 *
 * @author sulic
 */
public class Logout extends BaseAction{

    @Override
    public String execute() throws Exception {
        session.put("user", null);
        return super.execute();
    }

}
