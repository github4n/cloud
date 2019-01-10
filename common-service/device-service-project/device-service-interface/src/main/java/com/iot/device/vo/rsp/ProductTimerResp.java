package com.iot.device.vo.rsp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

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
public class ProductTimerResp {

    @ApiModelProperty(value="唯一id",name="id")
    private Long id;

    @ApiModelProperty(value="定时类型",name="timerType")
    private String timerType;

    @ApiModelProperty(value="产品ID",name="productId")
    private Long productId;

    @ApiModelProperty(value="租户ID",name="tenantId")
    private Long tenantId;

    public ProductTimerResp() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }
}
