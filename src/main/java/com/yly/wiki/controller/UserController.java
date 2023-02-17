package com.yly.wiki.controller;

import com.alibaba.fastjson.JSONObject;
import com.yly.wiki.req.*;
import com.yly.wiki.resp.*;
import com.yly.wiki.service.UserService;
import com.yly.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(TestController.class);


    @Resource
    private UserService userService;

    @Resource
    private SnowFlake snowFlake;

    @Resource
    private RedisTemplate redisTemplate;


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

    @PostMapping("/reset-password")
    public CommonResp login(@Valid @RequestBody UserResetPasswordReq req) {
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        userService.resetPassword(req);
        CommonResp resp = new CommonResp<>();
        return resp;
    }

    @PostMapping("/login")
    public CommonResp login(@Valid @RequestBody UserLoginReq req) {
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        CommonResp<UserLoginResp> resp = new CommonResp<>();
        UserLoginResp userLoginResp = userService.login(req);

        Long token = snowFlake.nextId();
        LOG.info("生成单点登录token：{}，并放入redis中", token);
        userLoginResp.setToken(token.toString());
        redisTemplate.opsForValue().set(token.toString(), JSONObject.toJSONString(userLoginResp), 3600 * 24, TimeUnit.SECONDS);

        resp.setContent(userLoginResp);
        return resp;
    }

    @PostMapping("/register")
    public CommonResp register(@Valid @RequestBody UserRegisterReq req) {
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        CommonResp<UserLoginResp> resp = new CommonResp<>();
        resp.setSuccess(userService.register(req));
        return resp;
    }


    @PostMapping("/logout/{token}")
    public CommonResp logout(@PathVariable String token) {
        redisTemplate.delete(token);
        CommonResp resp = new CommonResp<>();
        LOG.info("从redis中删除token: {}", token);
        return resp;
    }

    @GetMapping("/allRoles")
    public CommonResp allRoles() {
        CommonResp<List<RoleResp>> resp = new CommonResp<>();
        List<RoleResp> roleResps = userService.allRoles();
        resp.setContent(roleResps);
        return resp;
    }

}
