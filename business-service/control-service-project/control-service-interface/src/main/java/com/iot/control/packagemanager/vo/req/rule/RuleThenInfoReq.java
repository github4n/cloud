package com.iot.control.packagemanager.vo.req.rule;

import com.iot.control.packagemanager.vo.req.ActuatorInfoVo;
import com.iot.control.packagemanager.vo.req.ThenDevInfoReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
  * @despriction：策略then信息
  * @author  yeshiyuan
  * @created 2018/11/23 14:14
  */
@Data
@ApiModel(description = "策略then信息")
public class RuleThenInfoReq {

    @ApiModelProperty(name = "dev", value = "then中设备的信息详情", dataType = "List")
    private List<ThenDevInfoReq> dev;

    @ApiModelProperty(name = "scene", value = "场景id", dataType = "List")
    private List<Long> scene;

    @ApiModelProperty(name = "actuator", value = "执行配置信息", dataType = "List")
    private List<ActuatorInfoVo> actuator;
}
