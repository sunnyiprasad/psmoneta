/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package example;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;
import java.util.Date;
import javax.persistence.EntityManager;

/**
 *
 * @author sulic
 */
public class Handler extends ActionSupport {

    private Long MNT_ID = null;
    private String MNT_TRANSACTION_ID = null;
    private String MNT_CURRENCY_CODE = null;
    private Double MNT_AMOUNT = null;
    private Boolean MNT_TEST_MODE = false;
    private String MNT_DESCRIPTION = null;
    private String MNT_SIGNATURE = null;
    private String MNT_CUSTOM1 = null;
    private String MNT_CUSTOM2 = null;
    private String MNT_CUSTOM3 = null;
    private String MNT_SUCCESS_URL = null;
    private String MNT_FAIL_URL = null;
    private String MNT_COMMAND = null;
    private int MNT_RESULT_CODE;

    public Handler() {
    }

    @Override
    public String input() throws Exception {
        System.out.println("************************************************************");
        return super.input();
    }



    @Override
    public String execute() throws Exception {
        EntityManager em = EMF.getEntityManager();
        if (MNT_COMMAND != null && "CHECK".equals(MNT_COMMAND)) {
            try {
                long id = Long.parseLong(MNT_TRANSACTION_ID);
                Order order = em.find(Order.class, id);
                if (order == null) {
                    MNT_RESULT_CODE = 500;
                    em.close();
                    return Action.SUCCESS;
                }
                if (order.getStatus() == Order.STATUS_INACTIVE || order.getStatus() == Order.STATUS_PAID) {
                    MNT_RESULT_CODE = 200;
                    em.close();
                    return Action.SUCCESS;
                } else if (false) {
                    MNT_RESULT_CODE = 302;
                    em.close();
                    return Action.SUCCESS;
                } else if (order.getStatus() == Order.STATUS_ACTIVE) {
                    if (MNT_AMOUNT != null) {
                        MNT_RESULT_CODE = 100;
                        em.close();
                        return Action.SUCCESS;
                    } else {
                        MNT_RESULT_CODE = 402;
                        em.close();
                        return Action.SUCCESS;
                    }
                } else {
                    MNT_RESULT_CODE = 500;
                    em.close();
                    return Action.SUCCESS;
                }
            } catch (Exception exception) {
                MNT_RESULT_CODE = 302;
                exception.printStackTrace();
                MNT_DESCRIPTION = exception.toString();
                em.close();
                return Action.SUCCESS;
            }
        } else {
            //PAY
            try {
                long id = Long.parseLong(MNT_TRANSACTION_ID);
                Order order = em.find(Order.class, id);
                if (order == null) {
                    MNT_RESULT_CODE = 100;
                    em.close();
                    return Action.SUCCESS;
                }
                if (order.getStatus() == Order.STATUS_ACTIVE) {
                    MNT_RESULT_CODE = 200;
                    Payment payment = new Payment();
                    payment.setAmount(MNT_AMOUNT);
                    payment.setDate(new Date());
                    payment.setOrder(order);
                    new Dao(em).persist(payment);
                    order.setStatus(Order.STATUS_PAID);
                    new Dao(em).persist(order);
                    em.close();
                    return Action.SUCCESS;
                } else if (false) {
                    MNT_RESULT_CODE = 500;
                    em.close();
                    return Action.SUCCESS;
                } else {
                    MNT_RESULT_CODE = 100;
                    em.close();
                    return Action.SUCCESS;
                }
            } catch (Exception e) {
                MNT_RESULT_CODE = 500;
                e.printStackTrace();
                MNT_DESCRIPTION = e.toString();
                em.close();
                return Action.SUCCESS;
            }
        }
    }

    public String getMNT_COMMAND() {
        return MNT_COMMAND;
    }

    public void setMNT_COMMAND(String MNT_COMMAND) {
        this.MNT_COMMAND = MNT_COMMAND;
    }

    public int getMNT_RESULT_CODE() {
        return MNT_RESULT_CODE;
    }

    public void setMNT_RESULT_CODE(int MNT_RESULT_CODE) {
        this.MNT_RESULT_CODE = MNT_RESULT_CODE;
    }

    public Double getMNT_AMOUNT() {
        return MNT_AMOUNT;
    }

    public void setMNT_AMOUNT(Double MNT_AMOUNT) {
        this.MNT_AMOUNT = MNT_AMOUNT;
    }

    public String getMNT_CURRENCY_CODE() {
        return MNT_CURRENCY_CODE;
    }

    public void setMNT_CURRENCY_CODE(String MNT_CURRENCY_CODE) {
        this.MNT_CURRENCY_CODE = MNT_CURRENCY_CODE;
    }

    public String getMNT_CUSTOM1() {
        return MNT_CUSTOM1;
    }

    public void setMNT_CUSTOM1(String MNT_CUSTOM1) {
        this.MNT_CUSTOM1 = MNT_CUSTOM1;
    }

    public String getMNT_CUSTOM2() {
        return MNT_CUSTOM2;
    }

    public void setMNT_CUSTOM2(String MNT_CUSTOM2) {
        this.MNT_CUSTOM2 = MNT_CUSTOM2;
    }

    public String getMNT_CUSTOM3() {
        return MNT_CUSTOM3;
    }

    public void setMNT_CUSTOM3(String MNT_CUSTOM3) {
        this.MNT_CUSTOM3 = MNT_CUSTOM3;
    }

    public String getMNT_DESCRIPTION() {
        return MNT_DESCRIPTION;
    }

    public void setMNT_DESCRIPTION(String MNT_DESCRIPTION) {
        this.MNT_DESCRIPTION = MNT_DESCRIPTION;
    }

    public String getMNT_FAIL_URL() {
        return MNT_FAIL_URL;
    }

    public void setMNT_FAIL_URL(String MNT_FAIL_URL) {
        this.MNT_FAIL_URL = MNT_FAIL_URL;
    }

    public Long getMNT_ID() {
        return MNT_ID;
    }

    public void setMNT_ID(Long MNT_ID) {
        this.MNT_ID = MNT_ID;
    }

    public String getMNT_SIGNATURE() {
        return MNT_SIGNATURE;
    }

    public void setMNT_SIGNATURE(String MNT_SIGNATURE) {
        this.MNT_SIGNATURE = MNT_SIGNATURE;
    }

    public String getMNT_SUCCESS_URL() {
        return MNT_SUCCESS_URL;
    }

    public void setMNT_SUCCESS_URL(String MNT_SUCCESS_URL) {
        this.MNT_SUCCESS_URL = MNT_SUCCESS_URL;
    }

    public Boolean getMNT_TEST_MODE() {
        return MNT_TEST_MODE;
    }

    public void setMNT_TEST_MODE(Boolean MNT_TEST_MODE) {
        this.MNT_TEST_MODE = MNT_TEST_MODE;
    }

    public String getMNT_TRANSACTION_ID() {
        return MNT_TRANSACTION_ID;
    }

    public void setMNT_TRANSACTION_ID(String MNT_TRANSACTION_ID) {
        this.MNT_TRANSACTION_ID = MNT_TRANSACTION_ID;
    }
}
