package com.iot.control.packagemanager.vo.req.rule;

import com.iot.control.packagemanager.vo.req.IfDevInfoReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
  * @despriction：触发具体信息
  * @author  yeshiyuan
  * @created 2018/11/23 14:28
  */
@Data
@ApiModel(description = "触发具体信息")
public class TriggerInfoReq {

    @ApiModelProperty(name = "dev", value = "设备类型", dataType = "List")
    private List<IfDevInfoReq> dev;
    @ApiModelProperty(name = "threeApi", value = "第三方api", dataType = "List")
    private List<Map<String,Object>> threeApi;

}
