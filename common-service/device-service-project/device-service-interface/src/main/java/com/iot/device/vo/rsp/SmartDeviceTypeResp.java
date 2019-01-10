package com.iot.device.vo.rsp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("音箱类型信息")
public class SmartDeviceTypeResp {
    @ApiModelProperty(value = "智能类型编码")
    private String smartType;
    @ApiModelProperty(value = "智能类型,1:Alexa;2:GoogleHome", allowableValues = "1,2")
    private Integer smart;

    public String getSmartType() {
        return smartType;
    }

    public void setSmartType(String smartType) {
        this.smartType = smartType;
    }

    public Integer getSmart() {
        return smart;
    }

    public void setSmart(Integer smart) {
        this.smart = smart;
    }
}
