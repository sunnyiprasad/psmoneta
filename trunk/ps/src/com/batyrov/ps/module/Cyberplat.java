package com.batyrov.ps.module;

import org.cyberplat.ipriv.IPriv;
import org.cyberplat.ipriv.data.IPrivSerial;

import java.util.Properties;
import java.util.Date;
import java.net.URL;
import java.net.URLEncoder;
import java.net.URLConnection;
import java.io.*;
import java.text.SimpleDateFormat;

import com.batyrov.ps.bean.AbonentPayment;
import com.batyrov.ps.util.Utils;
import com.batyrov.ps.Dao;


/**
 * Created by IntelliJ IDEA.
 * User: sulic
 * Date: 03.03.2008
 * Time: 22:46:08
 * To change this template use File | Settings | File Templates.
 */


public class Cyberplat implements Processor {
    private String keyDirectory = "c:\\cyberplat\\";
    private String ENC = "";
    private String SD = "";
    private String AP = "";
    private String OP = "";
    private String PASS = "";
    private int BANK_KEY_SERIAL = 0;
    private String PUBKEYS = "";
    private String SECRET = "";
    private IPriv iPriv;
    private Dao dao = new Dao();

    private String getConfigParameter(Properties props, String name) throws IOException {
        if (props.get(name) != null) {
            return (String) props.get(name);
        } else {
            throw new IOException("Not found " + name);
        }
    }

    public Cyberplat() throws IOException {
        Properties props = new Properties();
        FileInputStream config = new FileInputStream(keyDirectory + "cyber.ini");
        props.load(config);
        config.close();
        SD = getConfigParameter(props, "SD");
        AP = getConfigParameter(props, "AP");
        OP = getConfigParameter(props, "OP");
        PUBKEYS = getConfigParameter(props, "PUBKEYS");
        SECRET = getConfigParameter(props, "SECRET");
        PASS = getConfigParameter(props, "PASS");
        ENC = getConfigParameter(props, "ENC");
        BANK_KEY_SERIAL = Integer.parseInt(getConfigParameter(props, "BANK_KEY_SERIAL"));
        iPriv = new IPriv(PASS, keyDirectory + SECRET, keyDirectory + PUBKEYS);
        iPriv.init();
    }

    public String makeRequest(AbonentPayment pay, int req_type) {
        String req = "SD=" + SD + "\r\n";
        req += "AP=" + AP + "\r\n";
        req += "OP=" + OP + "\r\n";
        req += "SESSION=" + pay.getSession() + "\r\n";
        if (pay.getNumber() != null)
            req += "NUMBER=" + pay.getNumber() + "\r\n";
        else
            req += "NUMBER=\r\n";
        if (pay.getAccount() != null)
            req += "ACCOUNT=" + pay.getAccount() + "\r\n";
        else
            req += "ACCOUNT=\r\n";
        req += "AMOUNT=" + pay.getAmount() + "\r\n";
        req += "AMOUNT_ALL=" + pay.getAmountAll() + "\r\n";
        if (pay.getComment() != null)
            req += "COMMENT=" + pay.getComment() + "\r\n";
        else
            req += "COMMENT=\r\n";
        if (req_type >= 0)
            req = req + "REQ_TYPE=" + req_type + "\r\n";
        return req;
    }

    public Properties sendRequest(AbonentPayment pay, int req_type, String url) {
        try {
            String req = makeRequest(pay, req_type);
            req = new String(iPriv.sign(req.getBytes(), 0, req.length()));
            System.out.println(req);
            req = "inputmessage=" + URLEncoder.encode(req, "cp1251");
            URL u = new URL(url);
            URLConnection con = u.openConnection();
            con.setDoOutput(true);
            con.connect();
            con.getOutputStream().write(req.getBytes());
            IPrivSerial serial = new IPrivSerial();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), ENC));
            char[] raw_resp = new char[1024];
            int raw_resp_len = in.read(raw_resp);
            StringBuffer s = new StringBuffer();
            s.append(raw_resp, 0, raw_resp_len);
            String resp = s.toString();
            System.out.println("Replay:  ");
            System.out.println("<'" + resp + "'>");
            System.out.println();
            System.out.println();
            if (iPriv.checkSignature(new DataInputStream(new ByteArrayInputStream(resp.getBytes())), serial)) {
                resp = new String(serial.getPlainData());
                String[] str = resp.split("=|\r\n");
                Properties res = new Properties();
                for (int i = 0; i < str.length / 2; i++) {
                    res.put(str[i * 2], str[i * 2 + 1]);
                }
                return res;
            } else {
                return getCommonError(pay.getSession());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return getCommonError(pay.getSession());
        }
    }

    public Properties check(AbonentPayment pay, int req_type) {
        Properties props = sendRequest(pay, req_type, pay.getProvider().getCheckUrl());
        if ("0".equals(props.getProperty("RESULT")) && "0".equals(props.getProperty("ERROR")))
            pay.setStatus(AbonentPayment.CHECKED);
        else
            pay.setStatus(AbonentPayment.CHECKED_ERROR);
        return props;
    }

    public Properties payment(AbonentPayment pay, int req_type) {
        Properties props = sendRequest(pay, req_type, pay.getProvider().getPaymentUrl());
        if ("0".equals(props.getProperty("RESULT")) && "0".equals(props.getProperty("ERROR")))
            pay.setStatus(AbonentPayment.PAYMENT);
        else
            pay.setStatus(AbonentPayment.PAYMENT_ERROR);
        return props;
    }

    public Properties getStatus(AbonentPayment pay, int req_type){
        Properties props = sendRequest(pay, req_type, pay.getProvider().getGetStatusUrl());
        int result = Utils.getInt(props.getProperty("RESULT"));
        int error = Utils.getInt(props.getProperty("ERROR"));
        if (result == -1) {
            if (error == 11) {
                dao.setPaymentStatus(pay, AbonentPayment.ERROR);
            } else {
                dao.setPaymentStatus(pay, AbonentPayment.UNKNOWN);
            }
        } else {
            if (result < 2)
                dao.setPaymentStatus(pay, AbonentPayment.ERROR);
            else if ((result > 1) && (result < 7))
                dao.setPaymentStatus(pay, AbonentPayment.PROCESSING);
            else if (result == 7) {
                if (error == 0)
                    dao.setPaymentStatus(pay, AbonentPayment.SUCCESS);
                else
                    dao.setPaymentStatus(pay, AbonentPayment.ERROR);
            }else{
                dao.setPaymentStatus(pay, AbonentPayment.UNKNOWN);
            }
        }
        return props;
    }

    public Properties getCommonError(String session) {
        Properties res = Cyberplat.getErrorAnswer(1, 30);
        res.put("SESSION", session);
        return res;
    }

    public static Properties getErrorAnswer(int result, int error) {
        Properties res = new Properties();
        res.put("ERROR", error + "");
        res.put("RESULT", result + "");
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        res.put("DATE", df.format(new Date(System.currentTimeMillis())));
        return res;
    }
}
