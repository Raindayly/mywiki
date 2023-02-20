package com.yly.wiki.service;

import com.yly.wiki.mapper.MyDocMapper;
import com.yly.wiki.mapper.StatisticsMapper;
import com.yly.wiki.resp.StatisticsResp;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;


@Service
public class StatisticsService {
    @Resource
    private MyDocMapper myDocMapper;

    @Resource
    private DocService docService;

    @Resource
    StatisticsMapper statisticsMapper;

    public HashMap sum(){
        Integer docSum = myDocMapper.docSum();
        Integer voteSum = myDocMapper.voteSum();
        Integer viewSum = myDocMapper.viewSum();

        HashMap<String, Integer> map = new HashMap<>();
        map.put("docSum",docSum);
        map.put("voteSum",voteSum);
        map.put("viewSum",viewSum);
        return map;
    }

    public HashMap sumPersonally(String id){
        Integer docSum = myDocMapper.docSumPersonally(id);
        Integer voteSum = myDocMapper.voteSumPersonally(id);
        Integer viewSum = myDocMapper.viewSumPersonally(id);

        HashMap<String, Integer> map = new HashMap<>();
        map.put("docSum",docSum);
        map.put("voteSum",voteSum);
        map.put("viewSum",viewSum);
        return map;
    }



    public List<StatisticsResp> getStatistics(String id){
        List<StatisticsResp> statistics = statisticsMapper.getStatistics(id);
        if(statistics.size() == 1){
            Instant instant_yesterday = LocalDate.now().minusDays(1).atTime(LocalTime.MIDNIGHT).atZone(ZoneId.systemDefault()).toInstant();
            statistics.add(0,new StatisticsResp(Date.from(instant_yesterday),0,0,0,0));
        } else if(statistics.size() == 0){
            Instant instant_yesterday = LocalDate.now().minusDays(1).atTime(LocalTime.MIDNIGHT).atZone(ZoneId.systemDefault()).toInstant();
            statistics.add(new StatisticsResp(Date.from(instant_yesterday),0,0,0,0));
            Instant instant_today = LocalDate.now().minusDays(0).atTime(LocalTime.MIDNIGHT).atZone(ZoneId.systemDefault()).toInstant();
            statistics.add(new StatisticsResp(Date.from(instant_today),0,0,0,0));

        }
        return statistics;
    }


    public List<StatisticsResp> getChart(String id){
        return statisticsMapper.getChart(id);
    }



}
