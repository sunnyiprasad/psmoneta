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
    private int status = 0;
    private Collection<CashOrder> cashOrders;
    private SumAndCount sumAndCount;

    @Override
    public String execute() throws Exception {
        CashOrderDao cashOrderDao = new CashOrderDao(em);
        sumAndCount = cashOrderDao.getCashOrderCountAndSumFilterByStatus(this.getStartDate(), this.getEndDate(), status);
        setListPages(sumAndCount.getCount());
        cashOrders = cashOrderDao.getCashOrdersPageFilterByStatus(page, getStartDate(), getEndDate(), status);
        return Action.SUCCESS;
    }

    public SumAndCount getSumAndCount() {
        return sumAndCount;
    }

    public void setSumAndCount(SumAndCount sumAndCount) {
        this.sumAndCount = sumAndCount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Collection<CashOrder> getCashOrders() {
        return cashOrders;
    }

    public void setCashOrders(Collection<CashOrder> cashOrders) {
        this.cashOrders = cashOrders;
    }

}