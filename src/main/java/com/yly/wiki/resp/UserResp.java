package com.yly.wiki.resp;

import com.yly.wiki.entity.Role;

import java.util.ArrayList;

public class UserResp {
    private String id;

    private String loginName;

    private String nickName;

    private ArrayList<Role> roles;

    private String password;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPassword() {
        return password;
    }

    public ArrayList<Role> getRoles() {
        return roles;
    }

    public void setRoles(ArrayList<Role> roles) {
        this.roles = roles;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public String toString() {
        return "UserResp{" +
                "id='" + id + '\'' +
                ", loginName='" + loginName + '\'' +
                ", nickName='" + nickName + '\'' +
                ", roles='" + roles + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}