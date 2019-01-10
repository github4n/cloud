package com.iot.control.packagemanager.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@ApiModel(description = "查询套包返回实体类")
@Data
public class PackageResp implements Serializable{

    @ApiModelProperty(name = "id", value = "套包id")
    private Long id;

    @ApiModelProperty(name = "tenantId", value = "租户id")
    private Long tenantId;

    @ApiModelProperty(name = "name", value = "套包名称")
    private String name;

    @ApiModelProperty(name = "productId", value = "产品id")
    private Long productId;

    @ApiModelProperty(name = "createBy", value = "创建人")
    private Long createBy;

    @ApiModelProperty(name = "updateBy", value = "更新人")
    private Long updateBy;

    @ApiModelProperty(name = "createTime", value = "创建时间")
    private Date createTime;

    @ApiModelProperty(name = "updateTime", value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(name = "icon", value = "图标")
    private String icon;

    @ApiModelProperty(name = "description", value = "描述")
    private String description;

    @ApiModelProperty(name = "packageType", value = "套包类型")
    private Integer packageType;

}
