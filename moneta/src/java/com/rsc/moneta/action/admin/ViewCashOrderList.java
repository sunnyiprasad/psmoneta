/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsc.moneta.action.admin;

import com.opensymphony.xwork2.Action;
import com.rsc.moneta.dao.CashOrderDao;
import com.rsc.moneta.action.BaseListAction;
import com.rsc.moneta.action.BasePeriodListAction;
import com.rsc.moneta.bean.CashOrder;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author sulic
 */

public class ViewCashOrderList extends BasePeriodListAction{

    private Collection<CashOrder> cashOrders;   

    @Override
    public String execute() throws Exception {

        CashOrderDao cashOrderDao = new CashOrderDao(em);
        SumAndCount sumAndCount = cashOrderDao.getCashOrderCountAndSum(this.getStartDate(), this.getEndDate());
        setListPages(sumAndCount.getCount());
        cashOrders = cashOrderDao.getCashOrdersPage(page, getStartDate(), getEndDate());
        return Action.SUCCESS;
    }

    public Collection<CashOrder> getCashOrders() {
        return cashOrders;
    }

    public void setCashOrders(Collection<CashOrder> cashOrders) {
        this.cashOrders = cashOrders;
    }

}