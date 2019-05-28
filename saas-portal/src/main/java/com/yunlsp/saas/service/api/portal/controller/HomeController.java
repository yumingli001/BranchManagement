package com.yunlsp.saas.service.api.portal.controller;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @Author: 许路路
 * @Date: 2019/3/5
 */
@ApiIgnore
@RestController
public class HomeController {

    @NacosValue(value = "${test:12345}", autoRefreshed = true)
    private String nacosValue;

    @RequestMapping("home/index")
    public void index() {
        System.out.println("hello world! 你好，" + nacosValue);
    }
}
