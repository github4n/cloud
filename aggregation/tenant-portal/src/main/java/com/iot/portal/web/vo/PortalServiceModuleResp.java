package com.iot.portal.web.vo;

import com.iot.device.vo.rsp.ServiceModuleListResp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 9:15 2018/7/2
 * @Modify by:
 */
@ApiModel("功能模组信息")
public class PortalServiceModuleResp extends ServiceModuleListResp implements Serializable {

    //是否被选中 默认未选中
    @ApiModelProperty(value = "设备类型id", allowableValues = "true 选中 ,false 未选中")
    private boolean whetherCheck = false;

    //功能组所有方法
    @ApiModelProperty(value = "功能组方法")
    private List<PortalActionListResp> actionList;

    //功能组所有事件
    @ApiModelProperty(value = "功能组事件")
    private List<PortalEventListResp> eventList;

    //功能组所有属性
    @ApiModelProperty(value = "功能组属性")
    private List<PortalPropertyListResp> propertyList;


    public boolean isWhetherCheck() {
        return whetherCheck;
    }

    public void setWhetherCheck(boolean whetherCheck) {
        this.whetherCheck = whetherCheck;
    }

    public List<PortalActionListResp> getActionList() {
        return actionList;
    }

    public void setActionList(List<PortalActionListResp> actionList) {
        this.actionList = actionList;
    }

    public List<PortalEventListResp> getEventList() {
        return eventList;
    }

    public void setEventList(List<PortalEventListResp> eventList) {
        this.eventList = eventList;
    }

    public List<PortalPropertyListResp> getPropertyList() {
        return propertyList;
    }

    public void setPropertyList(List<PortalPropertyListResp> propertyList) {
        this.propertyList = propertyList;
    }
}
