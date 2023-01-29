package com.yly.wiki.controller;

import com.yly.wiki.entity.Test;
import com.yly.wiki.service.TestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @version 1.0
 * @Author 姚李岩
 * @Date 2022/12/26 12:23
 * @注释
 */
//@Controller//一般用来返回网页
@RestController
public class TestController {

    private static final Logger LOG = LoggerFactory.getLogger(TestController.class);

    @Resource
    private RedisTemplate redisTemplate;

    /*
    读取自定义配置项 :后面为默认值
     */
    @Value("${test.hello:TEST}")
    private String hello;

    @Resource
    private TestService testService;

    @RequestMapping(value = "/hello",method = RequestMethod.GET)
    public String hello() {
        return "helloworld"+ hello;
    }

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public List<Test> list() {
        return testService.list();
    }

    @RequestMapping("/redis/set/{key}/{value}")
    public String set(@PathVariable Long key, @PathVariable String value) {
        redisTemplate.opsForValue().set(key, value, 3600, TimeUnit.SECONDS);
        LOG.info("key: {}, value: {}", key, value);
        return "success";
    }

    @RequestMapping("/redis/get/{key}")
    public Object get(@PathVariable Long key) {
        Object object = redisTemplate.opsForValue().get(key);
        LOG.info("key: {}, value: {}", key, object);
        return object;
    }
}
