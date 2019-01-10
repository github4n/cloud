package com.iot.tenant.vo.req.reply;

import com.iot.common.exception.BusinessException;
import com.iot.common.util.StringUtil;
import com.iot.tenant.enums.ReplyObjectType;
import com.iot.tenant.exception.TenantExceptionEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 项目名称：cloud
 * 功能描述：添加回复消息入参
 * 创建人： yeshiyuan
 * 创建时间：2018/11/1 20:06
 * 修改人： yeshiyuan
 * 修改时间：2018/11/1 20:06
 * 修改描述：
 */
@Data
@ApiModel(description = "添加回复消息入参")
public class AddReplyMessageReq {

    @ApiModelProperty(name = "tenantId", value = "租户主键", dataType = "Long")
    private Long tenantId;

    @ApiModelProperty(name = "parentId", value = "上一条消息id", dataType = "Long")
    private Long parentId;

    @ApiModelProperty(name = "objectType", value = "对象类型（app、Google_voice、alexa_voice）", dataType = "String")
    private String objectType;

    @ApiModelProperty(name = "objectId", value = "对象ID", dataType = "Long")
    private Long objectId;

    @ApiModelProperty(name = "message", value = "消息内容", dataType = "String")
    private String message;

    @ApiModelProperty(name = "messageType", value = "消息类型（feedback：反馈，reply：回复）", dataType = "String")
    private String messageType;

    @ApiModelProperty(name = "createBy", value = "创建人", dataType = "Long")
    private Long createBy;

    @ApiModelProperty(name = "createTime", value = "创建时间", dataType = "Date")
    private Date createTime;

    public static void checkParam(AddReplyMessageReq req){
        if (req.getTenantId() == null) {
            throw new BusinessException(TenantExceptionEnum.PARAM_ERROR, "tenantId is null");
        }
        if (req.getObjectId() == null) {
            throw new BusinessException(TenantExceptionEnum.PARAM_ERROR, "objectId is null");
        }
        if (!ReplyObjectType.checkType(req.getObjectType())) {
            throw new BusinessException(TenantExceptionEnum.PARAM_ERROR, "object type is error");
        }
        if (StringUtil.isBlank(req.getMessage())) {
            throw new BusinessException(TenantExceptionEnum.PARAM_ERROR, "message is null");
        }
        if (req.getCreateBy() == null) {
            throw new BusinessException(TenantExceptionEnum.PARAM_ERROR, "create by is null");
        }
    }

    public AddReplyMessageReq() {
    }

    public AddReplyMessageReq(Long tenantId, String objectType, Long objectId, String message, String messageType, Long createBy, Date createTime) {
        this.tenantId = tenantId;
        this.objectType = objectType;
        this.objectId = objectId;
        this.message = message;
        this.messageType = messageType;
        this.createBy = createBy;
        this.createTime = createTime;
    }

    public AddReplyMessageReq(Long tenantId, Long parentId, String objectType, Long objectId, String message, String messageType, Long createBy, Date createTime) {
        this.tenantId = tenantId;
        this.parentId = parentId;
        this.objectType = objectType;
        this.objectId = objectId;
        this.message = message;
        this.messageType = messageType;
        this.createBy = createBy;
        this.createTime = createTime;
    }
}
