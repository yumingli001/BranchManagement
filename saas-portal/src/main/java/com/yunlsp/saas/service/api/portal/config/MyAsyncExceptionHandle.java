package com.yunlsp.saas.service.api.portal.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

/**
 * 异步方法调用统一异常处理
 *
 * @author 许路路
 */
public class MyAsyncExceptionHandle implements AsyncUncaughtExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger("MyAsyncExceptionHandle");

    @Override
    public void handleUncaughtException(Throwable throwable, Method method, Object... objects) {
        logger.error("异步方法调用统一异常处理,", throwable);
        logger.error("异常信息:{},方法名称:{},参数:{}", throwable.getMessage(), method.getName(), objects);
    }
}
