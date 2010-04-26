/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsc.moneta.scheduller;

import com.rsc.moneta.bean.Mail;
import com.rsc.moneta.bean.Market;
import com.rsc.moneta.bean.PaymentTransaction;
import com.rsc.moneta.dao.EMF;
import com.rsc.moneta.dao.MarketDao;
import java.util.Calendar;
import java.util.List;
import javax.persistence.EntityManager;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author sulic
 */
public class SendPaymentList implements Job {

    public void execute(JobExecutionContext jec) throws JobExecutionException {
        EntityManager em = EMF.getEntityManager();
        Calendar today = Calendar.getInstance();
        Calendar yesterday = (Calendar) today.clone();
        yesterday.add(Calendar.DAY_OF_MONTH, -1);
        try {
            List<Market> markets = new MarketDao(em).getAllMarketList();
            for (Market market : markets) {
                if (market.getPaymentListSendEmail() != null) {
                    List<PaymentTransaction> paymentTransactions = new PaymentTransactionDao(em).getPaymentTransaction(yesterday.getTime(), today.getTime());
                    StringBuilder builder = new StringBuilder();
                    double totalAmount = 0;
                    for (PaymentTransaction paymentTransaction : paymentTransactions) {
                        builder.append(paymentTransaction.toString() + "\n\r");
                        totalAmount += paymentTransaction.getAmount();
                    }
                    builder.append("TotalAmount: " + totalAmount);
                    Mail mail = new Mail(market.getPaymentListSendEmail(), "Payment List", builder.toString());
                    mail.setContentType("text/plain");
                    mail.start();
                }
            }
        } finally {
            em.close();
        }
    }
}
