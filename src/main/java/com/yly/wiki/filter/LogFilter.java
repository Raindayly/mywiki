//package com.yly.wiki.filter;
///*
// 过滤器它其实是servlet的一个概念。servlet它又是容器的一个概念。过滤器是给容器用的,
// 所谓的容器，其实就是我们经常听到的tomcat netty 这些都叫密器。所以我们写的这个过滤器其实是给netty或者tomcat使用的
//*/
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.stereotype.Component;
//
// import javax.servlet.*;
// import javax.servlet.http.HttpServletRequest;
// import java.io.IOException;
//
// @Component
// public class LogFilter implements Filter {
//
//     private static final Logger LOG = LoggerFactory.getLogger(LogFilter.class);
//
//     @Override
//     public void init(FilterConfig filterConfig) throws ServletException {
//
//     }
//
//     /*
//      过滤器的链，项目可能有多个过滤器，所以会有一个链，像链条一样，一环接一环，一个过滤器接一个过滤器
//     */
//     @Override
//     public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//         // 打印请求信息
//         HttpServletRequest request = (HttpServletRequest) servletRequest;
//         LOG.info("------------- LogFilter 开始 -------------");
//         LOG.info("请求地址: {} {}", request.getRequestURL().toString(), request.getMethod());
//         LOG.info("远程地址: {}", request.getRemoteAddr());
//
//         long startTime = System.currentTimeMillis();
//         filterChain.doFilter(servletRequest, servletResponse);
//         LOG.info("------------- LogFilter 结束 耗时：{} ms -------------", System.currentTimeMillis() - startTime);
//     }
// }
