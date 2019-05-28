package com.yunlsp.saas.service.api.portal.config;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.BeansException;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * SsoRedisConfig
 *
 * @author rodxu
 * @date 2018/08/28.
 */
@Configuration
public class SassServerConfig implements ApplicationContextAware, AsyncConfigurer {

    private static final Logger logger = LoggerFactory.getLogger("SassServerConfig");

    public static ApplicationContext context;

    @Override
    public Executor getAsyncExecutor() {
        logger.info("指定异步方法调用线程池Executor:ThreadPoolTaskExecutor");
        return taskExecutor();
    }

    /**
     * 指定异步方法调用统一异常处理
     *
     * @return
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        logger.info("指定异步方法调用统一异常处理:MyAsyncExceptionHandle");
        return new MyAsyncExceptionHandle();
    }

    /**
     * spirng线程池初始化
     *
     * @return
     */
    @Bean("taskExecutor")
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(15);
        executor.setKeepAliveSeconds(30);
        executor.setQueueCapacity(5);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static <T> T getBean(Class<T> beanClazz) {
        return context.getBean(beanClazz);
    }

    @Bean
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        tomcat.addConnectorCustomizers(new MyTomcatConnectorCustomizer());
        return tomcat;
    }

    class MyTomcatConnectorCustomizer implements TomcatConnectorCustomizer {
        @Override
        public void customize(Connector connector) {
            Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
            //就是这一句起作用了
            protocol.setConnectionTimeout(10000);
        }
    }

}
