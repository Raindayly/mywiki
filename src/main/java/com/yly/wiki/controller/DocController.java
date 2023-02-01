package com.yly.wiki.controller;

import com.yly.wiki.entity.Content;
import com.yly.wiki.req.DocQueryReq;
import com.yly.wiki.req.DocSaveReq;
import com.yly.wiki.resp.CommonResp;
import com.yly.wiki.resp.DocResp;
import com.yly.wiki.resp.PageResp;
import com.yly.wiki.service.DocService;
import com.yly.wiki.websocket.WebSocketServer;
import org.springframework.util.ObjectUtils;
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

    @Resource
    private WebSocketServer webSocketServer;


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

    /**
     * @return resp
     * 获取电子书对应的文档
     */
    @GetMapping("/ebook-docs/{ebookId}")
    public CommonResp docs(@PathVariable String ebookId) {
        CommonResp<List<DocResp>> resp = new CommonResp<>();
        List<DocResp> list = docService.listByEbookId(ebookId);
        resp.setContent(list);
        return resp;
    }

    @GetMapping("/content/{id}")
    public CommonResp list(@PathVariable String id) {
        CommonResp<Content> resp = new CommonResp<>();
        Content content = docService.findContent(id);
        if(ObjectUtils.isEmpty(content)){
            //没有内容默认返回空内容
            Content newContent = new Content();
            newContent.setId(id);
            newContent.setContent("");
            resp.setContent(newContent);
        }else {
            resp.setContent(content);
        }
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

    @GetMapping("/vote/{id}")
    public CommonResp vote(@PathVariable String id) {
        CommonResp resp = new CommonResp<>();
        docService.vote(id);

        return resp;
    }
}
