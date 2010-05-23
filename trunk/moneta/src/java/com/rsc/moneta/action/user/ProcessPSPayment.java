/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsc.moneta.action.user;

import com.rsc.moneta.Config;
import com.rsc.moneta.Currency;
import com.rsc.moneta.action.BaseAction;
import com.rsc.moneta.bean.Account;
import com.rsc.moneta.bean.CyberplatPayment;
import com.rsc.moneta.bean.PSPayment;
import com.rsc.moneta.bean.PSResponse;
import com.rsc.moneta.bean.Provider;
import com.rsc.moneta.dao.Dao;
import com.rsc.moneta.dao.PaymentOrderDao;
import com.rsc.moneta.module.cyberplat.Processor;
import java.util.Date;
import java.util.Properties;

/**
 *
 * @author sulic
 */
public class ProcessPSPayment extends BaseAction {

    private Long providerId;
    private Double amount;
    private String number;
    private String account;

    @Override
    public String execute() throws Exception {
        if (providerId == null) {
            addActionError(getText("provider_id_not_defined"));
        } else {
            Provider provider = em.find(Provider.class, providerId);
            if (provider == null) {
                addActionError(getText("provider_not_found"));
            } else {
                Processor proc = null;
                try {
                    proc = (Processor) this.getClass().getClassLoader().loadClass(provider.getClassName()).newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                    addActionError(getText("cannot_init_instance") + e.getMessage());
                    return super.execute();
                }
                if (amount == null) {
                    addActionError(getText("amount_not_defined"));
                } else {
                    if (amount < 0) {
                        addActionError(getText("amount_must_be_more_than_zero"));
                    } else {
                        if (number == null) {
                            addActionError(getText("number_is_not_defined"));
                        } else {
                            Account account = user.getAccount(Currency.RUB);
                            if (account == null) {
                                addActionError(getText("you_hasnt_account"));
                            } else {
                                if (account.getBalance() >= amount) {
                                    PSPayment payment = new PSPayment();
                                    payment.setAmount(amount);
                                    payment.setBill(this.account);
                                    payment.setNumber(number);
                                    payment.setDate(new Date());
                                    payment.setAccount(account);
                                    payment.setStatus(PSResponse.RC_SUCCESS);
                                    new Dao(em).persist(payment);
                                    proc.setEntityManager(em);
                                    PSResponse response = proc.check(payment);
                                    if (response.getResultCode() == PSResponse.RC_SUCCESS) {
                                        response = proc.payment(payment);
                                        if (new PaymentOrderDao(em).addUserAccountBalance(account, 0 - amount)) {
                                            if (response.getResultCode() == PSResponse.RC_SUCCESS){
                                                addActionMessage(getText("psyment_is_success"));
                                            } else {
                                                addActionError(getText("ps_answer_error") + response.getMessage());
                                                new PaymentOrderDao(em).addUserAccountBalance(account, amount);
                                            }
                                        } else {
                                            addActionError(getText("cannot_sub_balance"));
                                        }
                                    } else {
                                        addActionError(getText("ps_answer_error") + response.getMessage());
                                    }
                                } else {
                                    addActionError(getText("you_hasnt_money"));
                                }
                            }
                        }
                    }
                }

            }
        }
        return super.execute();
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Long getProviderId() {
        return providerId;
    }

    public void setProviderId(Long providerId) {
        this.providerId = providerId;
    }

}
