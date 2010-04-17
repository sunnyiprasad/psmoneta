/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package example;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.ServletActionContext;

/**
 *
 * @author sulic
 */

public class Pay extends ActionSupport{
    private Long MNT_ID;
    private String MNT_COMMAND;
    private String MNT_TRANSACTION_ID;
    private Long MNT_OPERATION_ID;
    private Double MNT_AMOUNT;
    private String MNT_CURRENCY_CODE;
    private Integer MNT_TEST_MODE;
    private String MNT_SIGNATURE;
    private String MNT_USER;
    private String MNT_CUSTOM1;
    private String MNT_CUSTOM2;
    private String MNT_CUSTOM3;
    private int MNT_RESULT_CODE;

    public Pay() {       

    }

    @Override
    public String execute() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        String _paymentSystemUnitId = request.getParameter("paymentSystem.unitId");
        MNT_RESULT_CODE = 200;
        return Action.SUCCESS;
    }

    public Double getMNT_AMOUNT() {
        return MNT_AMOUNT;
    }

    public void setMNT_AMOUNT(Double MNT_AMOUNT) {
        this.MNT_AMOUNT = MNT_AMOUNT;
    }

    public String getMNT_COMMAND() {
        return MNT_COMMAND;
    }

    public void setMNT_COMMAND(String MNT_COMMAND) {
        this.MNT_COMMAND = MNT_COMMAND;
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

    public Long getMNT_ID() {
        return MNT_ID;
    }

    public void setMNT_ID(Long MNT_ID) {
        this.MNT_ID = MNT_ID;
    }

    public Long getMNT_OPERATION_ID() {
        return MNT_OPERATION_ID;
    }

    public void setMNT_OPERATION_ID(Long MNT_OPERATION_ID) {
        this.MNT_OPERATION_ID = MNT_OPERATION_ID;
    }

    public int getMNT_RESULT_CODE() {
        return MNT_RESULT_CODE;
    }

    public void setMNT_RESULT_CODE(int MNT_RESULT_CODE) {
        this.MNT_RESULT_CODE = MNT_RESULT_CODE;
    }

    public String getMNT_SIGNATURE() {
        return MNT_SIGNATURE;
    }

    public void setMNT_SIGNATURE(String MNT_SIGNATURE) {
        this.MNT_SIGNATURE = MNT_SIGNATURE;
    }

    public Integer getMNT_TEST_MODE() {
        return MNT_TEST_MODE;
    }

    public void setMNT_TEST_MODE(Integer MNT_TEST_MODE) {
        this.MNT_TEST_MODE = MNT_TEST_MODE;
    }

    public String getMNT_TRANSACTION_ID() {
        return MNT_TRANSACTION_ID;
    }

    public void setMNT_TRANSACTION_ID(String MNT_TRANSACTION_ID) {
        this.MNT_TRANSACTION_ID = MNT_TRANSACTION_ID;
    }

    public String getMNT_USER() {
        return MNT_USER;
    }

    public void setMNT_USER(String MNT_USER) {
        this.MNT_USER = MNT_USER;
    }

    

}