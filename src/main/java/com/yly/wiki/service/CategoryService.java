package com.yly.wiki.service;

import com.yly.wiki.entity.Category;
import com.yly.wiki.entity.CategoryExample;
import com.yly.wiki.mapper.CategoryMapper;
import com.yly.wiki.req.CategoryQueryReq;
import com.yly.wiki.req.CategorySaveReq;
import com.yly.wiki.resp.CategoryResp;
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


    public List<CategoryResp> list(CategoryQueryReq categoryReq) {
        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria = categoryExample.createCriteria();

        if(!ObjectUtils.isEmpty(categoryReq.getName())) {
            criteria.andNameLike("%" + categoryReq.getName() + "%");
        }

        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);

        //格式化返回信息
        List<CategoryResp> respList = CopyUtil.copyList(categoryList, CategoryResp.class);


        return respList;
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
