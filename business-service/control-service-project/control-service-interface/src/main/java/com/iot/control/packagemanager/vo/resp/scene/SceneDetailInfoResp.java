package com.iot.control.packagemanager.vo.resp.scene;

import com.iot.control.packagemanager.vo.req.scene.SceneConfigReq;
import com.iot.control.packagemanager.vo.resp.DeviceTypeIdAndName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
@Data
public class SceneDetailInfoResp {

//    @ApiModelProperty(name = "devList", value = "设备id或产品id集合", dataType = "List")
//    private List<Long> devList;

    @ApiModelProperty(name = "config", value = "场景配置信息", dataType = "List")
    private SceneConfigResp config;

}
