package com.yly.wiki.controller;

import com.yly.wiki.resp.CommonResp;
import com.yly.wiki.service.StatisticsService;
import com.yly.wiki.websocket.WebSocketServer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;


@RestController
@RequestMapping("/statistics")
public class StatisticsController {


    @Resource
    private StatisticsService statisticsService;

    @Resource
    private WebSocketServer webSocketServer;


    /**
     * 统计所有文档，点赞，阅读数，用户数
     */
    @GetMapping("/sum")
    public CommonResp vote() {
        CommonResp resp = new CommonResp<>();
        HashMap sum = statisticsService.sum();
        resp.setContent(sum);
        return resp;
    }
}
