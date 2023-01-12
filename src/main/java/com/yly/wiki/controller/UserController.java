package com.yly.wiki.controller;

import com.yly.wiki.req.UserQueryReq;
import com.yly.wiki.req.UserSaveReq;
import com.yly.wiki.resp.CommonResp;
import com.yly.wiki.resp.PageResp;
import com.yly.wiki.resp.UserResp;
import com.yly.wiki.service.UserService;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("/user")
public class UserController {


    @Resource
    private UserService userService;


    @GetMapping("/all")
    public CommonResp all() {
        CommonResp<List<UserResp>> resp = new CommonResp<>();
        List<UserResp> list = userService.all();
        resp.setContent(list);
        return resp;
    }

    @GetMapping("/list")
    public CommonResp list(@Valid UserQueryReq req) {
        CommonResp<PageResp<UserResp>> resp = new CommonResp<>();
        PageResp<UserResp> list = userService.list(req);
        resp.setContent(list);
        return resp;
    }


    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public CommonResp save(@Valid @RequestBody UserSaveReq req) {
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        CommonResp resp = new CommonResp<>();
        userService.save(req);
        return resp;
    }

    @RequestMapping(value = "/delete/{idsStr}",method = RequestMethod.DELETE)
    public CommonResp delete(@PathVariable String idsStr) {
        CommonResp resp = new CommonResp<>();
        List<String> list = Arrays.asList(idsStr.split(","));
        userService.delete(list);
        return resp;
    }
}
