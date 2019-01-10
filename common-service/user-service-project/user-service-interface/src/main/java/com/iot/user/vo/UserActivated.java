package com.iot.user.vo;

import lombok.Data;

/**
 *@description 用户激活缓存的实体类
 *@author wucheng
 *@create 2019/1/4 11:31
 */
@Data
public class UserActivated {

    private String uuid; // 用户uuid

    private Long time; // 激活时间

    private Long tenantId; // 租户id

    public UserActivated(String uuid, Long time, Long tenantId) {
        this.uuid = uuid;
        this.time = time;
        this.tenantId = tenantId;
    }
}
