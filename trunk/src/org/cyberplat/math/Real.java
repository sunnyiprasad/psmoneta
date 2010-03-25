/*
 * Real.java
 *
 * Created on 13 јпрель 2006 г., 15:04
 */

package org.cyberplat.math;


/**
 *
 * @author  Shutov
 * @version
 */
public class Real {
    public static final long PREC = 100000L;
    public static final int SZ_PREC = 5;
    public static final int SZ_PREC_MIN = 2;
    public static final String  SZ_PREC_STR = "00000";
    
    private static final long SQ_PREC = PREC * PREC;
    
    private long value = 0L;
    /** Creates a new instance of Real */
    public Real() {
    }
    /** Creates a new instance of Real */
    public Real(String i_point_d) {
        value = stringToRealValue(i_point_d);
    }
    /** Creates a new instance of Real */
    public Real(int i) {
        value = i * PREC;
    }
    /** Creates a new instance of Real */
    public Real(long l) {
        value = l * PREC;
    }
    /** Creates a new instance of Real */
    public Real(Real r) {
        value = r.value;
    }
    
    /** Creates a new instance of Real */
    public Real set(Real r) {
        value = r.value;
        return this;
    }

    /** Creates a new instance of Real */
    public Real set(int i) {
        value = i * PREC;
        return this;
    }

    /** Creates a new instance of Real */
    public Real set(long l) {
        value = l * PREC;
        return this;
    }
    
    public Real set(String i_point_d) {
        value = stringToRealValue(i_point_d);
        return this;
    }
        
    /** Creates a new instance of Real */
    public static long stringToRealValue(String i_point_d) {
        if (i_point_d == null || i_point_d.equals(""))
            return 0L;
        String s = i_point_d.trim();
        
        long v = 0L;
        String lpart = "";
        String rpart = "";
        
        int p = s.indexOf(".");
        if (p != -1) {
            if (p + 1 < s.length())
                rpart = s.substring(p + 1);
            if (p > 0)
                lpart = s.substring(0, p);
        } else {
            lpart = s;
        }
        
        long val = 0;
        
        if (rpart.equals(""))
            rpart = SZ_PREC_STR;
        else {
            rpart += SZ_PREC_STR;
            rpart = rpart.substring(0, SZ_PREC);
            if (rpart.charAt(SZ_PREC - 1) > '4')
                val = 1;
        }
        
        s = lpart + rpart;
        
        val += Long.parseLong(s, 10);
        return val;
    }
    
    public Real inc(Real a) {
        value += a.value;
        return this;
    }
    
    public Real inc(long a) {
        value += a * PREC;
        return this;
    }
    
    public Real inc() {
        value += PREC;
        return this;
    }

    public Real inc(String i_point_d) {
        return inc(new Real(i_point_d));
    }
    public Real dec(Real a) {
        value -= a.value;
        return this;
    }
    
    public Real dec(long a) {
        value -= a * PREC;
        return this;
    }
    
    public Real dec() {
        value -= PREC;
        return this;
    }

    public Real dec(String i_point_d) {
        return dec(new Real(i_point_d));
    }
    
    /** Creates a new instance of Real */
    public Real sum(Real a) {
        Real result = new Real();
        result.value = value + a.value;
        return result;
    }
    /** Creates a new instance of Real */
    public Real sum(long a) {
        Real result = new Real();
        result.value = value + a * PREC;
        return result;
    }
    /** Creates a new instance of Real */
    public Real dif(Real a) {
        Real result = new Real();
        result.value = value - a.value;
        return result;
    }
    /** Creates a new instance of Real */
    public Real dif(long a) {
        Real result = new Real();
        result.value = value - a * PREC;
        return result;
    }
    /** Creates a new instance of Real */
    public Real neg() {
        Real result = new Real();
        result.value = -value;
        return result;
    }
    /** Creates a new instance of Real */
    public Real mul(Real a) {
        Real result = new Real();
        result.value = value * a.value;
        result.value /= PREC;
        return result;
    }
    /** Creates a new instance of Real */
    public Real mul(long a) {
        Real result = new Real();
        result.value = value * a;
        return result;
    }
    /** Creates a new instance of Real */
    public Real div(Real a) {
        Real result = new Real();
        result.value = (value * PREC)/ a.value;
        return result;
    }
    /** Creates a new instance of Real */
    public Real div(long a) {
        Real result = new Real();
        result.value = value / a;
        return result;
    }

    public boolean eq(Real a) {
        return (value == a.value);
    }

    public boolean gr(Real a) {
        return (value > a.value);
    }

    public boolean gr(long l) {
        return (value > l * PREC);
    }

    public boolean gr(int i) {
        return (value > i * PREC);
    }
    
    public boolean gre(Real a) {
        return (value >= a.value);
    }

    public boolean gre(long l) {
        return (value >= l * PREC);
    }

    public boolean gre(int i) {
        return (value >= i * PREC);
    }
    
    public boolean lt(Real a) {
        return (value < a.value);
    }
    
    public boolean lt(long l) {
        return (value < l * PREC);
    }

    public boolean lt(int i) {
        return (value < i * PREC);
    }
    
    public boolean lte(Real a) {
        return (value <= a.value);
    }

    public boolean lte(long l) {
        return (value <= l * PREC);
    }

    public boolean lte(int i) {
        return (value <= i * PREC);
    }
    

    public boolean eq_zero() {
        return (value == 0);
    }

    public void set_zero() {
        value = 0;
    }
    
    public void clear() {
        value = 0L;
    }
    
    public String toString() {
        return toString(SZ_PREC_MIN, SZ_PREC);
    }

    public String toMoneyFormat() {
        return toString(2, 2);
    }

    public String toString(int rightAlignedSize) {
        return toString(rightAlignedSize, SZ_PREC);
    }
    
    public String toString(int rightAlignedSize, int maxRightSize) {
        if (maxRightSize > SZ_PREC)
            maxRightSize = SZ_PREC;
        else if (maxRightSize < SZ_PREC_MIN)
            maxRightSize = SZ_PREC_MIN;
        
        long MY_PREC;
        
        if (maxRightSize != SZ_PREC) {
            MY_PREC = 1;
            for (int i = 0; i < maxRightSize; i++)
                MY_PREC *= 10;
        } else {
            MY_PREC = PREC;
        }
        
        long v = value;
        
        if (maxRightSize < SZ_PREC) {
            int k = SZ_PREC - maxRightSize;
            for (int i = 0; i < k; i++)
                v = v / 10 + ((v % 10 >= 5)?(1):(0)); /// if >= 5 - используетс€ математическое округление
        }
        
        long d = v % MY_PREC;

        if (d < 0)
            d = -d;
        
        String sd = Long.toString(d, 10);
        
        if (d > 0) {
            for (int i = 0; sd.length() < maxRightSize && i < maxRightSize; i++)
                sd = "0" + sd;
            while(sd.length() > rightAlignedSize && sd.endsWith("0"))
                sd = sd.substring(0, sd.length() - 1);
        }
        
        while(sd.length() < rightAlignedSize)
            sd += '0';
        
        return Long.toString(v / MY_PREC, 10) + "." + sd;
    }
    
    public Real clone() {
        Real result = new Real();
        result.value = value;
        return result;
    }
}
