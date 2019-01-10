package com.iot.tenant.vo.resp.network;

import com.iot.tenant.vo.req.network.NetworkStepReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 项目名称：cloud
 * 功能描述：配网步骤信息出参
 * 创建人： yeshiyuan
 * 创建时间：2018/10/8 15:22
 * 修改人： yeshiyuan
 * 修改时间：2018/10/8 15:22
 * 修改描述：
 */
@ApiModel(description = "配网步骤信息出参")
public class NetworkStepResp extends NetworkStepReq{

    @ApiModelProperty(name = "iconUrl", value = "图片url", dataType = "String")
    private String iconUrl;

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

}
