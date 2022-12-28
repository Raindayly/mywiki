package com.yly.wiki.service;

import com.yly.wiki.entity.Ebook;
import com.yly.wiki.entity.EbookExample;
import com.yly.wiki.mapper.EbookMapper;
import com.yly.wiki.req.EbookReq;
import com.yly.wiki.resp.EbookResp;
import com.yly.wiki.util.CopyUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;


@Service
public class EbookService {


    @Resource
    private EbookMapper ebookMapper;


    public List<EbookResp> list(EbookReq ebookReq) {
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        if(!ObjectUtils.isEmpty(ebookReq.getName())) {
            criteria.andNameLike("%" + ebookReq.getName() + "%");
        }
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);
        List<EbookResp> respList = CopyUtil.copyList(ebookList, EbookResp.class);
        return respList;
    }
}
