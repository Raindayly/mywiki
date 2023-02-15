package com.yly.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yly.wiki.entity.Role;
import com.yly.wiki.entity.User;
import com.yly.wiki.entity.UserExample;
import com.yly.wiki.exception.BusinessException;
import com.yly.wiki.exception.BusinessExceptionCode;
import com.yly.wiki.mapper.UserMapper;
import com.yly.wiki.req.UserLoginReq;
import com.yly.wiki.req.UserQueryReq;
import com.yly.wiki.req.UserResetPasswordReq;
import com.yly.wiki.req.UserSaveReq;
import com.yly.wiki.resp.PageResp;
import com.yly.wiki.resp.RoleResp;
import com.yly.wiki.resp.UserLoginResp;
import com.yly.wiki.resp.UserResp;
import com.yly.wiki.util.CopyUtil;
import com.yly.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service
public class UserService {


    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Resource
    private UserMapper userMapper;

    @Resource
    private RoleService roleService;

    @Resource
    private SnowFlake snowFlake;


    public PageResp<UserResp> list(UserQueryReq req) {
        UserExample userExample = new UserExample();
        PageHelper.startPage(req.getPage(), req.getSize());
        UserExample.Criteria criteria = userExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getNickName())){
            criteria.andNickNameLike("%" + req.getNickName() + "%");
        }
        if (!ObjectUtils.isEmpty(req.getLoginName())){
            criteria.andLoginNameLike("%" +req.getLoginName() + "%");
        }
        List<User> userList = userMapper.selectByExample(userExample);

        PageInfo<User> pageInfo = new PageInfo<>(userList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());


        ArrayList<UserResp> userResps = new ArrayList<>();
        userList.forEach( item ->{

            String[] roles = item.getRoles().split(",");

            ArrayList<String> rolesIds = new ArrayList<>(Arrays.asList(roles));

            ArrayList<Role> roleS = new ArrayList<>();

            rolesIds.forEach(rolesId -> {
                roleS.add(findRoleByRoleId(rolesId));
            });

            UserResp userResp = new UserResp();
            BeanUtils.copyProperties(item,userResp);
            userResp.setRoles(roleS);
            userResps.add(userResp);
        });

        // List<UserResp> respList = new ArrayList<>();
        // for (User user : userList) {
        //     // UserResp userResp = new UserResp();
        //     // BeanUtils.copyProperties(user, userResp);
        //     // 对象复制
        //     UserResp userResp = CopyUtil.copy(user, UserResp.class);
        //
        //     respList.add(userResp);
        // }

        // 列表复制



        PageResp<UserResp> pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(userResps);

        return pageResp;
    }

    /**
     * 保存
     * @param req
     */
    public void save(@Valid UserSaveReq req) {
        User user = CopyUtil.copy(req, User.class);
        String roleStr = req.getRoles();
        if(ObjectUtils.isEmpty(req.getId())) {
            //新增
            user.setId(String.valueOf(snowFlake.nextId()));
            user.setRoles("0");
            if(ObjectUtils.isEmpty(findUserByLoginName(req.getLoginName()))){
                userMapper.insert(user);
            } else {
                throw new BusinessException(BusinessExceptionCode.USER_LOGIN_NAME_EXIST);
            }
        }else {
            //修改
            user.setLoginName(null);
            user.setPassword(null);
            user.setRoles(roleStr);
            userMapper.updateByPrimaryKeySelective(user);
        }
    }

    /**
     * 删除
     * @param id
     */
    public void delete(String id) {
        userMapper.deleteByPrimaryKey(id);
    }

    public void delete(List<String> ids) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andIdIn(ids);
        userMapper.deleteByExample(userExample);
    }

    /**
     * 查询所有
     */
    public List<UserResp> all() {
        UserExample userExample = new UserExample();
        userExample.setOrderByClause("sort asc");
        List<User> userList = userMapper.selectByExample(userExample);

        // 列表复制
        List<UserResp> list = CopyUtil.copyList(userList, UserResp.class);

        return list;

    }
    /**
     * 查询角色
     */
    public List<RoleResp> allRoles() {
        return roleService.allRoles();
    }

    public User selectByLoginName(String LoginName) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andLoginNameEqualTo(LoginName);
        List<User> userList = userMapper.selectByExample(userExample);
        if (CollectionUtils.isEmpty(userList)) {
            return null;
        } else {
            return userList.get(0);
        }
    }

    public List<User> findUserByLoginName(String loginName) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andLoginNameEqualTo(loginName);
        List<User> users = userMapper.selectByExample(userExample);
        return users;
    }

    public Role findRoleByRoleId(String roleId) {
        return roleService.getRoleByRoleId(roleId);
    }



    public UserLoginResp login(UserLoginReq req) {
        //拿到用户名一致的第一个用户信息
        User userDb = selectByLoginName(req.getLoginName());

        if(ObjectUtils.isEmpty(userDb)){
            LOG.info("用户名不存在, {}", req.getLoginName());
            throw new BusinessException(BusinessExceptionCode.LOGIN_USER_ERROR);
        }else {
            if (userDb.getPassword().equals(req.getPassword())) {
                // 登录成功
                UserLoginResp userLoginResp = CopyUtil.copy(userDb, UserLoginResp.class);
                return userLoginResp;
            } else {
                // 密码不对
                LOG.info("密码不对, 输入密码：{}, 数据库密码：{}", req.getPassword(), userDb.getPassword());
                throw new BusinessException(BusinessExceptionCode.LOGIN_USER_ERROR);
            }
        }


    }

    /**
     * 修改密码
     */
    public void resetPassword(UserResetPasswordReq req) {
        User user = CopyUtil.copy(req, User.class);
        userMapper.updateByPrimaryKeySelective(user);
    }
}
