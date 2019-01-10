package com.iot.control.packagemanager.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 *@description 保存套包场景实体类
 *@author wucheng
 *@create 2018/12/11 16:22
 */
@ApiModel(description = "保存套包场景实体类")
@Data
public class SceneInfoSaveReq {

    @ApiModelProperty(name = "id", value = "场景id", dataType = "Long")
    private Long id;

    @ApiModelProperty(name = "packageId", value = "套包id", dataType = "Long")
    private Long packageId;

    @ApiModelProperty(name = "tenantId", value = "租户", dataType = "Long")
    private Long tenantId;

    @ApiModelProperty(name = "sceneName", value = "场景名称", dataType = "String")
    private String sceneName;

    @ApiModelProperty(name = "json", value = "json体", dataType = "String")
    private String json;

    @ApiModelProperty(name = "createTime", value = "创建时间", dataType = "Date")
    private Date createTime;

    @ApiModelProperty(name = "createBy", value = "创建人", dataType = "Long")
    private Long createBy;

    @ApiModelProperty(name = "updateTime", value = "修改时间", dataType = "Date")
    private Date updateTime;

    @ApiModelProperty(name = "updateBy", value = "修改人", dataType = "updateBy")
    private Long updateBy;

    @ApiModelProperty(name = "demoSceneId", value = "模板场景id", dataType = "demoSceneId")
    private Long demoSceneId;

    public SceneInfoSaveReq() {}

    public SceneInfoSaveReq(Long id, String sceneName, Long updateBy, Date updateTime, String json) {
        this.id = id;
        this.sceneName = sceneName;
        this.updateBy = updateBy;
        this.updateTime = updateTime;
        this.json = json;
    }

    public SceneInfoSaveReq(Long packageId, Long tenantId, String sceneName, Long createBy, Date createTime, Long demoSceneId) {
        this.packageId = packageId;
        this.tenantId = tenantId;
        this.sceneName = sceneName;
        this.createBy = createBy;
        this.createTime = createTime;
        this.demoSceneId = demoSceneId;
    }
}