package com.iot.tenant.vo.resp.network;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：配网信息出参
 * 创建人： yeshiyuan
 * 创建时间：2018/10/8 15:26
 * 修改人： yeshiyuan
 * 修改时间：2018/10/8 15:26
 * 修改描述：
 */
@ApiModel(description = "配网信息出参")
public class NetworkInfoResp {

    @ApiModelProperty(name = "networkTypeId", value = "配网模式id", dataType = "Long")
    private Long networkTypeId;

    @ApiModelProperty(name = "networkType", value = "配网模式", dataType = "String")
    private String networkType;

    @ApiModelProperty(name = "steps", value = "配网步骤详情", dataType = "List")
    private List<NetworkStepResp> steps;

    public String getNetworkType() {
        return networkType;
    }

    public void setNetworkType(String networkType) {
        this.networkType = networkType;
    }

    public List<NetworkStepResp> getSteps() {
        return steps;
    }

    public void setSteps(List<NetworkStepResp> steps) {
        this.steps = steps;
    }

    public Long getNetworkTypeId() {
        return networkTypeId;
    }

    public void setNetworkTypeId(Long networkTypeId) {
        this.networkTypeId = networkTypeId;
    }
}
