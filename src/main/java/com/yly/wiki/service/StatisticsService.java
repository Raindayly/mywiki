package com.yly.wiki.service;

import com.yly.wiki.mapper.MyDocMapper;
import com.yly.wiki.mapper.StatisticsMapper;
import com.yly.wiki.resp.StatisticsResp;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
        Integer docSum = docService.all().size();
        Integer voteSum = myDocMapper.voteSum();
        Integer viewSum = myDocMapper.viewSum();

        HashMap<String, Integer> map = new HashMap<>();
        map.put("docSum",docSum);
        map.put("voteSum",voteSum);
        map.put("viewSum",viewSum);
        return map;
    }



    public List<StatisticsResp> getStatistics(){
        return statisticsMapper.getStatistics();
    }

}
