package com.batyrov.ps.servlet;

import com.batyrov.ps.Dao;
import com.batyrov.ps.ipriv.IPriv;
import com.batyrov.ps.util.Utils;
import com.batyrov.ps.module.Cyberplat;
import com.batyrov.ps.bean.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.*;
import java.util.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import org.cyberplat.ipriv.data.IPrivSerial;

/**
 * Created by IntelliJ IDEA.
 * User: sulic
 * Date: 02.03.2008
 * Time: 23:10:39
 * To change this template use File | Settings | File Templates.
 */
public class Servlet extends HttpServlet {
    private long providerId = -1;
    private long dealerId = -1;
    private long pointId = -1;
    private long userId = -1;
    private String session = null;
    private String number = null;
    private String account = null;
    private String comment = null;
    private double amount = 0;
    private double amountAll = 0;
    private Timestamp date = null;
    private int req_type = -1;
    private Provider provider = null;
    private AbonentPayment payment = null;
    private Dealer dealer = null;
    private Point point = null;
    private User user = null;
    private Properties answer = null;
    private OutputStream out = null;
    private HttpServletResponse httpServletResponse = null;
    protected Dao dao = new Dao();

    public void inititialize(){
        providerId = -1;
        dealerId = -1;
        pointId = -1;
        userId = -1;
        session = null;
        number = null;
        account = null;
        comment = null;
        req_type = -1;
        provider = null;
        payment = null;
        dealer = null;
        point = null;
        user = null;
        answer = null;
        out = null;
        httpServletResponse = null;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getAmountAll() {
        return amountAll;
    }

    public void setAmountAll(double amountAll) {
        this.amountAll = amountAll;
    }

    public HttpServletResponse getHttpServletResponse() {
        return httpServletResponse;
    }

    public void setHttpServletResponse(HttpServletResponse httpServletResponse) {
        this.httpServletResponse = httpServletResponse;
    }

    public OutputStream getOut() {
        return out;
    }

    public void setOut(OutputStream out) {
        this.out = out;
    }

    public Properties getAnswer() {
        return answer;
    }

    public void setAnswer(Properties answer) {
        this.answer = answer;
    }

    public Dealer getDealer() {
        return dealer;
    }

    public void setDealer(Dealer dealer) {
        this.dealer = dealer;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public AbonentPayment getPayment() {
        return payment;
    }

    public void setPayment(AbonentPayment payment) {
        this.payment = payment;
    }

    public int getReq_type() {
        return req_type;
    }

    public void setReq_type(int req_type) {
        this.req_type = req_type;
    }

    public long getDealerId() {
        return dealerId;
    }

    public void setDealerId(long dealerId) {
        this.dealerId = dealerId;
    }

    public long getPointId() {
        return pointId;
    }

    public void setPointId(long pointId) {
        this.pointId = pointId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getProviderId() {
        return providerId;
    }

    public void setProviderId(long providerId) {
        this.providerId = providerId;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        inititialize();
        httpServletRequest.setCharacterEncoding("windows-1251");
        this.httpServletResponse = httpServletResponse;
        out = httpServletResponse.getOutputStream();
        String data = httpServletRequest.getParameter("inputmessage");
        IPrivSerial serial = IPriv.getInstance().checkSignature(data); //new IPrivSerial();
        //if (Kernel.getInstance().getIpriv().checkSignature(new DataInputStream(new ByteArrayInputStream(data.getBytes())), serial)) {
        if (serial != null) {
            String resp = new String(serial.getPlainData());
            long pId = Utils.getLong(serial.getOwnerKeyNumber());
            if (pId < 0){
                print(Cyberplat.getErrorAnswer(1, 3));
                return;
            }
            System.out.println(resp);
            String[] str = resp.split("=|\r\n");
            Properties res = new Properties();
            for (int i = 0; i < str.length / 2; i++) {
                res.put(str[i * 2], str[i * 2 + 1]);
            }
            try {
                dealerId = Long.parseLong(res.getProperty("SD"));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                print(Cyberplat.getErrorAnswer(1, 2));
                return;
            }

            try {
                pointId = Long.parseLong(res.getProperty("AP"));
                if (pointId != pId)
                    throw new NumberFormatException("keyserial not equals AP");   
            } catch (NumberFormatException e) {
                e.printStackTrace();
                print(Cyberplat.getErrorAnswer(1, 3));
                return;
            }
            try {
                userId = Long.parseLong(res.getProperty("OP"));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                print(Cyberplat.getErrorAnswer(1, 4));
                return;
            }
            try {
                req_type = Integer.parseInt(res.getProperty("REQ_TYPE"));
            } catch (NumberFormatException e) {
            }
            String dt = res.getProperty("DATE");
            if (dt != null){
                SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                try{
                    Date date = df.parse(dt);
                }catch (ParseException e){
                    System.out.println("\r\n"+dt);
                    e.printStackTrace();
                }
                this.date = new Timestamp(date.getTime());
            }
            session = res.getProperty("SESSION");
            if (session == null) {
                print(Cyberplat.getErrorAnswer(1, 5));
                return;
            }
            number = res.getProperty("NUMBER");
            if (number == null) {
                print(Cyberplat.getErrorAnswer(1, 8));
                return;
            }
            account = res.getProperty("ACCOUNT");
            comment = res.getProperty("COMMENT");
            try {
                amount = Double.parseDouble(res.getProperty("AMOUNT"));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                print(Cyberplat.getErrorAnswer(1, 7));
                return;
            }
            try {
                amountAll = Double.parseDouble(res.getProperty("AMOUNT_ALL"));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            try {
                providerId = Long.parseLong(httpServletRequest.getParameter("PROVIDER"));
            } catch (Exception e) {
                print(Cyberplat.getErrorAnswer(1, 10));
                return;
            }
            provider = dao.getEm().find(Provider.class, providerId);
            payment = dao.getPayment(session);
            dealer = dao.getEm().find(Dealer.class, dealerId);
            point = dao.getEm().find(Point.class, pointId);
            user = dao.getEm().find(User.class, userId);
            if (provider == null) {
                System.err.println("Not found provider: " + providerId);
                print(Cyberplat.getErrorAnswer(1, 10));
                return;
            }
            if (dealer == null) {
                System.err.println("Not found dealer: " + dealerId);
                print(Cyberplat.getErrorAnswer(1, 2));
                return;
            }
            if (point == null) {
                System.err.println("Not found point: " + pointId);
                print(Cyberplat.getErrorAnswer(1, 3));
                return;
            }
            if (user == null) {
                System.err.println("Not found user: " + userId);
                print(Cyberplat.getErrorAnswer(1, 4));
                return;
            }

            if (user.getPointId() != pointId){
                System.err.println("user point: " + user.getPointId() + ", point = " + pointId);
                print(Cyberplat.getErrorAnswer(1, 4));
                return;
            }
            if (point.getDealerId() != dealerId){
                System.err.println("dealer: " + dealerId + ", point dealer = " + point.getDealerId());
                print(Cyberplat.getErrorAnswer(1, 2));
                return;
            }
            
        } else {
            System.err.println("Invalid signature" + data);
            print(Cyberplat.getErrorAnswer(1, 6));
        }

    }

    public void print(Properties res) {
        if (res == null)
            return;
        Enumeration keys = res.keys();
        String buf = "";
        while (keys.hasMoreElements()) {
            String key = keys.nextElement().toString();
            buf += key + "=" + res.getProperty(key) + "\r\n";
        }
        buf += "SESSION=" + session + "\r\n";
                //"SD=" + dealerId + "\r\n" +
                //"AP=" + pointId + "\r\n" +
                //"OP=" + userId + "\r\n";
        byte[] message = buf.getBytes();
        try {
            byte[] signedMessage = IPriv.getInstance().sign(message, 0, message.length);
            httpServletResponse.setContentLength(signedMessage.length);
            out.write(signedMessage);
            System.out.println(new String(signedMessage));
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
