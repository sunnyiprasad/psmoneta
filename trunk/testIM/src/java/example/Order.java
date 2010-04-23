/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package example;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author sulic
 */
@Entity
@Table(name="TestOrder")
public class Order implements Serializable {
    static int STATUS_PAID = 2;
    @OneToOne(mappedBy = "order")
    private Payment payment;
    static int STATUS_INACTIVE = 1;
    public static int STATUS_ACTIVE = 0;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;    
    private double amount;
    private int status;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

   

}
