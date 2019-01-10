package com.iot.tenant.vo.resp.network;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：设备配网步骤出参（portal使用）
 * 创建人： yeshiyuan
 * 创建时间：2018/10/8 15:12
 * 修改人： yeshiyuan
 * 修改时间：2018/10/8 15:12
 * 修改描述：
 */
@ApiModel(description = "设备配网步骤出参（portal使用）")
public class DeviceNetworkStepTenantResp {

    @ApiModelProperty(name = "productId", value = "产品id", dataType = "Long")
    private Long productId;

    @ApiModelProperty(name = "appId", value = "appId", dataType = "Long")
    private Long appId;

    @ApiModelProperty(name = "networkInfos", value = "配网信息", dataType = "List")
    private List<NetworkInfoResp> networkInfos;

    public List<NetworkInfoResp> getNetworkInfos() {
        return networkInfos;
    }

    public void setNetworkInfos(List<NetworkInfoResp> networkInfos) {
        this.networkInfos = networkInfos;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }
}
