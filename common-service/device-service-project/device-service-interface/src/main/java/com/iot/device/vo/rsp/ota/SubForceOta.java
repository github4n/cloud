package com.iot.device.vo.rsp.ota;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：IOT云平台
 * 模块名称：ota
 * 功能描述：ota
 * 创建人： 490485964@qq.com
 * 创建时间：2018年08月01日 11:06
 * 修改人： 490485964@qq.com
 * 修改时间：2018年08月01日 11:06
 */
public class SubForceOta {

    /**
     * 网关Id
     */
    private String pdevId;
    /**
     * 子设备
     */
    private List<ForceOtaDevInfo> subDevList = new ArrayList<>();

    public SubForceOta() {

    }

    public SubForceOta(String pdevId, List<ForceOtaDevInfo> subDevList) {
        this.pdevId = pdevId;
        this.subDevList = subDevList;
    }

    public String getPdevId() {
        return pdevId;
    }

    public void setPdevId(String pdevId) {
        this.pdevId = pdevId;
    }

    public List<ForceOtaDevInfo> getSubDevList() {
        return subDevList;
    }

    public void setSubDevList(List<ForceOtaDevInfo> subDevList) {
        this.subDevList = subDevList;
    }
}
