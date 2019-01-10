package com.iot.tenant.vo.req.network;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：配网信息入参
 * 创建人： yeshiyuan
 * 创建时间：2018/10/8 15:26
 * 修改人： yeshiyuan
 * 修改时间：2018/10/8 15:26
 * 修改描述：
 */
@ApiModel(description = "配网信息入参")
public class NetworkInfoReq {

    @ApiModelProperty(name = "networkTypeId", value = "配网模式id", dataType = "Long")
    private Long networkTypeId;

    @ApiModelProperty(name = "steps", value = "配网步骤详情", dataType = "List")
    private List<NetworkStepReq> steps;

    public Long getNetworkTypeId() {
        return networkTypeId;
    }

    public void setNetworkTypeId(Long networkTypeId) {
        this.networkTypeId = networkTypeId;
    }

    public List<NetworkStepReq> getSteps() {
        return steps;
    }

    public void setSteps(List<NetworkStepReq> steps) {
        this.steps = steps;
    }
}
