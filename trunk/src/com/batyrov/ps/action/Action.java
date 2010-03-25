package com.batyrov.ps.action;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ActionContext;
import com.batyrov.ps.Dao;
import com.batyrov.ps.bean.Dealer;
import com.batyrov.ps.bean.Point;
import com.batyrov.ps.bean.Key;
import org.apache.struts2.interceptor.SessionAware;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.util.ServletContextAware;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.SessionMap;


import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletContext;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: sulic
 * Date: 02.03.2008
 * Time: 11:13:52
 * To change this template use File | Settings | File Templates.
 */
public class Action  extends ActionSupport implements ServletContextAware, SessionAware {
    
    protected long myDealerId;
    protected long myPointId;
    protected long myUserId;
    protected long myKeyId;
    protected Dealer dealer;
    protected Map session = null;
    protected EntityManager em;
    protected Dao dao;
    protected ServletContext servletContext;
    protected Point point;
    protected Key key;
    

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public Action() {

    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public long getMyPointId() {
        return myPointId;
    }

    public void setMyPointId(long myPointId) {
        this.myPointId = myPointId;
    }

    public long getMyDealerId() {
        return myDealerId;
    }

    public void setMyDealerId(long myDealerId) {
        this.myDealerId = myDealerId;
    }

    public long getMyUserId() {
        return myUserId;
    }

    public void setMyUserId(long myUserId) {
        this.myUserId = myUserId;
    }

    public long getMyKeyId() {
        return myKeyId;
    }

    public void setMyKeyId(long myKeyId) {
        this.myKeyId = myKeyId;
    }

    public Dealer getDealer() {
        return dealer;
    }

    public void setDealer(Dealer dealer) {
        this.dealer = dealer;
    }

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public Dao getDao() {
        return dao;
    }

    public void setDao(Dao dao) {
        this.dao = dao;
    }

    @Override
    public String execute() throws Exception {
        dao = new Dao();
        em = dao.getEm();
        //session = (SessionMap) ActionContext.getContext().get(ActionContext.SESSION);
        if (session.get("myDealerId") != null){
            myDealerId = (Long)session.get("myDealerId");
            dealer = em.find(Dealer.class, myDealerId);
        }
        if (session.get("myPointId") != null){
            myDealerId = (Long)session.get("myPointId");
            point = em.find(Point.class, myPointId);
        }
        if (session.get("myUserId") != null){
            myUserId = (Long)session.get("myUserId");
        }
        if (session.get("myKeyId") != null){
            myKeyId = (Long)session.get("myKeyId");
            key = em.find(Key.class, myKeyId);
        }
        return super.execute();
    }

    public void setSession(Map map) {
        this.session = map;
    }
}
