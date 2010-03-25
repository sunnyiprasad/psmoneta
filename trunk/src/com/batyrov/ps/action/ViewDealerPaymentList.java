package com.batyrov.ps.action;

import com.batyrov.ps.Const;
import com.batyrov.ps.Dao;
import com.batyrov.ps.bean.DealerPayment;
import com.batyrov.ps.bean.Dealer;

import java.sql.Timestamp;
import java.util.List;
import java.util.Date;
import java.util.Calendar;

/**
 * Created by IntelliJ IDEA.
 * User: sulic
 * Date: 26.12.2008
 * Time: 23:28:37
 * To change this template use File | Settings | File Templates.
 */
public class ViewDealerPaymentList extends Action {
    Date startDate;
    Date endDate;
    List<DealerPayment> payments;

    public List<DealerPayment> getPayments() {
        return payments;
    }

    public void setPayments(List<DealerPayment> payments) {
        this.payments = payments;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String execute() throws Exception {
        super.execute();

        Calendar st = Calendar.getInstance();
        st.set(Calendar.HOUR, 0);
        st.set(Calendar.MINUTE, 0);
        st.set(Calendar.SECOND, 0);
        if (startDate == null)
            startDate = st.getTime();
        st.add(Calendar.DAY_OF_MONTH, 1);
        if (endDate == null)
            endDate = st.getTime();
        try {
            payments = dao.getDealerPayment(myDealerId, new Timestamp(startDate.getTime()), new Timestamp(endDate.getTime()));
            if (payments == null) {
                addActionError(getText("cannot_get_dealer_payment"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            addActionError(e.toString());
        }
        return Const.SUCCESS;
    }
}
