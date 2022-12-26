package com.yly.wiki.service;

import com.yly.wiki.entity.Test;
import com.yly.wiki.mapper.TestMapper;
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
public class TestService {

//    @Resource 这个是jdk自带的
//    @Autowired //这个是spring的
    @Resource
    private TestMapper testMapper;


    public List<Test> list() {
        return testMapper.list();
    }
}
