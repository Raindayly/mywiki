package com.yly.wiki.controller;

import com.yly.wiki.resp.CommonResp;
import com.yly.wiki.resp.StatisticsResp;
import com.yly.wiki.service.StatisticsService;
import com.yly.wiki.websocket.WebSocketServer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;


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
    public CommonResp sum() {
        CommonResp resp = new CommonResp<>();
        HashMap sum = statisticsService.sum();
        resp.setContent(sum);
        return resp;
    }

    /**
     * 统计个人账号的所有文档，点赞，阅读数，用户数
     */
    @GetMapping("/sumPersonally/{id}")
    public CommonResp sumPersonally(@PathVariable String id) {
        CommonResp resp = new CommonResp<>();
        HashMap sum = statisticsService.sumPersonally(id);
        resp.setContent(sum);
        return resp;
    }

    /**
     * 统计首页大屏
     * [ ] 今日阅读数
     * [ ] 今日点赞数
     * [ ] 总阅读量
     * [ ] 总点赞量
     * [ ] 点赞率
     */
    @GetMapping("/homeStatistics/{id}")
    public CommonResp getStatistics(@PathVariable String id) {
        CommonResp resp = new CommonResp<>();
        List<StatisticsResp> statistics = statisticsService.getStatistics(id);
        resp.setContent(statistics);
        return resp;
    }

    /**
     *
     * 首页统计图 ， 30天的数据
     *
     */

    @GetMapping("/chart/{id}")
    public CommonResp getChart(@PathVariable String id) {
        CommonResp resp = new CommonResp<>();
        List<StatisticsResp> statistics = statisticsService.getChart(id);
        resp.setContent(statistics);
        return resp;
    }
}
