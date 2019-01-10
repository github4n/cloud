package com.iot.video.vo.redis;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 项目名称：cloud
 * 功能描述：redis中video-plan-info对应实体
 * 创建人： yeshiyuan
 * 创建时间：2018/6/12 10:08
 * 修改人： yeshiyuan
 * 修改时间：2018/6/12 10:08
 * 修改描述：
 */
@ApiModel(value = "redis中video-plan-info对应实体")
public class RedisVideoPlanInfoVo implements Serializable{
    @ApiModelProperty(name = "planId",value = "计划uuid",dataType = "String")
    private String planId;

    @ApiModelProperty(name = "deviceId",value = "设备uuid",dataType = "String")
    private String deviceId;

    @ApiModelProperty(name = "packageEventNumOrFullHour",value = "可录制的事件数量或时间长度，当planType为0时时间长度（单位：小时），当planType为1时可录制事件数量",dataType = "int")
    private Integer packageEventNumOrFullHour;

    @ApiModelProperty(name = "planType",value = "计划类型 0-全时录影，1-事件录影",dataType = "int")
    private Integer planType;

    @ApiModelProperty(name = "usedEventNum",value = "已录制的事件，当planType为1时才有值",dataType = "int")
    private Integer usedEventNum;

    @ApiModelProperty(name = "tenantId",value = "租户id",dataType = "long")
    private Long tenantId;

    @ApiModelProperty(name = "userId", value = "用户ID", dataType = "string")
    private String userId;

    @ApiModelProperty(name = "expireFlag", value = "过期标志（0：未过期，1：已过期）", dataType = "String")
    private String expireFlag;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Integer getPackageEventNumOrFullHour() {
        return packageEventNumOrFullHour;
    }

    public void setPackageEventNumOrFullHour(Integer packageEventNumOrFullHour) {
        this.packageEventNumOrFullHour = packageEventNumOrFullHour;
    }

    public Integer getPlanType() {
        return planType;
    }

    public void setPlanType(Integer planType) {
        this.planType = planType;
    }

    public Integer getUsedEventNum() {
        return usedEventNum;
    }

    public void setUsedEventNum(Integer usedEventNum) {
        this.usedEventNum = usedEventNum;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getExpireFlag() {
        return expireFlag;
    }

    public void setExpireFlag(String expireFlag) {
        this.expireFlag = expireFlag;
    }

    public RedisVideoPlanInfoVo() {
    }

    public RedisVideoPlanInfoVo(String planId,String deviceId,Integer planType, Integer packageEventNumOrFullHour) {
        this.planId = planId;
        this.deviceId = deviceId;
        this.packageEventNumOrFullHour = packageEventNumOrFullHour;
        this.planType = planType;
    }
}
