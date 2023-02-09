package com.yly.wiki.mapper;

import com.yly.wiki.resp.StatisticsResp;

import java.util.List;

/**
 * @version 1.0
 * @Author 姚李岩
 * @Date 2022/12/26 14:33
 * 其实是Dao层(持久层)因为mybaties官方生成器会自动生成XXXMapper, 因此用mapper这个包名
 */
public interface StatisticsMapper {

    List<StatisticsResp> getStatistics();
}
