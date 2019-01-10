package com.iot.device.vo.req;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 13:51 2018/4/24
 * @Modify by:
 */
public class CommDeviceStatusReq implements Serializable {

    /**
     * 开关，1：开启；0：关闭
     * on_off
     */
    private Integer onOff;
    /**
     * 激活状态（0-未激活，1-已激活）
     * active_status
     */
    private Integer activeStatus;
    /**
     * 激活时间
     * active_time
     */
    private Date activeTime;
    /**
     * 在线状态
     * online_status
     */
    private String onlineStatus;
    /**
     * 租户ID
     * tenant_id
     */
    private Long tenantId;

    public Integer getOnOff() {
        return onOff;
    }

    public void setOnOff(Integer onOff) {
        this.onOff = onOff;
    }

    public Integer getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(Integer activeStatus) {
        this.activeStatus = activeStatus;
    }

    public Date getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(Date activeTime) {
        this.activeTime = activeTime;
    }

    public String getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(String onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }
}
