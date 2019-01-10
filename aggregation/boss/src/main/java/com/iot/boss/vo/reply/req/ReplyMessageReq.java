package com.iot.boss.vo.reply.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 项目名称：cloud
 * 功能描述：回复消息入参
 * 创建人： yeshiyuan
 * 创建时间：2018/11/2 11:14
 * 修改人： yeshiyuan
 * 修改时间：2018/11/2 11:14
 * 修改描述：
 */
@Data
@ApiModel(description = "回复消息入参")
public class ReplyMessageReq {

    @ApiModelProperty(name = "parentId", value = "上一条消息id", dataType = "Long")
    private Long parentId;

    @ApiModelProperty(name = "objectId", value = "对象ID", dataType = "Long")
    private Long objectId;

    @ApiModelProperty(name = "message", value = "消息内容", dataType = "String")
    private String message;



}
