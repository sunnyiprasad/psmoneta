package com.batyrov.ps.action;

import com.batyrov.ps.bean.PointPayment;
import com.batyrov.ps.Const;

import java.sql.Timestamp;
import java.util.List;
import java.util.Date;
import java.util.Calendar;

/**
 * Created by IntelliJ IDEA.
 * User: sulic
 * Date: 27.12.2008
 * Time: 1:04:01
 * To change this template use File | Settings | File Templates.
 */
public class ViewPointPaymentList extends Action {
    Date startDate;
    Date endDate;
    List<PointPayment> payments;

    public List<PointPayment> getPayments() {
        return payments;
    }

    public void setPayments(List<PointPayment> payments) {
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
        try {
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
            payments = dao.getPointPayment(myDealerId, new Timestamp(startDate.getTime()), new Timestamp(endDate.getTime()));
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
