package com.yly.wiki.req;

public class UserQueryReq extends PageReq {

    private String loginName;

    private String nickName;

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

    @Override
    public String toString() {
        return "UserReq{" +
                "loginName='" + loginName + '\'' +
                ", nickName='" + nickName + '\'' +
                "} " + super.toString();
    }
}