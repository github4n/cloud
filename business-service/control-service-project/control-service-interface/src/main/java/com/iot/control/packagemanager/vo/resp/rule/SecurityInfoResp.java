package com.iot.control.packagemanager.vo.resp.rule;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;
@Data
@ApiModel(description = "安防策略信息")
public class SecurityInfoResp {
    @ApiModelProperty(name = "type", value = "安防策略类型（stay、away、sos)", dataType = "String")
    private String type;

    @ApiModelProperty(name = "alram", value = "安防警告配置", dataType = "Map")
    private Map<String, Object> alram;
}
