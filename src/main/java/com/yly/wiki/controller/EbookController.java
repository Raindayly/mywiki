package com.yly.wiki.controller;

import com.yly.wiki.req.EbookQueryReq;
import com.yly.wiki.req.EbookSaveReq;
import com.yly.wiki.resp.CommonResp;
import com.yly.wiki.resp.EbookResp;
import com.yly.wiki.resp.PageResp;
import com.yly.wiki.service.EbookService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("/ebook")
public class EbookController {


    @Resource
    private EbookService ebookService;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public CommonResp list(EbookQueryReq ebookReq) {
        CommonResp<PageResp<EbookResp>> resp = new CommonResp<>();
        resp.setContent(ebookService.list(ebookReq));
        return resp;
    }

    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public CommonResp save(@RequestBody EbookSaveReq req) {
        CommonResp resp = new CommonResp<>();
        ebookService.save(req);
        return resp;
    }
}
