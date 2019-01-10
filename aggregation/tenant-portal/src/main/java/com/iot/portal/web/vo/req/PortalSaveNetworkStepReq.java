package com.iot.portal.web.vo.req;

import com.iot.tenant.vo.req.network.NetworkInfoReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：保存设备配网步骤入参（portal使用）
 * 创建人： yeshiyuan
 * 创建时间：2018/10/8 15:12
 * 修改人： yeshiyuan
 * 修改时间：2018/10/8 15:12
 * 修改描述：
 */
@ApiModel(description = "保存设备配网步骤入参（portal使用）")
public class PortalSaveNetworkStepReq {

    @ApiModelProperty(name = "appId", value = "appId", dataType = "Long")
    private Long appId;

    @ApiModelProperty(name = "productId", value = "产品id", dataType = "Long")
    private Long productId;

    @ApiModelProperty(name = "oldFileList", value = "旧文件列表", dataType = "List")
    private List<String> oldFileList;

    @ApiModelProperty(name = "newFileList", value = "新文件列表", dataType = "List")
    private List<String> newFileList;

    @ApiModelProperty(name = "networkInfos", value = "配网信息", dataType = "List")
    private List<NetworkInfoReq> networkInfos;

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }


    public List<NetworkInfoReq> getNetworkInfos() {
        return networkInfos;
    }

    public void setNetworkInfos(List<NetworkInfoReq> networkInfos) {
        this.networkInfos = networkInfos;
    }

    public List<String> getOldFileList() {
        return oldFileList;
    }

    public void setOldFileList(List<String> oldFileList) {
        this.oldFileList = oldFileList;
    }

    public List<String> getNewFileList() {
        return newFileList;
    }

    public void setNewFileList(List<String> newFileList) {
        this.newFileList = newFileList;
    }
}
