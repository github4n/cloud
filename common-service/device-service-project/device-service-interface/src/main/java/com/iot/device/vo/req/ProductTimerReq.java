package com.iot.device.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 *
 * 项目名称：IOT云平台
 * 模块名称：设备
 * 功能描述：产品定时VO
 * 创建人： maochengyuan
 * 创建时间：2018/11/23 9:48
 * 修改人： maochengyuan
 * 修改时间：2018/11/23 9:48
 * 修改描述：
 */
@ApiModel(value="ProductTimerReq", description="产品定时VO")
public class ProductTimerReq {

    @ApiModelProperty(value="定时类型",name="timerType")
    private String timerType;

    @ApiModelProperty(value="产品ID",name="productId")
    private Long productId;

    @ApiModelProperty(value="租户ID",name="tenantId")
    private Long tenantId;

    /**创建时间*/
    @ApiModelProperty(value="创建时间",name="createTime")
    private Date createTime;

    /**创建人*/
    @ApiModelProperty(value="创建人",name="createBy")
    private Long createBy;

    /**修改时间*/
    @ApiModelProperty(value="修改时间",name="updateTime")
    private Date updateTime;

    /**修改人*/
    @ApiModelProperty(value="修改人",name="updateBy")
    private Long updateBy;

    public ProductTimerReq() {

    }

    public ProductTimerReq(String timerType, Long productId, Long tenantId) {
        this.timerType = timerType;
        this.productId = productId;
        this.tenantId = tenantId;
    }

    public String getTimerType() {
        return timerType;
    }

    public void setTimerType(String timerType) {
        this.timerType = timerType;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

}
