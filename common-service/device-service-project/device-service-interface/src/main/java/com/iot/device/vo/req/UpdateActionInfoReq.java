package com.iot.device.vo.req;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 15:41 2018/7/3
 * @Modify by:
 */
public class UpdateActionInfoReq implements Serializable {

    private AddServiceModuleActionReq actionReq;

    //请求参数属性
    private List<AddServiceModulePropertyReq> paramPropertyList;
    //返回参数属性
    private List<AddServiceModulePropertyReq> returnPropertyList;

    private List<AddServiceModulePropertyReq> propertyReqList;

    public AddServiceModuleActionReq getActionReq() {
        return actionReq;
    }

    public void setActionReq(AddServiceModuleActionReq actionReq) {
        this.actionReq = actionReq;
    }

    public List<AddServiceModulePropertyReq> getPropertyReqList() {
        return propertyReqList;
    }

    public void setPropertyReqList(List<AddServiceModulePropertyReq> propertyReqList) {
        this.propertyReqList = propertyReqList;
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
