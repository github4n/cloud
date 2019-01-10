package com.iot.control.packagemanager.vo.req.scene;

import com.iot.control.packagemanager.vo.req.ActuatorInfoVo;
import com.iot.control.packagemanager.vo.req.ThenDevInfoReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
  * @despriction：场景配置详情
  * @author  yeshiyuan
  * @created 2018/11/23 11:21
  */
@ApiModel(description = "场景配置详情")
@Data
public class SceneConfigReq {

    @ApiModelProperty(name = "dev", value = "设备配置信息", dataType = "List")
    private List<ThenDevInfoReq> dev;

    @ApiModelProperty(name = "actuator", value = "执行配置信息", dataType = "List")
    private List<ActuatorInfoVo> actuator;
}
