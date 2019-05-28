package com.yunlsp.saas.service.api.portal;

import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author 许路路
 */
@EnableSwagger2
@ImportResource("classpath:dubbo-consumer.xml")
@NacosPropertySource(dataId = "yunlsp_saas_portal", groupId = "saas", autoRefreshed = true)
@SpringBootApplication
public class SaasPortalApplication {

    public static void main(String[] args) {
        SpringApplication.run(SaasPortalApplication.class, args);
    }

}
