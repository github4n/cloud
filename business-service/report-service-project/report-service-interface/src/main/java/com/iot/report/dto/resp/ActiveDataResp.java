package com.iot.report.dto.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *@description 用户激活、活跃，设备激活、活跃查询返回数据
 *@author wucheng
 *@create 2019/1/7 9:32
 */
@ApiModel(value = "用户激活、活跃，设备激活、活跃查询返回数据")
@Data
public class ActiveDataResp implements Serializable{

    @ApiModelProperty(name = "activatedDate", value = "日期")
    private String activatedDate;

    @ApiModelProperty(name = "totalNumber", value = "数量")
    private Long totalNumber;
}
