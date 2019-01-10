package com.iot.tenant.vo.resp.network;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：设备配网步骤出参（BOSS使用）
 * 创建人： yeshiyuan
 * 创建时间：2018/10/8 15:12
 * 修改人： yeshiyuan
 * 修改时间：2018/10/8 15:12
 * 修改描述：
 */
@ApiModel(description = "设备配网步骤出参（BOSS使用）")
public class DeviceNetworkStepBaseResp {

    @ApiModelProperty(name = "deviceTypeId", value = "设备类型id", dataType = "Long")
    private Long deviceTypeId;

    @ApiModelProperty(name = "networkInfos", value = "配网信息", dataType = "List")
    private List<NetworkInfoResp> networkInfos;

    public Long getDeviceTypeId() {
        return deviceTypeId;
    }

    public void setDeviceTypeId(Long deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
    }


    public List<NetworkInfoResp> getNetworkInfos() {
        return networkInfos;
    }

    public void setNetworkInfos(List<NetworkInfoResp> networkInfos) {
        this.networkInfos = networkInfos;
    }
}
