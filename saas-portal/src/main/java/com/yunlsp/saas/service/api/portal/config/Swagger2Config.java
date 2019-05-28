package com.yunlsp.saas.service.api.portal.config;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author Rod
 */
@Configuration
public class Swagger2Config {

    @NacosValue("${swagger.show:true}")
    private boolean is_show;

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .enable(is_show)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.yunlsp.saas.service.api.portal.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("海管家简云saas")
                .description("提供给海管家简云saas的接口项目")
                .termsOfServiceUrl("#")
                .version("1.0")
                .build();
    }
}
