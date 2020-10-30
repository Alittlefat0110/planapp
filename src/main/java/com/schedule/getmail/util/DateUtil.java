package com.schedule.getmail.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    /**
     * 获取当年第一天的时间
     * @return
     */
    public static Date getFirstDay() {
        Calendar date = Calendar.getInstance();
        int year = date.get(Calendar.YEAR);
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        //当年第一天
        Date currYearFirst = calendar.getTime();
        return currYearFirst;
    }
}
