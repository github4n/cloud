package com.iot.device.vo.rsp.ota;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：IOT云平台
 * 模块名称：ota
 * 功能描述：ota
 * 创建人： 490485964@qq.com
 * 创建时间：2018年08月01日 10:51
 * 修改人： 490485964@qq.com
 * 修改时间：2018年08月01日 10:51
 */
public class VersionDeviceRelevance {
    /**
     * 直连设备IdList
     */
    private List<String> directDeviceIdList = new ArrayList<>();
    /**
     * 子设备List
     */
    private List<SubForceOta> subDeviceList = new ArrayList();

    public List<String> getDirectDeviceIdList() {
        return directDeviceIdList;
    }

    public void setDirectDeviceIdList(List<String> directDeviceIdList) {
        this.directDeviceIdList = directDeviceIdList;
    }

    public List<SubForceOta> getSubDeviceList() {
        return subDeviceList;
    }

    public void setSubDeviceList(List<SubForceOta> subDeviceList) {
        this.subDeviceList = subDeviceList;
    }
}
