package com.iot.control.packagemanager.vo.resp.rule;

import com.iot.control.packagemanager.vo.req.ActuatorInfoVo;
import com.iot.control.packagemanager.vo.resp.SceneInfoIdAndNameResp;
import com.iot.control.packagemanager.vo.resp.ThenDevInfoResp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel(value = "策略then信息返回bean")
@Data
public class RuleThenInfoResp {

    @ApiModelProperty(name = "dev", value = "then中设备的信息详情", dataType = "List")
    private List<ThenDevInfoResp> dev;

    @ApiModelProperty(name = "scenes", value = "场景id", dataType = "List")
    private List<SceneInfoIdAndNameResp> scenes;

    @ApiModelProperty(name = "actuator", value = "执行配置信息", dataType = "List")
    private List<ActuatorInfoVo> actuator;
}
