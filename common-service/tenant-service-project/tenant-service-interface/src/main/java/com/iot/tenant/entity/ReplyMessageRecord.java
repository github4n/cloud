package com.iot.tenant.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 项目名称：cloud
 * 功能描述：回复消息记录实体类
 * 创建人： yeshiyuan
 * 创建时间：2018/11/1 17:23
 * 修改人： yeshiyuan
 * 修改时间：2018/11/1 17:23
 * 修改描述：
 */
@Data
@NoArgsConstructor
@ApiModel(description = "回复消息记录实体类")
public class ReplyMessageRecord {

    @ApiModelProperty(name = "id", value = "id", dataType = "Long")
    private Long id;

    @ApiModelProperty(name = "tenantId", value = "租户主键", dataType = "Long")
    private Long tenantId;

    @ApiModelProperty(name = "parentId", value = "上一条消息id", dataType = "Long")
    private Long parentId;

    @ApiModelProperty(name = "objectType", value = "对象类型（app、Google_voice、alexa_voice）", dataType = "String")
    private String objectType;

    @ApiModelProperty(name = "objectId", value = "对象ID(当object_type为deviceNetwork时为appId_productId)", dataType = "Long")
    private Long objectId;

    @ApiModelProperty(name = "message", value = "消息内容", dataType = "String")
    private String message;

    @ApiModelProperty(name = "messageType", value = "消息类型（feedback：反馈，reply：回复）", dataType = "String")
    private String messageType;

    @ApiModelProperty(name = "createBy", value = "创建人", dataType = "Long")
    private Long createBy;

    @ApiModelProperty(name = "createTime", value = "创建时间", dataType = "Date")
    private Date createTime;

    @ApiModelProperty(name = "updateBy", value = "更新人", dataType = "Long")
    private Long updateBy;

    @ApiModelProperty(name = "updateTime", value = "修改时间", dataType = "Date")
    private Date updateTime;



}
