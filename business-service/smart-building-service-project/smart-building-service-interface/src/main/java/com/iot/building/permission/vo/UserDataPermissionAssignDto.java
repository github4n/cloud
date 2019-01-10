package com.iot.building.permission.vo;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @Author: Xieby
 * @Date: 2018/9/7
 * @Description: *
 */
public class UserDataPermissionAssignDto {

    private Long userId;

    private Long roleId;

    private List<JSONObject> list;

    public UserDataPermissionAssignDto () {}

    public UserDataPermissionAssignDto(Long userId, Long roleId, List<JSONObject> list) {
        this.userId = userId;
        this.roleId = roleId;
        this.list = list;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public List<JSONObject> getList() {
        return list;
    }

    public void setList(List<JSONObject> list) {
        this.list = list;
    }
}
