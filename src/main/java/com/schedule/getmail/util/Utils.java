package com.schedule.getmail.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * 参数解析
 * @author StrTom
 * @since 2020-10-28
 */
public class Utils {

    public static String getReqParameter(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        Enumeration<String> enumeration = request.getParameterNames();
        JSONArray jsonArray = new JSONArray();
        while (enumeration.hasMoreElements()) {
            String key = enumeration.nextElement();
            String value = request.getParameter(key);
            JSONObject json = new JSONObject();
            json.put(key, value);
            jsonArray.add(json);
        }
        return jsonArray.toString();
    }
}
