package com.iot.shcs.space.vo;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @description:
 * @program: cloud
 * @return:
 * @author: chq
 * @date: 2018/10/11 14:48
 **/
public class SpaceAndSpaceDeviceVo {

    @NotNull
    private Long tenantId;

    private List<Long> spaceIds;

    private List<String> deviceIds;

    private Long userId;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public List<Long> getSpaceIds() {
        return spaceIds;
    }

    public void setSpaceIds(List<Long> spaceIds) {
        this.spaceIds = spaceIds;
    }

    public List<String> getDeviceIds() {
        return deviceIds;
    }

    public void setDeviceIds(List<String> deviceIds) {
        this.deviceIds = deviceIds;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
