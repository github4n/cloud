package com.iot.boss.vo.network;

import com.iot.tenant.vo.req.network.NetworkInfoReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：保存设备配网步骤入参（BOSS使用）
 * 创建人： yeshiyuan
 * 创建时间：2018/10/8 15:12
 * 修改人： yeshiyuan
 * 修改时间：2018/10/8 15:12
 * 修改描述：
 */
@ApiModel(description = "保存设备配网步骤入参（BOSS使用）")
public class BossSaveNetworkStepReq {

    @ApiModelProperty(name = "newFileList", value = "新文件列表", dataType = "List")
    private List<String> newFileList;

    @ApiModelProperty(name = "deviceTypeId", value = "设备类型id", dataType = "Long")
    private Long deviceTypeId;

    @ApiModelProperty(name = "networkInfos", value = "配网信息", dataType = "List")
    private List<NetworkInfoReq> networkInfos;

    public Long getDeviceTypeId() {
        return deviceTypeId;
    }

    public void setDeviceTypeId(Long deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
    }


    public List<NetworkInfoReq> getNetworkInfos() {
        return networkInfos;
    }

    public void setNetworkInfos(List<NetworkInfoReq> networkInfos) {
        this.networkInfos = networkInfos;
    }

    public List<String> getNewFileList() {
        return newFileList;
    }

    public void setNewFileList(List<String> newFileList) {
        this.newFileList = newFileList;
    }
}
