package com.schedule.getmail.util;

/**
 * 剔除html的标签获取body的文本内容
 */

public class HtmlUtil {
    public static String getContentFromHtml(String content){
        content = content.replaceAll("</?[^>]+>", ""); //剔出<html>的标签
        content = content.replaceAll("<a>\\s*|\t|\r|\n</a>", "");
        content = content.replaceAll("&nbsp;", "");
        content = content.replaceAll("\n", "");
        return content;
    }

}
