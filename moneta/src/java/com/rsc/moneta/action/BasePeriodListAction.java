package com.rsc.moneta.action;



import com.rsc.moneta.util.Month;
import com.rsc.moneta.util.Year;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * User: Suleyman Batyrov
 * Date: 25.01.2007
 * Time: 16:26:16
 */


public class BasePeriodListAction extends BaseListAction  {

    public List getMonths() {
        return Month.getMonths();
    }
    public List getYear() {
        return Year.getYears();
    }


    private int day_begin = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    private int day_end = day_begin;
    private int month_begin = Calendar.getInstance().get(Calendar.MONTH);
    private int month_end = month_begin;
    private int year_begin = Calendar.getInstance().get(Calendar.YEAR);
    private int year_end = year_begin;


    public BasePeriodListAction () {
        GregorianCalendar gcalendar = new GregorianCalendar ();
        gcalendar.add(Calendar.DAY_OF_MONTH, +1);
        day_end = gcalendar.get(Calendar.DAY_OF_MONTH);
        month_end = gcalendar.get(Calendar.MONTH);
        year_end = gcalendar.get(Calendar.YEAR);
    }


    public Timestamp getStartDateTimestamp(){
        Calendar cal = Calendar.getInstance();
        cal.set(year_begin, month_begin, day_begin, 0, 0, 0);
        return new Timestamp(cal.getTimeInMillis());
    }

    public Timestamp getEndDateTimestamp(){
        Calendar cal = Calendar.getInstance();
        cal.set(year_end, month_end, day_end, 0, 0, 0);
        return new Timestamp(cal.getTimeInMillis());
    }

    public Date getStartDate(){
        Calendar cal = Calendar.getInstance();
        cal.set(year_begin, month_begin, day_begin, 0, 0, 0);
        return new Date(cal.getTimeInMillis());
    }

    public Date getEndDate(){
        Calendar cal = Calendar.getInstance();
        cal.set(year_end, month_end, day_end, 0, 0, 0);
        return new Date(cal.getTimeInMillis());
    }

    public int getDay_begin() {
        return day_begin;
    }

    public void setDay_begin(int day_begin) {
        this.day_begin = day_begin;
    }

    public int getDay_end() {
        return day_end;
    }

    public void setDay_end(int day_end) {
        this.day_end = day_end;
    }

    public int getMonth_begin() {
        return month_begin;
    }

    public void setMonth_begin(int month_begin) {
        this.month_begin = month_begin;
    }

    public int getMonth_end() {
        return month_end;
    }

    public void setMonth_end(int month_end) {
        this.month_end = month_end;
    }

    public int getYear_begin() {
        return year_begin;
    }

    public void setYear_begin(int year_begin) {
        this.year_begin = year_begin;
    }

    public int getYear_end() {
        return year_end;
    }

    public void setYear_end(int year_end) {
        this.year_end = year_end;
    }

    @Override
    public String execute() throws Exception {
        return super.execute();
    }
}
