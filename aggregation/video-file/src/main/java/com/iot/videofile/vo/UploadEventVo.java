package com.iot.videofile.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 *@description 上报事件实体bean
 *@author wucheng
 *@create 2018/12/19 14:23
 */
@ApiModel(value = "上报事件实体bean")
@Data
public class UploadEventVo {

    @ApiModelProperty(name = "evtId", value = "事件id")
    private String evtId;

    @ApiModelProperty(name = "evtType", value = "事件类型")
    private String evtType;

    @ApiModelProperty(name = "triggerTime", value = "触发时间")
    private Date triggerTime;

    @ApiModelProperty(name = "planId", value = "计划时间")
    private String planId;

    @ApiModelProperty(name = "devName", value = "设备名称")
    private String devName;
}
