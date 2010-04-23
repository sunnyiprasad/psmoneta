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
import java.util.List;
import org.apache.struts2.ServletActionContext;

/**
 *
 * @author sulic
 */
public class ViewPaymentOrderList extends BasePeriodListAction{

    private Collection<PaymentOrder> paymentOrders;
    private SumAndCount sumAndCount;
    private int status = 0;
    private List statusList;

    @Override
    public String execute() throws Exception {
        statusList = PaymentOrder.getStatusList(ServletActionContext.getContext().getLocale());
        PaymentOrderDao paymentOrderDao = new PaymentOrderDao(em);
        if (status == 0){
            sumAndCount = paymentOrderDao.getPaymentOrdersCountAndSumFilterByUser(this.getStartDate(), this.getEndDate(), user.getId());        
            paymentOrders = paymentOrderDao.getPaymentOrdersPageFilterByUser(page, this.getStartDate(), this.getEndDate(), user.getId());
        }else{
            sumAndCount = paymentOrderDao.getPaymentOrdersCountAndSumFilterByStatusAndUser(this.getStartDate(), this.getEndDate(), status, user.getId());        
            paymentOrders = paymentOrderDao.getPaymentOrdersPageFilterByStatusAndUser(page, this.getStartDate(), this.getEndDate(), status, user.getId());
        }
        setListPages(sumAndCount.getCount());
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

    public List getStatusList() {
        return statusList;
    }

    public void setStatusList(List statusList) {
        this.statusList = statusList;
    }




}
