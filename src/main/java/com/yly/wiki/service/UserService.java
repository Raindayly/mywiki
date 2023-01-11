package com.yly.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yly.wiki.entity.User;
import com.yly.wiki.entity.UserExample;
import com.yly.wiki.mapper.UserMapper;
import com.yly.wiki.req.UserQueryReq;
import com.yly.wiki.req.UserSaveReq;
import com.yly.wiki.resp.PageResp;
import com.yly.wiki.resp.UserResp;
import com.yly.wiki.util.CopyUtil;
import com.yly.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;


@Service
public class UserService {


    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Resource
    private UserMapper userMapper;

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
        List<UserResp> list = CopyUtil.copyList(userList, UserResp.class);

        PageResp<UserResp> pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);

        return pageResp;
    }

    /**
     * 保存
     * @param req
     */
    public void save(UserSaveReq req) {
        User user = CopyUtil.copy(req, User.class);
        if(ObjectUtils.isEmpty(req.getId())) {
            user.setId(String.valueOf(snowFlake.nextId()));
            userMapper.insert(user);
        }else {
            userMapper.updateByPrimaryKey(user);
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

    public User findUser(String id) {
        return userMapper.selectByPrimaryKey(id);
    }

}
