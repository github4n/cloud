package com.iot.portal.web.vo;

import com.iot.device.vo.rsp.ServiceModuleEventResp;
import com.iot.device.vo.rsp.ServiceModulePropertyResp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 18:05 2018/7/3
 * @Modify by:
 */
@ApiModel("功能模组事件信息")
public class PortalEventInfoResp implements Serializable {

    @ApiModelProperty(value = "功能事件信息")
    private ServiceModuleEventResp eventInfo;
    @ApiModelProperty(value = "功能事件属性信息")
    private List<ServiceModulePropertyResp> propertyList;

    public ServiceModuleEventResp getEventInfo() {
        return eventInfo;
    }

    public void setEventInfo(ServiceModuleEventResp eventInfo) {
        this.eventInfo = eventInfo;
    }

    public List<ServiceModulePropertyResp> getPropertyList() {
        return propertyList;
    }

    public void setPropertyList(List<ServiceModulePropertyResp> propertyList) {
        this.propertyList = propertyList;
    }
}
