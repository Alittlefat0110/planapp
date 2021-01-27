package com.schedule.getmail.util;

import microsoft.exchange.webservices.data.property.complex.Attendee;

import java.util.List;

/**
 * list列表，String字符串拼接工具
 */

public class SplitUtil {

    /**
     * 将两个list列表中的字符串拼接成一个String字符串以英文逗号“,”间隔开
     *
     * @Param
     */
    public static String splitUtil(List<Attendee> list1, List<Attendee> list2) {
        java.lang.StringBuilder str1 = new java.lang.StringBuilder();
        java.lang.StringBuilder str2 = new java.lang.StringBuilder();
        if (list1 != null && list1.size() > 0) {
            for (int i = 0; i < list1.size(); i++) {
                str1.append(list1.get(i));
                if (i < list1.size() - 1) {
                    str1.append(",");
                }
            }
        }
        if (list2 != null && list2.size() > 0) {
            for (int i = 0; i < list2.size(); i++) {
                str2.append(list2.get(i));
                if (i < list2.size() - 1) {
                    str2.append(",");
                }
            }
        }
        String Str3 = "";
        String[] arr = {str1.toString(), str2.toString()};
        for (int i = 0; i < arr.length; i++) {
            Str3 += arr[i];
            if (i != arr.length - 1) {
                Str3 += ",";
            }
        }
        return Str3;
    }

    /**
     * 将两个String字符串拼接成一个字符串以英文逗号“,”间隔开
     *
     * @Param
     */
    public static String splitString(String str1, String str2) {
        String str = "";
        if (CheckUtil.isEmpty(str1) && !CheckUtil.isEmpty(str2)) {
            str = str2;
        } else if (CheckUtil.isEmpty(str2) && !CheckUtil.isEmpty(str1)) {
            str = str1;
        } else {
            String[] arr = {str1, str2};
            for (int i = 0; i < arr.length; i++) {
                str += arr[i];
                if (i != arr.length - 1) {
                    str += ",";
                }
            }
        }
        return str;
    }
}
