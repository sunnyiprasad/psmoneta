/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsc.moneta.scheduller;

import com.rsc.moneta.bean.Account;
import com.rsc.moneta.bean.CashOrder;
import com.rsc.moneta.dao.AccountDao;
import com.rsc.moneta.dao.CashOrderDao;
import com.rsc.moneta.dao.EMF;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author sulic
 */
public class CashOrderProccess implements Job {

    public void execute(JobExecutionContext jec) throws JobExecutionException {


        EntityManager em = EMF.getEntityManager();
        try {
            CashOrderDao cashOrderDao = new CashOrderDao(em);
            AccountDao accountDao = new AccountDao(em);
            List<Account> accounts = accountDao.getAccountsFilterByAutoCheckoutMoney(Account.EVERYDAY);
            for (Account account : accounts) {
                try {
                    new CashOrderDao(em).checkoutCashOrder(account, account.getBalance());
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        } finally {
            em.close();
        }
    }
}
