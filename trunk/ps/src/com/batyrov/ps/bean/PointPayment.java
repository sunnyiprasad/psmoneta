package com.batyrov.ps.bean;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by IntelliJ IDEA.
 * User: sulic
 * Date: 16.03.2008
 * Time: 15:23:07
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "T_POINTPAYMENT")
public class PointPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;
    @Column (name = "DATE1")
    private Timestamp date;
    @Column (name = "SUMMA")
    private double summa;
    @Column (name = "DEALERID", insertable = false, nullable = false, updatable = false)
    private long dealerId;
    @Column (name = "POINTID", insertable = false, nullable = false, updatable = false)
    private long pointId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public double getSumma() {
        return summa;
    }

    public void setSumma(double summa) {
        this.summa = summa;
    }

    public long getDealerId() {
        return dealerId;
    }

    public void setDealerId(long dealerId) {
        this.dealerId = dealerId;
    }

    public long getPointiId() {
        return pointId;
    }

    public void setPointiId(long pointId) {
        this.pointId = pointId;
    }

    public PointPayment() {

    }

    public PointPayment(Timestamp date, double summa, long dealerId, long pointId) {
        this.date = date;
        this.summa = summa;
        this.dealerId = dealerId;
        this.pointId = pointId;        
    }

    @ManyToOne
    @JoinColumn(name = "DEALERID")
    private Dealer dealer;

    @ManyToOne
    @JoinColumn(name = "POINTID")
    private Point point;


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
}
