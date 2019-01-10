package com.iot.boss.vo.module;

import com.iot.device.vo.rsp.ServiceModuleListResp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：模组详情
 * 创建人： yeshiyuan
 * 创建时间：2018/10/26 15:27
 * 修改人： yeshiyuan
 * 修改时间：2018/10/26 15:27
 * 修改描述：
 */
@ApiModel(description = "模组详情")
public class BossServiceModuleResp extends ServiceModuleListResp implements Serializable{
    //功能组所有方法
    @ApiModelProperty(value = "功能组方法")
    private List<BossActionInfoResp> actionList;

    //功能组所有事件
    @ApiModelProperty(value = "功能组事件")
    private List<BossEventInfoResp> eventList;

    //功能组所有属性
    @ApiModelProperty(value = "功能组属性")
    private List<BossPropertyInfoResp> propertyList;

    //是否被选中 默认未选中
    @ApiModelProperty(value = "设备类型id", allowableValues = "true 选中 ,false 未选中")
    private boolean whetherCheck = false;

    public boolean isWhetherCheck() {
        return whetherCheck;
    }

    public void setWhetherCheck(boolean whetherCheck) {
        this.whetherCheck = whetherCheck;
    }

    public List<BossActionInfoResp> getActionList() {
        return actionList;
    }

    public void setActionList(List<BossActionInfoResp> actionList) {
        this.actionList = actionList;
    }

    public List<BossEventInfoResp> getEventList() {
        return eventList;
    }

    public void setEventList(List<BossEventInfoResp> eventList) {
        this.eventList = eventList;
    }

    public List<BossPropertyInfoResp> getPropertyList() {
        return propertyList;
    }

    public void setPropertyList(List<BossPropertyInfoResp> propertyList) {
        this.propertyList = propertyList;
    }
}
