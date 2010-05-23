/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsc.moneta.action.user;

import com.opensymphony.xwork2.Action;
import com.rsc.moneta.action.BaseAction;
import com.rsc.moneta.bean.PaymentOrder;
import com.rsc.moneta.util.FwMoney;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

/**
 *
 * @author sulic
 */
public class GetInvoice extends BaseAction {

    private Long paymentOrderId;
    private String organization;
    private InputStream inputStream;

    @Override
    public String execute() throws Exception {
        if (paymentOrderId == null) {
            addActionError(getText("order_not_defined"));
            return Action.ERROR;
        }
        PaymentOrder paymentOrder = em.find(PaymentOrder.class, paymentOrderId);
        if (paymentOrder == null) {
            addActionError(getText("order_not_found"));
            return Action.ERROR;
        }
        Map params = new HashMap();
        params.put("ExpireDate", paymentOrder.getExpireDate());
        params.put("InvoiceNumber", paymentOrder.getId());
        params.put("Customer", organization);
        FwMoney fw = new FwMoney(paymentOrder.getAmount());
        params.put("TotalInWords", fw.num2str());        
        JasperReport jasperReport = JasperCompileManager.compileReport(servletContext.getRealPath("./reports/invoice.jrxml"));
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
                new HashMap(), new JREmptyDataSource());
        inputStream = new ByteArrayInputStream(JasperExportManager.exportReportToPdf(jasperPrint));
        return Action.SUCCESS;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public Long getPaymentOrderId() {
        return paymentOrderId;
    }

    public void setPaymentOrderId(Long paymentOrderId) {
        this.paymentOrderId = paymentOrderId;
    }
}
