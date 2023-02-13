package com.yly.wiki.controller;

import com.yly.wiki.req.RoleQueryReq;
import com.yly.wiki.req.RoleSaveReq;
import com.yly.wiki.resp.CommonResp;
import com.yly.wiki.resp.RoleResp;
import com.yly.wiki.resp.PageResp;
import com.yly.wiki.service.RoleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;


@RestController
@RequestMapping("/role")
public class RoleController {


    @Resource
    private RoleService roleService;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public CommonResp list(@Valid RoleQueryReq roleReq) {
        CommonResp<PageResp<RoleResp>> resp = new CommonResp<>();
        resp.setContent(roleService.list(roleReq));
        return resp;
    }

    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public CommonResp save(@RequestBody RoleSaveReq req) {
        CommonResp resp = new CommonResp<>();
        roleService.save(req);
        return resp;
    }

    @RequestMapping(value = "/delete/{id}",method = RequestMethod.DELETE)
    public CommonResp delete(@PathVariable String id) {
        CommonResp resp = new CommonResp<>();
        roleService.delete(id);
        return resp;
    }
}
