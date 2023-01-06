package com.yly.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yly.wiki.entity.Doc;
import com.yly.wiki.entity.DocExample;
import com.yly.wiki.mapper.DocMapper;
import com.yly.wiki.req.DocQueryReq;
import com.yly.wiki.req.DocSaveReq;
import com.yly.wiki.resp.DocResp;
import com.yly.wiki.resp.PageResp;
import com.yly.wiki.util.CopyUtil;
import com.yly.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;


@Service
public class DocService {


    private static final Logger LOG = LoggerFactory.getLogger(DocService.class);

    @Resource
    private DocMapper docMapper;

    @Resource
    private SnowFlake snowFlake;


    public PageResp<DocResp> list(DocQueryReq req) {
        DocExample docExample = new DocExample();
        docExample.setOrderByClause("sort asc");
        DocExample.Criteria criteria = docExample.createCriteria();
        PageHelper.startPage(req.getPage(), req.getSize());
        List<Doc> docList = docMapper.selectByExample(docExample);

        PageInfo<Doc> pageInfo = new PageInfo<>(docList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        // List<DocResp> respList = new ArrayList<>();
        // for (Doc doc : docList) {
        //     // DocResp docResp = new DocResp();
        //     // BeanUtils.copyProperties(doc, docResp);
        //     // 对象复制
        //     DocResp docResp = CopyUtil.copy(doc, DocResp.class);
        //
        //     respList.add(docResp);
        // }

        // 列表复制
        List<DocResp> list = CopyUtil.copyList(docList, DocResp.class);

        PageResp<DocResp> pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);

        return pageResp;
    }

    /**
     * 保存
     * @param req
     */
    public void save(DocSaveReq req) {
        Doc doc = CopyUtil.copy(req, Doc.class);
        if(ObjectUtils.isEmpty(req.getId())) {
            doc.setId(String.valueOf(snowFlake.nextId()));
            docMapper.insert(doc);
        }else {
            docMapper.updateByPrimaryKey(doc);
        }
    }

    /**
     * 删除
     * @param id
     */
    public void delete(String id) {
        docMapper.deleteByPrimaryKey(id);
    }

    public void delete(List<String> ids) {
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        criteria.andIdIn(ids);
        docMapper.deleteByExample(docExample);
    }

    /**
     * 查询所有分类
     */
    public List<DocResp> all() {
        DocExample docExample = new DocExample();
        docExample.setOrderByClause("sort asc");
        List<Doc> docList = docMapper.selectByExample(docExample);

        // 列表复制
        List<DocResp> list = CopyUtil.copyList(docList, DocResp.class);
        
        return list;

    }
}
