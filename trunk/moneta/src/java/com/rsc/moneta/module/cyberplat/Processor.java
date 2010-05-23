package com.rsc.moneta.module.cyberplat;


import com.rsc.moneta.bean.PSPayment;
import com.rsc.moneta.bean.PSResponse;
import javax.persistence.EntityManager;

/**
 * Created by IntelliJ IDEA.
 * User: sulic
 * Date: 09.03.2008
 * Time: 10:22:16
 * To change this template use File | Settings | File Templates.
 */
public interface Processor {
    void setEntityManager(EntityManager em);
    PSResponse check(PSPayment pay);
    PSResponse payment(PSPayment pay);
    PSResponse getStatus(PSPayment pay);
}
