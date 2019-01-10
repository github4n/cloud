package com.iot.report.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 *@description 用户报表实体类
 *@author wucheng
 *@create 2018/12/29 13:51
 */
@ApiModel(value = "用户报表实体类")
@Data
public class UserActiveEntity {

    @ApiModelProperty(name = "id", value = "主键")
    private Long id;

    @ApiModelProperty(name = "tenantId", value = "租户id")
    private Long tenantId;

    @ApiModelProperty(name = "dataDate", value = "报表日期")
    private Date dataDate;

    @ApiModelProperty(name = "activeAmount", value = "活跃用户数")
    private Long activeAmount;

    @ApiModelProperty(name = "activatedAmount", value = "激活用户数")
    private Long activatedAmount;

    @ApiModelProperty(name = "totalAmount", value = "用户总数")
    private Long totalAmount;

    @ApiModelProperty(name = "createBy", value = "创建人")
    private Long createBy;

    @ApiModelProperty(name = "createTime", value = "创建时间")
    private Date createTime;

    @ApiModelProperty(name = "updateBy", value = "修改人")
    private Long updateBy;

    @ApiModelProperty(name = "updateTime", value = "'修改时间'")
    private Date updateTime;
}
