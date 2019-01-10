package com.iot.tenant.domain;

import com.baomidou.mybatisplus.enums.IdType;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableId;

import java.io.Serializable;

/**
 * <p>
 * app关联产品
 * </p>
 *
 * @author laiguiming
 * @since 2018-07-09
 */
public class AppProduct implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 关联产品主键
     */
    private Long productId;
    /**
     * 应用ID
     */
    private Long appId;
    /**
     * 配网引导图id smartConfig
     */
    private String smartImg;
    /**
     * ap 配网图fileId
     */
    private String apImg;
    /**
     * 多语言中 已选语言 1,2,3
     */
    private String chooseLang;
    /**
     * 租户ID
     */
    private Long tenantId;
    /**
     * 创建时间
     */
    private Date createTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getSmartImg() {
        return smartImg;
    }

    public void setSmartImg(String smartImg) {
        this.smartImg = smartImg;
    }

    public String getApImg() {
        return apImg;
    }

    public void setApImg(String apImg) {
        this.apImg = apImg;
    }

    public String getChooseLang() {
        return chooseLang;
    }

    public void setChooseLang(String chooseLang) {
        this.chooseLang = chooseLang;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "AppProduct{" +
                ", id=" + id +
                ", productId=" + productId +
                ", appId=" + appId +
                ", smartImg=" + smartImg +
                ", apImg=" + apImg +
                ", chooseLang=" + chooseLang +
                ", tenantId=" + tenantId +
                ", createTime=" + createTime +
                "}";
    }
}
