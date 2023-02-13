package com.yly.wiki.req;

import javax.validation.constraints.NotNull;

public class RoleSaveReq {

    private String roleId;

    @NotNull(message = "【角色名】不能为空")
    private String roleName;


    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return "RoleSaveReq{" +
                "roleName='" + roleName + '\'' +
                '}';
    }
}