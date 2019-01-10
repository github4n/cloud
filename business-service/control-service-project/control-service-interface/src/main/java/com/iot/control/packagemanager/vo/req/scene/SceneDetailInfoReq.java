package com.iot.control.packagemanager.vo.req.scene;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
  * @despriction：场景详情
  * @author  yeshiyuan
  * @created 2018/11/23 10:22
  */
@ApiModel(description = "场景详情")
@Data
public class SceneDetailInfoReq {

    @ApiModelProperty(name = "devList", value = "设备id或产品id集合", dataType = "List")
    private List<Long> devList;

    @ApiModelProperty(name = "config", value = "场景配置信息", dataType = "List")
    private SceneConfigReq config;

    public List<Long> getDevList() {
        return devList;
    }

    public void setDevList(List<Long> devList) {
        this.devList = devList;
    }

    public SceneConfigReq getConfig() {
        return config;
    }

    public void setConfig(SceneConfigReq config) {
        this.config = config;
    }
}
