package com.schedule.getmail.util;

import java.util.StringTokenizer;

/**
 * 拆分以英文半角逗号”,“间隔的过滤关键词/邮箱
 * @Param str
 * @return
 */

public class TokenUtil {
    public static String[] tokenString(String str){
        StringTokenizer token = new StringTokenizer(str,"，");
        String [] result = new String[token.countTokens()];
        int i=0;
        while(token.hasMoreTokens())
        {
            result[i++]=token.nextToken();
        }
        return result;
    }
}
