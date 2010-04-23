/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsc.moneta.action.user;

import com.opensymphony.xwork2.Action;
import com.rsc.moneta.action.BaseAction;
import com.rsc.moneta.action.BasePeriodListAction;
import com.rsc.moneta.action.admin.SumAndCount;
import com.rsc.moneta.bean.PaymentOrder;
import com.rsc.moneta.bean.User;
import com.rsc.moneta.dao.PaymentOrderDao;
import java.util.Collection;
import java.util.List;
import org.apache.struts2.ServletActionContext;

/**
 *
 * @author sulic
 */
public class ViewIMPaymentOrderList extends BasePeriodListAction {

    private Collection<PaymentOrder> paymentOrders;
    private SumAndCount sumAndCount;
    private int status = 0;
    private List statusList;

    @Override
    public String execute() throws Exception {
        if (user.getRole() == User.IMOWNER) {
            statusList = PaymentOrder.getStatusList(ServletActionContext.getContext().getLocale());
            PaymentOrderDao paymentOrderDao = new PaymentOrderDao(em);
            if (status == 0) {
                sumAndCount = paymentOrderDao.getPaymentOrdersCountAndSumFilterByUser(this.getStartDate(), this.getEndDate(), user.getId());
                paymentOrders = paymentOrderDao.getPaymentOrdersPageFilterByUser(page, this.getStartDate(), this.getEndDate(), user.getId());
            } else {
                sumAndCount = paymentOrderDao.getPaymentOrdersCountAndSumFilterByStatusAndUser(this.getStartDate(), this.getEndDate(), status, user.getId());
                paymentOrders = paymentOrderDao.getPaymentOrdersPageFilterByStatusAndUser(page, this.getStartDate(), this.getEndDate(), status, user.getId());
            }
            setListPages(sumAndCount.getCount());
            return Action.SUCCESS;
        } else {
            addActionError(getText("you_are_not_emarketplace_owner"));
            return Action.ERROR;
        }
    }
}
