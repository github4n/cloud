package com.iot.tenant.vo.req.reply;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 项目名称：cloud
 * 功能描述：查询回复消息列表入参
 * 创建人： yeshiyuan
 * 创建时间：2018/11/1 20:25
 * 修改人： yeshiyuan
 * 修改时间：2018/11/1 20:25
 * 修改描述：
 */
@ApiModel(description = "分页查询回复消息列表入参")
public class QueryReplyMessageListReq {

    @ApiModelProperty(name = "objectId", value = "对象ID", dataType = "Long")
    private Long objectId;

    @ApiModelProperty(name = "objectType", value = "对象类型（app、Google_voice、alexa_voice）", dataType = "String")
    private String objectType;

    @ApiModelProperty(name = "tenantId", value = "租户主键", dataType = "Long")
    private Long tenantId;

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public QueryReplyMessageListReq() {
    }

    public QueryReplyMessageListReq(Long objectId, String objectType, Long tenantId) {
        this.objectId = objectId;
        this.objectType = objectType;
        this.tenantId = tenantId;
    }
}
