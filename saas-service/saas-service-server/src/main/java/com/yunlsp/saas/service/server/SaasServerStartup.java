package com.yunlsp.saas.service.server;

import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import com.yunlsp.saas.service.api.UserLoginService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.CountDownLatch;

/**
 * @Author: 许路路
 * @Date: 2019/3/4
 */
@MapperScan("com.yunlsp.saas.service.dao")
@EnableAsync
@ImportResource({"classpath:dubbo-provider.xml"})
@NacosPropertySource(dataId = "yunlsp_saas", groupId = "saas", autoRefreshed = true)
@SpringBootApplication
public class SaasServerStartup {

    public static void main(String[] args) throws Exception {
        //SpringApplication application = new SpringApplication(SaasServerStartup.class);
        // 如果是web环境，默认创建AnnotationConfigEmbeddedWebApplicationContext，因此要指定applicationContextClass属性
        //application.setApplicationContextClass(AnnotationConfigApplicationContext.class);
        //application.run(args);

        ConfigurableApplicationContext applicationContext = SpringApplication.run(SaasServerStartup.class, args);
        System.out.println("......SaasServer Startup!");
        UserLoginService loginService = (UserLoginService) applicationContext.getBean("userLoginService");
        String result = loginService.sayHi("world");
        System.out.println(result);
        CountDownLatch latch = new CountDownLatch(1);
        latch.await();
    }
}
