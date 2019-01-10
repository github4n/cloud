package com.iot.control.packagemanager.vo.req.scene;

import com.iot.common.exception.BusinessException;
import com.iot.common.util.StringUtil;
import com.iot.control.packagemanager.execption.SceneExceptionEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
  * @despriction：保存场景入参
  * @author  yeshiyuan
  * @created 2018/12/5 9:51
  */
@ApiModel(description = "保存场景入参")
@Data
public class SaveSceneInfoReq {

    @ApiModelProperty(name = "sceneId", value = "场景id", dataType = "Long")
    private Long sceneId;

    @ApiModelProperty(name = "sceneName", value = "场景名称", dataType = "String")
    private String sceneName;

    @ApiModelProperty(name = "packageId", value = "套包id", dataType = "Long")
    private Long packageId;

    @ApiModelProperty(name = "detail", value = "配置详情", dataType = "SceneDetailInfoReq")
    private SceneDetailInfoReq detail;

    /**
      * @despriction：描述
      * @author  yeshiyuan
      * @created 2018/12/10 14:51
      */
    public static void checkParam(SaveSceneInfoReq saveSceneInfoReq) {
        if (saveSceneInfoReq == null) {
            throw new BusinessException(SceneExceptionEnum.PARAM_ERROR, "object is null");
        }
        if (saveSceneInfoReq.getPackageId() == null) {
            throw new BusinessException(SceneExceptionEnum.PARAM_ERROR, "package id is null");
        }
        if (StringUtil.isBlank(saveSceneInfoReq.getSceneName())) {
            throw new BusinessException(SceneExceptionEnum.PARAM_ERROR, "scene name is null");
        }
        if (saveSceneInfoReq.getDetail() == null) {
            throw new BusinessException(SceneExceptionEnum.PARAM_ERROR, "scene config is null");
        }
    }
}
