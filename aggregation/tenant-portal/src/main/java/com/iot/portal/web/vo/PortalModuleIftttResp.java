package com.iot.portal.web.vo;

import com.iot.device.vo.rsp.ModuleSupportIftttResp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：portal模组联动管理
 * 创建人： yeshiyuan
 * 创建时间：2018/10/23 15:04
 * 修改人： yeshiyuan
 * 修改时间：2018/10/23 15:04
 * 修改描述：
 */
@ApiModel(description = "portal模组联动管理")
public class PortalModuleIftttResp {

    @ApiModelProperty(name = "action", value = "方法", dataType = "ModuleSupportIftttResp")
    private ModuleSupportIftttResp action;

    @ApiModelProperty(name = "event", value = "事件", dataType = "ModuleSupportIftttResp")
    private ModuleSupportIftttResp event;

    @ApiModelProperty(name = "property", value = "属性", dataType = "ModuleSupportIftttResp")
    private ModuleSupportIftttResp property;

    @ApiModelProperty(name = "timerTypes", value = "定时类型")
    private List<String> timerTypes;

    public ModuleSupportIftttResp getAction() {
        return action;
    }

    public void setAction(ModuleSupportIftttResp action) {
        this.action = action;
    }

    public ModuleSupportIftttResp getEvent() {
        return event;
    }

    public void setEvent(ModuleSupportIftttResp event) {
        this.event = event;
    }

    public ModuleSupportIftttResp getProperty() {
        return property;
    }

    public void setProperty(ModuleSupportIftttResp property) {
        this.property = property;
    }

    public List<String> getTimerTypes() {
        return timerTypes;
    }

    public void setTimerTypes(List<String> timerTypes) {
        this.timerTypes = timerTypes;
    }

}
