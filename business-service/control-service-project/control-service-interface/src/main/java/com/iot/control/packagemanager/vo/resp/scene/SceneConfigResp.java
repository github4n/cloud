package com.iot.control.packagemanager.vo.resp.scene;

import com.iot.control.packagemanager.vo.req.ActuatorInfoVo;
import com.iot.control.packagemanager.vo.req.ThenDevInfoReq;
import com.iot.control.packagemanager.vo.resp.ThenDevInfoResp;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
@Data
public class SceneConfigResp {
    @ApiModelProperty(name = "dev", value = "设备配置信息", dataType = "List")
    private List<ThenDevInfoResp> dev;

    @ApiModelProperty(name = "actuator", value = "执行配置信息", dataType = "List")
    private List<ActuatorInfoVo> actuator;
}
