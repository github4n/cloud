package com.iot.device.vo.rsp.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 *@description  产品关联第三方服务返回实体类
 *@author wucheng
 *@create 2018/12/24 9:05
 */
@ApiModel(value = "产品关联第三方服务返回实体类")
@Data
public class ProductServiceInfoResp implements Serializable{
    @ApiModelProperty(name = "id", value = "id")
    private Long id;

    @ApiModelProperty(name = "productId", value = "产品id")
    private Long productId;

    @ApiModelProperty(name = "serviceId", value = "第三方服务商品goods_info的goods_id")
    private Long serviceId;

    @ApiModelProperty(name = "auditStatus", value = "'审核状态（0:未审核 1:审核未通过 2:审核通过）")
    private Integer auditStatus;

    private Long tenantId;

    @ApiModelProperty(name = "goodsCode", value = "第三方服务商品goods_info的商品编码")
    private String goodsCode;  //商品编码（不可重复）
}
