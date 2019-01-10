package com.iot.device.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: xfz
 * @Descrpiton: 功能组
 * @Date: 17:12 2018/7/3
 * @Modify by:
 */
@Data
@ToString
@ApiModel("功能模组基础信息")
public class AddServiceModuleReq implements Serializable {

    //功能组id
    @ApiModelProperty(value = "功能组id")
    private Long serviceModuleId;

    //父功能组id
    @ApiModelProperty(value = "父功能组id")
    private Long parentServiceModuleId;

    //功能组所有方法
    @ApiModelProperty(value = "功能组方法集合")
    private List<AddActionReq> actionList;

    //功能组所有事件
    @ApiModelProperty(value = "功能组事件集合")
    private List<AddEventReq> eventList;

    //功能组所有属性
    @ApiModelProperty(value = "功能组属性集合")
    private List<AddServiceModulePropertyReq> propertyList;

    private Long tenantId;

    private Long createBy;


}
