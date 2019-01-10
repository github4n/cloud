package com.iot.device.vo.req.uuid;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 项目名称：cloud
 * 功能描述：boss系统申请uuid入参
 * 创建人： yeshiyuan
 * 创建时间：2018/7/11 11:13
 * 修改人： yeshiyuan
 * 修改时间：2018/7/11 11:13
 * 修改描述：
 */
@ApiModel(description = "boss系统申请uuid入参")
public class UuidApplyForBoss {

    @ApiModelProperty(name = "tenantId", value = "租户id", dataType = "long")
    private Long tenantId;

    @ApiModelProperty(name = "userId", value = "用户id", dataType = "long")
    private Long userId;

    @ApiModelProperty(name = "productId", value = "产品id", dataType = "Long")
    private Long productId;

    @ApiModelProperty(name = "createNum", value = "uuid生成数量", dataType = "int")
    private Integer createNum;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getCreateNum() {
        return createNum;
    }

    public void setCreateNum(Integer createNum) {
        this.createNum = createNum;
    }
}
