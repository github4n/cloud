package com.iot.portal.web.vo;

import com.iot.device.vo.req.AddServiceModuleActionReq;
import com.iot.device.vo.req.AddServiceModulePropertyReq;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 15:32 2018/7/3
 * @Modify by:
 */
public class PortalUpdateActionInfoReq implements Serializable {

    private AddServiceModuleActionReq actionInfo;

    private List<AddServiceModulePropertyReq> propertyList;

    public AddServiceModuleActionReq getActionInfo() {
        return actionInfo;
    }

    public void setActionInfo(AddServiceModuleActionReq actionInfo) {
        this.actionInfo = actionInfo;
    }

    public List<AddServiceModulePropertyReq> getPropertyList() {
        return propertyList;
    }

    public void setPropertyList(List<AddServiceModulePropertyReq> propertyList) {
        this.propertyList = propertyList;
    }
}
