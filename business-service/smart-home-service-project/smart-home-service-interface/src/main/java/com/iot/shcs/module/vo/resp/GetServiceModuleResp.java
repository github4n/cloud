package com.iot.shcs.module.vo.resp;

import com.iot.device.vo.rsp.ServiceModuleListResp;
import com.iot.device.vo.rsp.ServiceModulePropertyResp;
import com.iot.device.vo.rsp.ServiceModuleStyleResp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: xfz @Descrpiton: @Date: 9:15 2018/7/2 @Modify by:
 */
@Data
@ToString
@ApiModel("功能模组信息")
public class GetServiceModuleResp extends ServiceModuleListResp implements Serializable {

    // 是否被选中 默认未选中
    @ApiModelProperty(value = "设备类型id", allowableValues = "true 选中 ,false 未选中")
    private boolean whetherCheck = false;

    @ApiModelProperty(value = "功能组样式")
    private List<ServiceModuleStyleResp> styles;

    // 功能组所有方法
    @ApiModelProperty(value = "功能组方法")
    private List<ListActionListResp> actionList;

    // 功能组所有事件
    @ApiModelProperty(value = "功能组事件")
    private List<ListEventListResp> eventList;

    // 功能组所有属性
    @ApiModelProperty(value = "功能组属性")
    private List<ServiceModulePropertyResp> propertyList;
}
