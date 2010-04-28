/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsc.moneta.action.user;

import com.opensymphony.xwork2.Action;
import com.rsc.moneta.action.BaseListAction;
import com.rsc.moneta.bean.Account;
import com.rsc.moneta.module.cyberplat.Provider;
import java.util.Collection;

/**
 *
 * @author abdulaev rashid
 */
public class ViewAccountList extends BaseListAction {

    private Collection<Account> accounts;
    

    @Override
    public String execute() throws Exception {        
        for (Account account : user.getAccounts()) {
            em.refresh(account);
        }
        accounts = user.getAccounts();        
        return Action.SUCCESS;
    }

    public Collection<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(Collection<Account> accounts) {
        this.accounts = accounts;
    }
}
