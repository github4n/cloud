package com.iot.device.vo.req;

import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: xfz
 * @Descrpiton: 事件
 * @Date: 17:16 2018/7/3
 * @Modify by:
 */
@ApiModel("功能模组事件基础信息")
public class AddEventReq implements Serializable {

    private AddEventInfoReq eventInfo;

    //事件入参参数属性
    private List<AddServiceModulePropertyReq> propertyList;

    public AddEventInfoReq getEventInfo() {
        return eventInfo;
    }

    public void setEventInfo(AddEventInfoReq eventInfo) {
        this.eventInfo = eventInfo;
    }

    public List<AddServiceModulePropertyReq> getPropertyList() {
        return propertyList;
    }

    public void setPropertyList(List<AddServiceModulePropertyReq> propertyList) {
        this.propertyList = propertyList;
    }
}
