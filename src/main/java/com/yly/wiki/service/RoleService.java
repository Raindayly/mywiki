package com.yly.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yly.wiki.entity.Role;
import com.yly.wiki.entity.RoleExample;
import com.yly.wiki.mapper.RoleMapper;
import com.yly.wiki.req.RoleQueryReq;
import com.yly.wiki.req.RoleSaveReq;
import com.yly.wiki.resp.PageResp;
import com.yly.wiki.resp.RoleResp;
import com.yly.wiki.util.CopyUtil;
import com.yly.wiki.util.SnowFlake;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;


@Service
public class RoleService {


    @Resource
    private RoleMapper roleMapper;


    @Resource
    private SnowFlake snowFlake;


    public PageResp<RoleResp> list(RoleQueryReq roleReq) {
        RoleExample roleExample = new RoleExample();
        RoleExample.Criteria criteria = roleExample.createCriteria();

        //分页sql查询
        PageHelper.startPage(roleReq.getPage(), roleReq.getSize());
        if(!ObjectUtils.isEmpty(roleReq.getRoleName())) {
            criteria.andRoleNameLike("%" + roleReq.getRoleName() + "%");
        }

        List<Role> roleList = roleMapper.selectByExample(roleExample);
        //获取分页信息
        PageInfo<Role> rolePageInfo = new PageInfo<>(roleList);

        //格式化返回信息
        List<RoleResp> respList = CopyUtil.copyList(roleList, RoleResp.class);

        //包装分页体
        PageResp<RoleResp> pageResp = new PageResp<>();
        pageResp.setList(respList);
        pageResp.setTotal(rolePageInfo.getTotal());
        return pageResp;
    }

    /**
     * 保存
     * @param req
     */
    public void save(RoleSaveReq req) {
        Role role = CopyUtil.copy(req, Role.class);
        if(ObjectUtils.isEmpty(req.getRoleId())) {
            role.setRoleId(String.valueOf(snowFlake.nextId()));
            roleMapper.insert(role);
        }else {
            roleMapper.updateByPrimaryKey(role);
        }
    }

    /**
     * 删除
     * @param id
     */
    public void delete(String id) {
        roleMapper.deleteByPrimaryKey(id);
    }


    /**
     * 获取role
     * @param roleId
     * @return role
     */
    public Role getRoleByRoleId(String roleId) {
        return roleMapper.selectByPrimaryKey(roleId);
    }

    public List<RoleResp> allRoles() {
        RoleExample roleExample = new RoleExample();
        List<Role> roleList = roleMapper.selectByExample(roleExample);

        // 列表复制
        List<RoleResp> list = CopyUtil.copyList(roleList, RoleResp.class);

        return list;
    }
}
