package com.yly.wiki.service;

import com.yly.wiki.entity.Ebook;
import com.yly.wiki.mapper.EbookMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
public class EbookService {


    @Resource
    private EbookMapper ebookMapper;


    public List<Ebook> list() {
        return ebookMapper.selectByExample(null);
    }
}
