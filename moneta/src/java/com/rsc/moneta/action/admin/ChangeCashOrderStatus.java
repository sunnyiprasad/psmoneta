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
    private String changeAndNext;


    @Override
    public String execute() throws Exception {
        CashOrder cashOrder = em.find(CashOrder.class, cashOrderId);
        if (cashOrder == null) {
            addActionError(getText("cashorder_not_found"));
            return Action.ERROR;
        }
        if (cashOrder.getStatus() == 0){
            cashOrder.setStatus(status);
        }else{
            addActionError(getText("order_already_processed"));
            return Action.ERROR;
        }
        if (new Dao(em).persist(cashOrder)) {
            if (changeAndNext != null)
                return "next";
            else
                return Action.SUCCESS;
        } else {
            return Action.ERROR;
        }
    }

    public String getChangeAndNext() {
        return changeAndNext;
    }

    public void setChangeAndNext(String changeAndNext) {
        this.changeAndNext = changeAndNext;
    }



    public long getCashOrderId() {
        return cashOrderId;
    }

    public void setCashOrderId(long cashOrderId) {
        this.cashOrderId = cashOrderId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
