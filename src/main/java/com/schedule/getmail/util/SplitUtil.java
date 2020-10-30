package com.schedule.getmail.util;

/**
 * 拆分以英文半角逗号”,“间隔的过滤关键词/邮箱
 * @Param str
 * @return
 */

public class SplitUtil {
    public static String[] splitWords(String str) {
        String[] result = str.split(",");
        return result;
    }
}
