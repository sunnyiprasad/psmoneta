/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsc.moneta.action.user;

import com.opensymphony.xwork2.Action;
import com.rsc.moneta.action.BaseAction;
import com.rsc.moneta.bean.Account;
import com.rsc.moneta.bean.PaymentOrder;
import com.rsc.moneta.dao.AccountDao;
import java.util.List;

/**
 *
 * @author sulic
 */
public class SelectAccountForPayOrder extends BaseAction {

    private Long accountId;
    private Long paymentOrderId;
    private List<Account> accounts;

    @Override
    public String execute() throws Exception {
        if (paymentOrderId == null) {
            addActionError(getText("payment_order_id_not_defined"));
            return Action.ERROR;
        }
        PaymentOrder paymentOrder = em.find(PaymentOrder.class, paymentOrderId);
        if (paymentOrder == null) {
            addActionError(getText("payment_order_not_found"));
            return Action.ERROR;
        }
        if (accountId == null) {
            accounts = new AccountDao(em).getAccountsWithMoney(user.getId(), paymentOrder.getAmount(), paymentOrder.getCurrency());
            if (accounts.size() == 0) {
                addActionError(getText("you_hasnt_money"));
                return Action.ERROR;
            } else {
                return Action.SUCCESS;
            }
        } else {
            Account account = em.find(Account.class, accountId);
            if (account == null) {
                addActionError(getText("account_not_found"));
                return Action.ERROR;
            } else {
                if (account.getBalance() < paymentOrder.getAmount()) {
                    addActionError(getText("you_hasnt_money"));
                    return Action.ERROR;
                } else {
                    return "next";
                }
            }
        }
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getPaymentOrderId() {
        return paymentOrderId;
    }

    public void setPaymentOrderId(Long paymentOrderId) {
        this.paymentOrderId = paymentOrderId;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }


}
