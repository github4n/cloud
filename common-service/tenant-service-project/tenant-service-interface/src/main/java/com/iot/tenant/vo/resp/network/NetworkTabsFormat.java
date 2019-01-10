package com.iot.tenant.vo.resp.network;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：配网tabs格式
 * 创建人： yeshiyuan
 * 创建时间：2018/10/18 14:39
 * 修改人： yeshiyuan
 * 修改时间：2018/10/18 14:39
 * 修改描述：
 */
@ApiModel(description = "配网tabs格式")
public class NetworkTabsFormat {

    @ApiModelProperty(name = "networkTypeId", value = "配网类型", dataType = "Long")
    private Long networkTypeId;

    @ApiModelProperty(name = "steps", value = "配网步骤", dataType = "List")
    private List<NetworkStepFormat> steps;

    @ApiModelProperty(name = "helps", value = "帮助文档", dataType = "List")
    private List<NetworkHelpFormat> helps;

    public Long getNetworkTypeId() {
        return networkTypeId;
    }

    public void setNetworkTypeId(Long networkTypeId) {
        this.networkTypeId = networkTypeId;
    }

    public List<NetworkStepFormat> getSteps() {
        return steps;
    }

    public void setSteps(List<NetworkStepFormat> steps) {
        this.steps = steps;
    }

    public List<NetworkHelpFormat> getHelps() {
        return helps;
    }

    public void setHelps(List<NetworkHelpFormat> helps) {
        this.helps = helps;
    }
}
