package com.yunlsp.saas.service.api.manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: 许路路
 * @Date: 2019/3/5
 */
@Controller
public class HomeController {

    @RequestMapping("index")
    public String index() {
        System.out.println("hello world! 你好，index");
        return "index";
    }

    @RequestMapping("home/page01")
    public String page01() {
        System.out.println("hello world! 你好，page01");
        return "home/page01";
    }

    @RequestMapping("home/page02")
    public String page02() {
        System.out.println("hello world! 你好，page02");
        return "home/page02";
    }
}
