/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsc.moneta.action.admin;

import com.opensymphony.xwork2.Action;
import com.rsc.moneta.action.BaseAction;
import com.rsc.moneta.bean.CashOrder;
import com.rsc.moneta.dao.CashOrderDao;
import java.util.Collection;

/**
 *
 * @author sulic
 */
public class ViewCashOrder extends BaseAction{

    private Long cashOrderId;
    private CashOrder cashOrder;

    public String nextUnprocessedCashOrder() throws Exception{
        Collection<CashOrder> cashOrders = new CashOrderDao(em).getCashOrderListFilterByStatus(0);
        if (cashOrders.size() == 0){
            addActionError(getText("cannot_find_unprocessed_cashorder"));
            return Action.ERROR;
        }else{
            cashOrder = (CashOrder) cashOrders.toArray()[0];
            return Action.SUCCESS;
        }
    }

    @Override
    public String execute() throws Exception {
        if (cashOrderId == null){
            addActionError(getText("cashorderid_not_defined"));
            return Action.SUCCESS;
        }
        cashOrder = em.find(CashOrder.class, cashOrderId);
        if (cashOrder == null){
            addActionError(getText("cashorder_not_found"));
            return Action.ERROR;
        }        
        return Action.SUCCESS;
    }

    public Long getCashOrderId() {
        return cashOrderId;
    }

    public void setCashOrderId(Long cashOrderId) {
        this.cashOrderId = cashOrderId;
    }

    public CashOrder getCashOrder() {
        return cashOrder;
    }

    public void setCashOrder(CashOrder cashOrder) {
        this.cashOrder = cashOrder;
    }

}
