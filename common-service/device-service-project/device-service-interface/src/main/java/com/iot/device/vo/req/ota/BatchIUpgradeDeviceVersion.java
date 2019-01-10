package com.iot.device.vo.req.ota;

import java.util.List;

/**
 * 项目名称：IOT云平台
 * 模块名称：
 * 功能描述：
 * 创建人： 490485964@qq.com
 * 创建时间：2018年08月02日 19:14
 * 修改人： 490485964@qq.com
 * 修改时间：2018年08月02日 19:14
 */
public class BatchIUpgradeDeviceVersion {
    private List<UpgradeDeviceVersionReq> upgradeDeviceVersionReqList;

    public List<UpgradeDeviceVersionReq> getUpgradeDeviceVersionReqList() {
        return upgradeDeviceVersionReqList;
    }

    public void setUpgradeDeviceVersionReqList(List<UpgradeDeviceVersionReq> upgradeDeviceVersionReqList) {
        this.upgradeDeviceVersionReqList = upgradeDeviceVersionReqList;
    }
}
