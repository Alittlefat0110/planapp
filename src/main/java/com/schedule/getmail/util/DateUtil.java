package com.schedule.getmail.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    private final static String FULL_DATE = "yyyy-MM-dd HH:mm:ss";
    private final static String DATE_NO_S = "yyyy-MM-dd HH:mm";
    private final static String DATE_HH_MM_SS = "HH:mm:ss";

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

    /**
     * <p class="detail">
     * 以完整格式转换到日期 yyyy-MM-dd HH:mm:ss
     * </p>
     * @param date	日期字符串
     * @return		完整格式日期对象
     */
    public static Date fullParse(String date) {
        if(CheckUtil.isEmpty(date)){
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(FULL_DATE);
        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            return new Date();
        }
    }


    /**
     * <p class="detail">
     * 以完整格式转换到日期 yyyy-MM-dd HH:mm:ss
     * </p>
     * @param date	日期字符串
     * @return		完整格式日期对象
     */
    public static String format(Timestamp date) {
        if(CheckUtil.isEmpty(date)){
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_NO_S);
        try {
            return dateFormat.format(date);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * <p class="detail">
     * 以完整格式转换到日期 yyyy-MM-dd HH:mm:ss
     * </p>
     * @param date	日期字符串
     * @return		完整格式日期对象
     */
    public static String format(Date date) {
        if(CheckUtil.isEmpty(date)){
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_NO_S);
        try {
            return dateFormat.format(date);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * <p class="detail">
     * 以完整格式转换到日期 yyyy-MM-dd HH:mm:ss
     * </p>
     * @param date	日期字符串
     * @return		完整格式日期对象
     */
    public static String formatHHMMSS(Date date) {
        if(CheckUtil.isEmpty(date)){
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_HH_MM_SS);
        try {
            return dateFormat.format(date);
        } catch (Exception e) {
            return null;
        }
    }

}
