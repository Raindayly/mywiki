package com.yly.wiki.config;

import com.yly.wiki.interceptor.LogInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {

//    @Resource
//    LoginInterceptor loginInterceptor;

//    @Resource
//    ActionInterceptor actionInterceptor;

    @Resource
    LogInterceptor logInterceptor;

    public void addInterceptors(InterceptorRegistry registry) {
        //针对所有的方法进行拦截 除了登录接口
        registry.addInterceptor(logInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/login");
//        registry.addInterceptor(loginInterceptor)
//                .addPathPatterns("/**")
//                .excludePathPatterns(
//                        "/test/**",
//                        "/redis/**",
//                        "/user/login",
//                        "/category/all",
//                        "/ebook/list",
//                        "/doc/all/**",
//                        "/doc/vote/**",
//                        "/doc/find-content/**",
//                        "/ebook-snapshot/**"
//                );

//        registry.addInterceptor(actionInterceptor)
//                .addPathPatterns(
//                        "/*/save",
//                        "/*/delete/**",
//                        "/*/reset-password");
    }
}