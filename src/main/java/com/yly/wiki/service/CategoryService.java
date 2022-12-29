package com.yly.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yly.wiki.entity.Category;
import com.yly.wiki.entity.CategoryExample;
import com.yly.wiki.mapper.CategoryMapper;
import com.yly.wiki.req.CategoryQueryReq;
import com.yly.wiki.req.CategorySaveReq;
import com.yly.wiki.resp.CategoryResp;
import com.yly.wiki.resp.PageResp;
import com.yly.wiki.util.CopyUtil;
import com.yly.wiki.util.SnowFlake;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;


@Service
public class CategoryService {


    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private SnowFlake snowFlake;


    public PageResp<CategoryResp> list(CategoryQueryReq categoryReq) {
        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria = categoryExample.createCriteria();

        //分页sql查询
        PageHelper.startPage(categoryReq.getPage(), categoryReq.getSize());
        if(!ObjectUtils.isEmpty(categoryReq.getName())) {
            criteria.andNameLike("%" + categoryReq.getName() + "%");
        }

        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);
        //获取分页信息
        PageInfo<Category> categoryPageInfo = new PageInfo<>(categoryList);

        //格式化返回信息
        List<CategoryResp> respList = CopyUtil.copyList(categoryList, CategoryResp.class);

        //包装分页体
        PageResp<CategoryResp> pageResp = new PageResp<>();
        pageResp.setList(respList);
        pageResp.setTotal(categoryPageInfo.getTotal());
        return pageResp;
    }

    /**
     * 保存
     * @param req
     */
    public void save(CategorySaveReq req) {
        Category category = CopyUtil.copy(req, Category.class);
        if(ObjectUtils.isEmpty(req.getId())) {
            category.setId(String.valueOf(snowFlake.nextId()));
            categoryMapper.insert(category);
        }else {
            categoryMapper.updateByPrimaryKey(category);
        }
    }

    /**
     * 删除
     * @param id
     */
    public void delete(String id) {
        categoryMapper.deleteByPrimaryKey(id);
    }
}
