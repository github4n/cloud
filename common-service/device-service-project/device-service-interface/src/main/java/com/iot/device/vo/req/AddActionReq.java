package com.iot.device.vo.req;

import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: xfz
 * @Descrpiton: 方法
 * @Date: 17:14 2018/7/3
 * @Modify by:
 */
@ApiModel("功能模组方法基础信息")
public class AddActionReq implements Serializable {

    //方法明细
    private AddServiceModuleActionReq actionInfo;
    //方法对应入参参数属性
    private List<AddServiceModulePropertyReq> paramPropertyList;
    //方法对应返回参数属性
    private List<AddServiceModulePropertyReq> returnPropertyList;


    public AddServiceModuleActionReq getActionInfo() {
        return actionInfo;
    }

    public void setActionInfo(AddServiceModuleActionReq actionInfo) {
        this.actionInfo = actionInfo;
    }

    public List<AddServiceModulePropertyReq> getParamPropertyList() {
        return paramPropertyList;
    }

    public void setParamPropertyList(List<AddServiceModulePropertyReq> paramPropertyList) {
        this.paramPropertyList = paramPropertyList;
    }

    public List<AddServiceModulePropertyReq> getReturnPropertyList() {
        return returnPropertyList;
    }

    public void setReturnPropertyList(List<AddServiceModulePropertyReq> returnPropertyList) {
        this.returnPropertyList = returnPropertyList;
    }
}
