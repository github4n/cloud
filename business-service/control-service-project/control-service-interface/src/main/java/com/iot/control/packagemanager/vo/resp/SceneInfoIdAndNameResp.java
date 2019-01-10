package com.iot.control.packagemanager.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 *@description 场景id和场景名称返回bean
 *@author wucheng
 *@create 2018/12/13 10:28
 */
@ApiModel(value = "场景id和场景名称")
@Data
public class SceneInfoIdAndNameResp implements Serializable{
    @ApiModelProperty(name = "id", value = "主键id")
    private Long id;

    @ApiModelProperty(name = "sceneName", value = "场景名称")
    private String sceneName;
}
