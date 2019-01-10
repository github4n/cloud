package com.iot.control.packagemanager.vo.resp.rule;

import com.iot.control.packagemanager.vo.req.IfDevInfoReq;
import com.iot.control.packagemanager.vo.resp.IfDevInfoResp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@ApiModel(description = "触发具体信息")
public class TriggerInfoResp {
    @ApiModelProperty(name = "dev", value = "设备类型", dataType = "List")
    private List<IfDevInfoResp> dev;
    @ApiModelProperty(name = "threeApi", value = "第三方api", dataType = "List")
    private List<Map<String,Object>> threeApi;
}
