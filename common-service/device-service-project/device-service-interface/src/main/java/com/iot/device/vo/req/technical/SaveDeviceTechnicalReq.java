package com.iot.device.vo.req.technical;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：保存设备对应的技术方案入参
 * 创建人： yeshiyuan
 * 创建时间：2018/10/15 19:14
 * 修改人： yeshiyuan
 * 修改时间：2018/10/15 19:14
 * 修改描述：
 */
@ApiModel(description = "保存设备对应的技术方案入参")
public class SaveDeviceTechnicalReq {

    @ApiModelProperty(name = "deviceTypeId", value = "设备类型id", dataType = "Long")
    private Long deviceTypeId;
    @ApiModelProperty(name = "createBy", value = "操作人id", dataType = "Long")
    private Long createBy;
    @ApiModelProperty(name = "technicalIds", value = "技术方案id", dataType = "List")
    private List<Long> technicalIds;
    @ApiModelProperty(name = "networkTypeIds", value = "配网模式ids", dataType = "List")
    private List<Long> networkTypeIds;

    public Long getDeviceTypeId() {
        return deviceTypeId;
    }

    public void setDeviceTypeId(Long deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public List<Long> getTechnicalIds() {
        return technicalIds;
    }

    public void setTechnicalIds(List<Long> technicalIds) {
        this.technicalIds = technicalIds;
    }

    public List<Long> getNetworkTypeIds() {
        return networkTypeIds;
    }

    public void setNetworkTypeIds(List<Long> networkTypeIds) {
        this.networkTypeIds = networkTypeIds;
    }
}
