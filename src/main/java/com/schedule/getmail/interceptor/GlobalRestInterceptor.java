package com.schedule.getmail.interceptor;

import com.schedule.getmail.constant.AppConstant;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.MDC;

@RestControllerAdvice
public class GlobalRestInterceptor implements  HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String traceId = request.getHeader(AppConstant.LOG_TRACE_ID);
        if (traceId == null) {
            traceId = AppConstant.LOG_TRACE_ID;
        }

        MDC.put("traceId", traceId);
        return true;
    }

}
