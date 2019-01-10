package com.iot.shcs.ifttt.entity;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 联动表
 * </p>
 *
 * @author laiguiming
 * @since 2018-10-17
 */
@Data
@ToString
public class Automation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 图标
     */
    private String icon;
    /**
     * 触发类型
     */
    private String triggerType;
    /**
     * 0禁用 1启用
     */
    private Integer status;
    /**
     * 用户主键
     */
    private Long userId;
    /**
     * 空间主键
     */
    private Long spaceId;
    /**
     * 租户主键
     */
    private Long tenantId;
    /**
     * 程序主键
     */
    private Long appletId;
    /**
     * 是否直连 0否 1是
     */
    private Integer isMulti;
    /**
     * 直连设备主键
     */
    private String directId;
    /**
     * 使能开关延时时间
     */
    private Integer delay;
    /**
     * 时间参数json
     */
    private String timeJson;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * rule主键，用于数据迁移
     */
    private Long ruleId;
    /**
     * ble设备对应的Id与dev_timer_id共同确认autoId
     */
    private Integer devSceneId;
    /**
     * ble设备对应的Id与dev_scene_id共同确认autoId
     */
    private Integer devTimerId;
    /**
     * 是否直连 0否 1是
     */
    private Integer visiable;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTriggerType() {
        return triggerType;
    }

    public void setTriggerType(String triggerType) {
        this.triggerType = triggerType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(Long spaceId) {
        this.spaceId = spaceId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getAppletId() {
        return appletId;
    }

    public void setAppletId(Long appletId) {
        this.appletId = appletId;
    }

    public Integer getIsMulti() {
        return isMulti;
    }

    public void setIsMulti(Integer isMulti) {
        this.isMulti = isMulti;
    }

    public String getDirectId() {
        return directId;
    }

    public void setDirectId(String directId) {
        this.directId = directId;
    }

    public Integer getDelay() {
        return delay;
    }

    public void setDelay(Integer delay) {
        this.delay = delay;
    }

    public String getTimeJson() {
        return timeJson;
    }

    public void setTimeJson(String timeJson) {
        this.timeJson = timeJson;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    @Override
    public String toString() {
        return "Automation{" +
        ", id=" + id +
        ", name=" + name +
        ", icon=" + icon +
        ", triggerType=" + triggerType +
        ", status=" + status +
        ", userId=" + userId +
        ", spaceId=" + spaceId +
        ", tenantId=" + tenantId +
        ", appletId=" + appletId +
        ", isMulti=" + isMulti +
        ", directId=" + directId +
        ", delay=" + delay +
        ", timeJson=" + timeJson +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", ruleId=" + ruleId +
        "}";
    }
}
