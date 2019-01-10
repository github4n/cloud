package com.iot.tenant.domain;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;

import java.io.Serializable;

/**
 * <p>
 * 多语言管理
 * </p>
 *
 * @author laiguiming
 * @since 2018-07-09
 */
public class LangInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 应用主键
     */
    private Long appId;
    /**
     * key值
     */
    private String key;
    /**
     * 语言标识
     */
    private Integer lang;
    /**
     * 内容
     */
    private String content;
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

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public String toString() {
        return "LangInfo{" +
                ", id=" + id +
                ", appId=" + appId +
                ", key=" + key +
                ", lang=" + lang +
                ", content=" + content +
                ", tenantId=" + tenantId +
                "}";
    }
}
