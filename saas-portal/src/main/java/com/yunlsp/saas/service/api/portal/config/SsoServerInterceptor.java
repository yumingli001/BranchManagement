package com.yunlsp.saas.service.api.portal.config;

import com.yunlsp.saas.service.api.portal.interceptor.SsoInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * LoginInterceptor
 *
 * @author rodxu
 * @date 2017/12/14
 */
@Configuration
public class SsoServerInterceptor extends WebMvcConfigurationSupport {

    private static Logger logger = LoggerFactory.getLogger(SsoServerInterceptor.class);

    @Bean
    public HandlerInterceptor getMyInterceptor() {
        SsoInterceptor login = new SsoInterceptor();
        return login;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getMyInterceptor())
                .excludePathPatterns("/login", "/logout", "/home/*", "/noAuthority", "/code/**", "/forgetPassword",
                        "/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**", "/msgCode/**")
                .addPathPatterns("/**");
        super.addInterceptors(registry);
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/META-INF/resources/")
                .addResourceLocations("classpath:/resources/")
                .addResourceLocations("classpath:/static/")
                .addResourceLocations("classpath:/public/");
        super.addResourceHandlers(registry);
    }

}
