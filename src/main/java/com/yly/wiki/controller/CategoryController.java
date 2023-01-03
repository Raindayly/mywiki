package com.yly.wiki.controller;

import com.yly.wiki.req.CategoryQueryReq;
import com.yly.wiki.req.CategorySaveReq;
import com.yly.wiki.resp.CategoryResp;
import com.yly.wiki.resp.CommonResp;
import com.yly.wiki.resp.PageResp;
import com.yly.wiki.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/category")
public class CategoryController {


    @Resource
    private CategoryService categoryService;


    @GetMapping("/all")
    public CommonResp all() {
        CommonResp<List<CategoryResp>> resp = new CommonResp<>();
        List<CategoryResp> list = categoryService.all();
        resp.setContent(list);
        return resp;
    }

    @GetMapping("/list")
    public CommonResp list(@Valid CategoryQueryReq req) {
        CommonResp<PageResp<CategoryResp>> resp = new CommonResp<>();
        PageResp<CategoryResp> list = categoryService.list(req);
        resp.setContent(list);
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
