package com.yly.wiki.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version 1.0
 * @Author 姚李岩
 * @Date 2022/12/26 12:23
 * @注释
 */
//@Controller//一般用来返回网页
@RestController
public class testController {

    @RequestMapping(value = "/hello",method = RequestMethod.GET)
    public String hello() {
        return "helloworld!";
    }
}
