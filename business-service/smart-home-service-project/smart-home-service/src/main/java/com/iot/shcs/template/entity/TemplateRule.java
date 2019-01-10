package com.iot.shcs.template.entity;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import java.io.Serializable;

/**
 * <p>
 * 模板规则表
 * </p>
 *
 * @author laiguiming
 * @since 2018-10-12
 */
public class TemplateRule implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 套包主键
     */
    private Long packageId;
    /**
     * 租户主键
     */
    private Long tenantId;
    /**
     * 模板类型 0安防 1scene 2 ifttt
     */
    private Integer type;
    /**
     * 规则体
     */
    private String json;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPackageId() {
        return packageId;
    }

    public void setPackageId(Long packageId) {
        this.packageId = packageId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
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

    @Override
    public String toString() {
        return "TemplateRule{" +
        ", id=" + id +
        ", packageId=" + packageId +
        ", tenantId=" + tenantId +
        ", type=" + type +
        ", json=" + json +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}";
    }
}
