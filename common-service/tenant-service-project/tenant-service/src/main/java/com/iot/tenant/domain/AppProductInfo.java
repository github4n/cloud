package com.iot.tenant.domain;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;

import java.io.Serializable;

/**
 * <p>
 * app关联产品配网信息
 * </p>
 *
 * @author laiguiming
 * @since 2018-07-09
 */
public class AppProductInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 配网分类主键
     */
    private Long appProductId;
    /**
     * 文案类型 0 配网文案 1 按钮文案 2 帮助文案
     */
    private Integer type;
    private Integer lang;
    private String content;
    /**
     * 模式 0 SmartConfig模式 1 AP模式
     */
    private Integer mode;
    /**
     * 租户ID
     */
    private Long tenantId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAppProductId() {
        return appProductId;
    }

    public void setAppProductId(Long appProductId) {
        this.appProductId = appProductId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getLang() {
        return lang;
    }

    public void setLang(Integer lang) {
        this.lang = lang;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getMode() {
        return mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public String toString() {
        return "AppProductInfo{" +
                ", id=" + id +
                ", appProductId=" + appProductId +
                ", type=" + type +
                ", lang=" + lang +
                ", content=" + content +
                ", mode=" + mode +
                ", tenantId=" + tenantId +
                "}";
    }
}
