package com.iot.portal.web.vo.network;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
  * @despriction：配网步骤配置文件
  * @author  yeshiyuan
  * @created 2018/11/30 10:33
  */
@ApiModel(value = "配网步骤配置文件")
public class NetworkStepConfigFileVo {

    @ApiModelProperty(name = "protocol", value = "技术方案", dataType = "String")
    private String protocol;

    @ApiModelProperty(name = "tabs", value = "具体配置信息", dataType = "List")
    private List<NetworkStepDetailVo> tabs;

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public List<NetworkStepDetailVo> getTabs() {
        return tabs;
    }

    public void setTabs(List<NetworkStepDetailVo> tabs) {
        this.tabs = tabs;
    }
}
