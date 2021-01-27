package com.schedule.getmail.core;

import com.schedule.getmail.util.CheckUtil;
import com.schedule.getmail.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * 记录controller的调用日志
 *
 * @author StrTom
 * @since 2020-10-28
 */
@Aspect
@Component
@Slf4j(topic = "controllerLogger")
public class LogAspect {
    private static final ThreadLocal<Long> timeTreadLocal = new ThreadLocal<>();

    @Pointcut("execution(* com.schedule.getmail.controller.*.*(..))")
    private void logger() {
    }

    @Before("logger()")
    public void before(JoinPoint joinPoint) {
        timeTreadLocal.set(System.currentTimeMillis());
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        //获取请求的request
        HttpServletRequest request = attributes.getRequest();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        //获取被拦截的方法
        Method method = methodSignature.getMethod();
        //获取被拦截的方法名
        String methodName = method.getName();
        log.info("Aop begin,请求开始方法：{}", method.getDeclaringClass() + "." + methodName + "()");
        //获取所有请求参数key 和 value
        String keyValue = Utils.getReqParameter(request);
        log.info("请求url = {}", request.getRequestURL().toString());
        log.info("请求ip = {}", request.getRemoteAddr());
        log.info("请求方法requestMethod = {}", request.getMethod());
        log.info("请求资源uri = {}", request.getRequestURI());
        log.info("所有的请求参数 key：value = {}", keyValue);
        log.info("agent = {}", request.getHeader("user-agent"));

        //可以对日志进行入库

    }

    @After("logger()")
    public void after() {
    }

    /**
     * controller请求结束返回时调用
     */
    @AfterReturning(returning = "result", pointcut = "logger()")
    public Object afterReturn(Object result) {
        if (!CheckUtil.isEmpty(result)) {
            long startTime = timeTreadLocal.get();
            double callTime = (System.currentTimeMillis() - startTime) / 1000.0;
            log.info("调用controller花费时间time = {}s", callTime);
            return result;
        } else {
            return null;
        }
    }
}
