package com.iot.device.vo.rsp.servicemodule;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
  * @despriction：套包功能模组详情
  * @author  yeshiyuan
  * @created 2018/11/22 15:02
  */
@Data
@ApiModel("套包功能模组详情")
public class PackageServiceModuleDetailResp {

    @ApiModelProperty(name = "properties", value = "属性列表", dataType = "List")
    private List<PropertyResp> properties;

    @ApiModelProperty(name = "actions", value = "方法列表", dataType = "List")
    private List<ActionResp> actions;

    @ApiModelProperty(name = "events", value = "事件列表", dataType = "List")
    private List<EventResp> events;

}
