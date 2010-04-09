/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsc.moneta.action;

import com.opensymphony.xwork2.Action;
import com.rsc.moneta.dao.UserDao;
import com.rsc.moneta.dao.AccountDao;
import com.rsc.moneta.action.BaseListAction;
import com.rsc.moneta.bean.Account;
import com.rsc.moneta.bean.User;
import java.util.Collection;

/**
 *
 * @author abdulaev rashid
 */
public class ViewUserAccountsList extends BaseListAction {

    private Collection<Account> accounts;

    @Override
    public String execute() throws Exception {
        User user = (User) session.get("user");
        accounts = user.getAccounts();
        /*
         * TODO: Должна быть проверка пользователя
         * userId = Session.user.id;
         */
        return Action.SUCCESS;
    }

    public Collection<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(Collection<Account> accounts) {
        this.accounts = accounts;
    }
}
