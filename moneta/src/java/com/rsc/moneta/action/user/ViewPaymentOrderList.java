/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsc.moneta.action.user;

import com.opensymphony.xwork2.Action;
import com.rsc.moneta.action.BasePeriodListAction;
import com.rsc.moneta.action.admin.SumAndCount;
import com.rsc.moneta.bean.PaymentOrder;
import com.rsc.moneta.dao.PaymentOrderDao;
import java.util.Collection;

/**
 *
 * @author sulic
 */
public class ViewPaymentOrderList extends BasePeriodListAction{

    private Collection<PaymentOrder> paymentOrders;
    private SumAndCount sumAndCount;
    private int status;

    @Override
    public String execute() throws Exception {
        PaymentOrderDao paymentOrderDao = new PaymentOrderDao(em);
        sumAndCount = paymentOrderDao.getPaymentOrdersCountAndSumFilterByStatus(this.getStartDate(), this.getEndDate(), status);
        setListPages(sumAndCount.getCount());
        paymentOrders = paymentOrderDao.getPaymentOrdersPageFilterByStatus(page, this.getStartDate(), this.getEndDate(), status);
        return  Action.SUCCESS;
    }

    public Collection<PaymentOrder> getPaymentOrders() {
        return paymentOrders;
    }

    public void setPaymentOrders(Collection<PaymentOrder> paymentOrders) {
        this.paymentOrders = paymentOrders;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public SumAndCount getSumAndCount() {
        return sumAndCount;
    }

    public void setSumAndCount(SumAndCount sumAndCount) {
        this.sumAndCount = sumAndCount;
    }



}
