package com.iot.portal.web.vo.reply.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 项目名称：cloud
 * 功能描述：回复信息详情
 * 创建人： yeshiyuan
 * 创建时间：2018/11/2 14:03
 * 修改人： yeshiyuan
 * 修改时间：2018/11/2 14:03
 * 修改描述：
 */
@ApiModel(description = "回复信息详情")
public class ReplyDetailResp {

    @ApiModelProperty(name = "id", value = "id", dataType = "Long")
    private Long id;

    @ApiModelProperty(name = "parentId", value = "上一条消息id", dataType = "Long")
    private Long parentId;

    @ApiModelProperty(name = "message", value = "消息内容", dataType = "String")
    private String message;

    @ApiModelProperty(name = "messageType", value = "消息类型（feedback：反馈，reply：回复）", dataType = "String")
    private String messageType;

    @ApiModelProperty(name = "userName", value = "创建人", dataType = "String")
    private String userName;

    @ApiModelProperty(name = "createTime", value = "创建时间", dataType = "Date")
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
