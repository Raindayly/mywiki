package com.yly.wiki;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
/*
  这个注解其实加了一个 ComponentScan的注解,默认扫描当前包以及子包的类 , 因此一般我们要加上一个注解,默认扫描项目包
  多包扫描支持{"1","2"}语法
 */
@ComponentScan("com.yly")
public class WikiApplication {

    public static void main(String[] args) {
        SpringApplication.run(WikiApplication.class, args);
    }

}
