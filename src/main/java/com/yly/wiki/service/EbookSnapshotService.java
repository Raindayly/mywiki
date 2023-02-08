package com.yly.wiki.service;

import com.yly.wiki.mapper.MyEbookSnapshotMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
public class EbookSnapshotService {


    @Resource
    private MyEbookSnapshotMapper myEbookSnapshotMapper;



    /**
     * 生成电子书快照，一天一次
     */
    public void genEbookSnapshot() {
        myEbookSnapshotMapper.genEbookSnapshot();
    }
}
