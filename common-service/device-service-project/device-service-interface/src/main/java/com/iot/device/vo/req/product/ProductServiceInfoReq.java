package com.iot.device.vo.req.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 *@description 产品关联第三方服务实体类
 *@author wucheng
 *@create 2018/12/21 17:08
 */
@ApiModel(value = "产品关联第三方服务")
@Data
public class ProductServiceInfoReq {

    @ApiModelProperty(name = "id", value = "id")
    private Long id;

    @ApiModelProperty(name = "tenantId", value = "租户id")
    private Long tenantId;

    @ApiModelProperty(name = "productId", value = "产品id")
    private Long productId;

    @ApiModelProperty(name = "serviceId", value = "第三方服务商品goods_info的goods_id")
    private Long serviceId;

    @ApiModelProperty(name = "createBy", value = "创建者")
    private Long createBy;

    @ApiModelProperty(name = "createTime", value = "创建时间")
    private Date createTime;

    @ApiModelProperty(name = "updateBy", value = "更新人")
    private Long updateBy;

    @ApiModelProperty(name = "updateTime", value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(name = "auditStatus", value = "'审核状态（0:未审核 1:审核未通过 2:审核通过）")
    private Integer auditStatus;
}
