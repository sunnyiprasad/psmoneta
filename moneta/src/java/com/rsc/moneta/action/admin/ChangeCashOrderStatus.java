/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsc.moneta.action.admin;

import com.opensymphony.xwork2.Action;
import com.rsc.moneta.action.BaseAction;
import com.rsc.moneta.action.BasePeriodListAction;
import com.rsc.moneta.bean.CashOrder;
import com.rsc.moneta.dao.Dao;

/**
 *
 * @author sulic
 */
public class ChangeCashOrderStatus extends BaseAction {

    private long cashOrderId;
    private int status;

    public ChangeCashOrderStatus() {
    }

    @Override
    public String execute() throws Exception {
        CashOrder cashOrder = em.find(CashOrder.class, cashOrderId);
        if (cashOrder == null) {
            addActionError(getText("cashorder_not_found"));
            return Action.ERROR;
        }
        cashOrder.setStatus(status);
        if (new Dao(em).persist(cashOrder)) {
            return Action.SUCCESS;
        } else {
            return Action.ERROR;
        }
    }
}
