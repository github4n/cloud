package com.iot.control.packagemanager.vo.resp;

import com.iot.control.packagemanager.vo.resp.scene.SceneDetailInfoResp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@ApiModel(value = "场景详情实体类bean")
@Data
public class SceneInfoDetailResp implements Serializable{
    @ApiModelProperty(name = "id", value = "主键")
    private Long id;

    @ApiModelProperty(name = "sceneName", value = "场景名称")
    private String sceneName;

    @ApiModelProperty(name = "sceneDetailInfoResp", value = "场景详细信息")
    private SceneDetailInfoResp sceneDetailInfoResp;

    public SceneInfoDetailResp() {}

    public SceneInfoDetailResp(Long id, String sceneName, SceneDetailInfoResp sceneDetailInfoResp) {
        this.id = id;
        this.sceneName = sceneName;
        this.sceneDetailInfoResp = sceneDetailInfoResp;
    }
}
