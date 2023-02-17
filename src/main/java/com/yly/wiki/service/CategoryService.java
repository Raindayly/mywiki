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
import com.yly.wiki.resp.UserLoginResp;
import com.yly.wiki.util.CopyUtil;
import com.yly.wiki.util.LoginUserContext;
import com.yly.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;


@Service
public class CategoryService {


    private static final Logger LOG = LoggerFactory.getLogger(CategoryService.class);

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private SnowFlake snowFlake;


    public PageResp<CategoryResp> list(CategoryQueryReq req) {
        CategoryExample categoryExample = new CategoryExample();
        categoryExample.setOrderByClause("sort asc");
        CategoryExample.Criteria criteria = categoryExample.createCriteria();
        PageHelper.startPage(req.getPage(), req.getSize());
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);

        PageInfo<Category> pageInfo = new PageInfo<>(categoryList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        // List<CategoryResp> respList = new ArrayList<>();
        // for (Category category : categoryList) {
        //     // CategoryResp categoryResp = new CategoryResp();
        //     // BeanUtils.copyProperties(category, categoryResp);
        //     // 对象复制
        //     CategoryResp categoryResp = CopyUtil.copy(category, CategoryResp.class);
        //
        //     respList.add(categoryResp);
        // }

        // 列表复制
        List<CategoryResp> list = CopyUtil.copyList(categoryList, CategoryResp.class);

        PageResp<CategoryResp> pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);

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

            //获取当前用户
            UserLoginResp user = LoginUserContext.getUser();
            category.setUserId(user.getId());
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

    /**
     * 查询所有分类
     */
    public List<CategoryResp> all() {
        CategoryExample categoryExample = new CategoryExample();
        categoryExample.setOrderByClause("sort asc");
        UserLoginResp user = LoginUserContext.getUser();
        categoryExample.createCriteria().andUserIdEqualTo(user.getId());
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);

        // 列表复制
        List<CategoryResp> list = CopyUtil.copyList(categoryList, CategoryResp.class);
        
        return list;

    }
}
