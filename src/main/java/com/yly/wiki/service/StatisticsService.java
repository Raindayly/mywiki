package com.yly.wiki.service;

import com.yly.wiki.mapper.MyDocMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;


@Service
public class StatisticsService {

    @Resource
    private MyDocMapper myDocMapper;

    @Resource
    private DocService docService;

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

}
