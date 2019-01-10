package com.iot.portal.web.vo.req;

import com.iot.device.vo.req.ModuleIftttReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：保存联调配置入参
 * 创建人： yeshiyuan
 * 创建时间：2018/10/23 18:52
 * 修改人： yeshiyuan
 * 修改时间：2018/10/23 18:52
 * 修改描述：
 */
@ApiModel(description = "保存联调配置入参")
public class SaveModuleIftttReq {
    @ApiModelProperty(name = "productId", value = "产品id", dataType = "Long")
    private Long productId;

    @ApiModelProperty(name = "actions", value = "方法", dataType = "List")
    private List<ModuleIftttReq> actions;

    @ApiModelProperty(name = "events", value = "事件", dataType = "List")
    private List<ModuleIftttReq> events;

    @ApiModelProperty(name = "properties", value = "属性", dataType = "List")
    private List<ModuleIftttReq> properties;

    @ApiModelProperty(name = "timerTypes", value = "定时类型", dataType = "List")
    private List<String> timerTypes;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public List<ModuleIftttReq> getActions() {
        return actions;
    }

    public void setActions(List<ModuleIftttReq> actions) {
        this.actions = actions;
    }

    public List<ModuleIftttReq> getEvents() {
        return events;
    }

    public void setEvents(List<ModuleIftttReq> events) {
        this.events = events;
    }

    public List<ModuleIftttReq> getProperties() {
        return properties;
    }

    public void setProperties(List<ModuleIftttReq> properties) {
        this.properties = properties;
    }

    public List<String> getTimerTypes() {
        return timerTypes;
    }

    public void setTimerTypes(List<String> timerTypes) {
        this.timerTypes = timerTypes;
    }

}
