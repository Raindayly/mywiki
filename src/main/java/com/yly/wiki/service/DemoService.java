package com.yly.wiki.service;

import com.yly.wiki.entity.Demo;
import com.yly.wiki.mapper.DemoMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @version 1.0
 * @Author 姚李岩
 * @Date 2022/12/26 14:51
 *
 */

@Service
public class DemoService {


    @Resource
    private DemoMapper demoMapper;


    public List<Demo> list() {
        return demoMapper.selectByExample(null);
    }
}
