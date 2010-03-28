
import com.rsc.moneta.action.dao.Dao;
import com.rsc.moneta.action.dao.EMF;
import com.rsc.moneta.bean.Market;
import com.rsc.moneta.bean.User;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import org.junit.Test;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author sulic
 */
public class InitTestData {

    @Test
    public void initTestData() throws MalformedURLException, IOException {
        User user = new User();
        user.setPhone("9285817624");
        user.setPassword("12345");
        EntityManager em = EMF.getEntityManager();
        new Dao(em).persist(user);
        Market market = new Market();
        market.setName("test");
        market.setUser(user);
        new Dao(em).persist(market);
    }

    @Test
    public void test() throws MalformedURLException, IOException {
        
    }
}
