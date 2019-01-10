package com.iot.control.packagemanager.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 *@description 场景返回客户端实体类bean
 *@author wucheng
 *@create 2018/12/12 13:49
 */
@Data
public class SceneInfoResp {
    @ApiModelProperty(name = "id", value = "主键")
    private Long id;

    @ApiModelProperty(name = "tenantId", value = "租户id")
    private Long tenantId;

    @ApiModelProperty(name = "packageId", value = "套包id")
    private Long packageId;

    @ApiModelProperty(name = "sceneName", value = "场景名称")
    private String sceneName;

    @ApiModelProperty(name = "json", value = "场景json")
    private String json;

    @ApiModelProperty(name = "createTime", value = "创建时间")
    private Date createTime;

    @ApiModelProperty(name = "createBy", value = "创建人")
    private Long createBy;

    @ApiModelProperty(name = "updateTime", value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(name = "updateBy", value = "修改人")
    private Long updateBy;

    @ApiModelProperty(name = "demoSceneId", value = "模板场景id")
    private Long demoSceneId;

}