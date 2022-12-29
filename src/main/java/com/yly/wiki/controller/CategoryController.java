package com.yly.wiki.controller;

import com.yly.wiki.req.CategoryQueryReq;
import com.yly.wiki.req.CategorySaveReq;
import com.yly.wiki.resp.CommonResp;
import com.yly.wiki.resp.CategoryResp;
import com.yly.wiki.resp.PageResp;
import com.yly.wiki.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;


@RestController
@RequestMapping("/category")
public class CategoryController {


    @Resource
    private CategoryService categoryService;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public CommonResp list(@Valid CategoryQueryReq categoryReq) {
        CommonResp<PageResp<CategoryResp>> resp = new CommonResp<>();
        resp.setContent(categoryService.list(categoryReq));
        return resp;
    }

    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public CommonResp save(@RequestBody CategorySaveReq req) {
        CommonResp resp = new CommonResp<>();
        categoryService.save(req);
        return resp;
    }

    @RequestMapping(value = "/delete/{id}",method = RequestMethod.DELETE)
    public CommonResp delete(@PathVariable String id) {
        CommonResp resp = new CommonResp<>();
        categoryService.delete(id);
        return resp;
    }
}
