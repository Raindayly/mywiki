package com.yly.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yly.wiki.entity.Ebook;
import com.yly.wiki.entity.EbookExample;
import com.yly.wiki.mapper.EbookMapper;
import com.yly.wiki.mapper.MyEbookMapper;
import com.yly.wiki.req.EbookQueryReq;
import com.yly.wiki.req.EbookSaveReq;
import com.yly.wiki.resp.EbookResp;
import com.yly.wiki.resp.PageResp;
import com.yly.wiki.resp.UserLoginResp;
import com.yly.wiki.util.CopyUtil;
import com.yly.wiki.util.LoginUserContext;
import com.yly.wiki.util.SnowFlake;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;


@Service
public class EbookService {


    @Resource
    private EbookMapper ebookMapper;

    @Resource
    private MyEbookMapper myEbookMapper;

    @Resource
    private SnowFlake snowFlake;


    public PageResp<EbookResp> list(EbookQueryReq ebookReq) {
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();

        //分页sql查询
        PageHelper.startPage(ebookReq.getPage(), ebookReq.getSize());
        if(!ObjectUtils.isEmpty(ebookReq.getName())) {
            criteria.andNameLike("%" + ebookReq.getName() + "%");
        }

        if (!ObjectUtils.isEmpty(ebookReq.getCategory2Id())) {
            criteria.andCategory2IdEqualTo(ebookReq.getCategory2Id());
        }
        UserLoginResp user = LoginUserContext.getUser();
        criteria.andUserIdEqualTo(user.getId());
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);
        //获取分页信息
        PageInfo<Ebook> ebookPageInfo = new PageInfo<>(ebookList);

        //格式化返回信息
        List<EbookResp> respList = CopyUtil.copyList(ebookList, EbookResp.class);

        //包装分页体
        PageResp<EbookResp> pageResp = new PageResp<>();
        pageResp.setList(respList);
        pageResp.setTotal(ebookPageInfo.getTotal());
        return pageResp;
    }

    /**
     * 保存
     * @param req
     */
    public void save(EbookSaveReq req) {
        Ebook ebook = CopyUtil.copy(req, Ebook.class);
        if(ObjectUtils.isEmpty(req.getId())) {
            ebook.setId(String.valueOf(snowFlake.nextId()));
            //获取当前用户
            UserLoginResp user = LoginUserContext.getUser();
            ebook.setUserId(user.getId());
            ebookMapper.insert(ebook);
        }else {
            ebookMapper.updateByPrimaryKey(ebook);
        }
    }

    /**
     * 删除
     * @param id
     */
    public void delete(String id) {
        ebookMapper.deleteByPrimaryKey(id);
    }


    /**
     * 更新ebook信息，总数，点赞数，阅读数，文档数
     */
    public void updateEbookTiming() {
        myEbookMapper.updateEbookInfo();
    }

}
