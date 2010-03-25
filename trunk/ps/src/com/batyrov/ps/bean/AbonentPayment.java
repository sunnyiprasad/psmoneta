package com.batyrov.ps.bean;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by IntelliJ IDEA.
 * User: sulic
 * Date: 14.02.2008
 * Time: 22:08:38
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "T_ABONENTPAYMENT")
public class AbonentPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;
    @Column(name = "NUMBER1")
    private String number;
    @Column(name = "ACCOUNT")
    private String account;
    @Column(name = "COMMENT1")
    private String comment;

    @Column(name = "AUTHCODE")
    private String authCode;
    @Column(name = "TRANSID")
    private String transId;
    @Column(name = "STATUS")
    private int status;
    @Column(name = "ERRMSG")
    private int errorMessage;

    @Column(insertable = false, nullable = false, updatable = false, name = "PROVIDERID")
    private long providerId;

    @Column(insertable = false, nullable = false, updatable = false, name = "USERID")
    private long userId;

    @Column(insertable = false, nullable = false, updatable = false, name = "DEALERID")
    private long dealerId;

    @Column(insertable = false, nullable = false, updatable = false, name = "POINTID")
    private long pointId;

    @Column(name = "SESSION1")
    private String session;
    @Column(name = "AMOUNT")
    private double amount;
    @Column(name = "AMOUNT_ALL")
    private double amountAll;
    @Column(name = "DATE1")
    private Timestamp date;

    @Column(name = "STARTDATE")
    private Timestamp startDate;
    @Column(name = "ENDDATE")
    private Timestamp endDate;
    public final static int SUCCESS = 0;
    public static final int NEW = 1;
    public static final int CHECKED = 2;
    public static final int PAYMENT = 3;
    public static final int CHECKED_ERROR = 4;
    public static final int PAYMENT_ERROR = 5;
    public static final int ERROR = 6;
    public static final int UNKNOWN = 7;
    public static final int PROCESSING = 8;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
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

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
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

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(int errorMessage) {
        this.errorMessage = errorMessage;
    }

    @ManyToOne
    @JoinColumn(name = "POINTID")
    private Point point;

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    @ManyToOne
    @JoinColumn(name = "DEALERID")
    private Dealer dealer;

    public Dealer getDealer() {
        return dealer;
    }

    public void setDealer(Dealer dealer) {
        this.dealer = dealer;
    }

    @ManyToOne
    @JoinColumn(name = "PROVIDERID")
    private Provider provider;

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    @ManyToOne
    @JoinColumn(name = "USERID")
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
