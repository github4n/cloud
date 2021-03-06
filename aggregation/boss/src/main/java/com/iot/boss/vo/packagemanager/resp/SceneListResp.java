package com.iot.boss.vo.packagemanager.resp;

import com.iot.control.packagemanager.vo.req.scene.SceneDetailInfoReq;
import com.iot.control.packagemanager.vo.resp.DeviceTypeIdAndName;
import com.iot.control.packagemanager.vo.resp.scene.SceneDetailInfoResp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @program: cloud
 * @description: 场景列表实体类
 * @author: yeshiyuan
 * @create: 2018-12-13 11:32
 **/
@ApiModel(description = "场景列表实体类")
@Data
public class SceneListResp {

    @ApiModelProperty(name = "id", value = "主键")
    private Long id;

    @ApiModelProperty(name = "sceneName", value = "场景名称")
    private String sceneName;

    @ApiModelProperty(name = "deviceTypeNames", value = "设备类型名称列表")
    private List<String> deviceTypeNames;

    @ApiModelProperty(name = "senceDetailInfoReq", value = "场景详细信息")
    private SceneDetailInfoResp senceDetailInfoResp;

    public SceneListResp() {
    }

    public SceneListResp(Long id, String sceneName) {
        this.id = id;
        this.sceneName = sceneName;
    }
}
