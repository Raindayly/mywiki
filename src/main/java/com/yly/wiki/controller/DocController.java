package com.yly.wiki.controller;

import com.yly.wiki.req.DocQueryReq;
import com.yly.wiki.req.DocSaveReq;
import com.yly.wiki.resp.DocResp;
import com.yly.wiki.resp.CommonResp;
import com.yly.wiki.resp.PageResp;
import com.yly.wiki.service.DocService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("/doc")
public class DocController {


    @Resource
    private DocService docService;


    @GetMapping("/all")
    public CommonResp all() {
        CommonResp<List<DocResp>> resp = new CommonResp<>();
        List<DocResp> list = docService.all();
        resp.setContent(list);
        return resp;
    }

    @GetMapping("/list")
    public CommonResp list(@Valid DocQueryReq req) {
        CommonResp<PageResp<DocResp>> resp = new CommonResp<>();
        PageResp<DocResp> list = docService.list(req);
        resp.setContent(list);
        return resp;
    }

    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public CommonResp save(@Valid @RequestBody DocSaveReq req) {
        CommonResp resp = new CommonResp<>();
        docService.save(req);
        return resp;
    }

    @RequestMapping(value = "/delete/{idsStr}",method = RequestMethod.DELETE)
    public CommonResp delete(@PathVariable String idsStr) {
        CommonResp resp = new CommonResp<>();
        List<String> list = Arrays.asList(idsStr.split(","));
        docService.delete(list);
        return resp;
    }
}
