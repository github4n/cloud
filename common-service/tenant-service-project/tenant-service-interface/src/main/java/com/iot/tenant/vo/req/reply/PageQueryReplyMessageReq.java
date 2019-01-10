package com.iot.tenant.vo.req.reply;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 项目名称：cloud
 * 功能描述：分页查询回复消息列表入参
 * 创建人： yeshiyuan
 * 创建时间：2018/11/1 20:25
 * 修改人： yeshiyuan
 * 修改时间：2018/11/1 20:25
 * 修改描述：
 */
@ApiModel(description = "分页查询回复消息列表入参")
public class PageQueryReplyMessageReq {

    @ApiModelProperty(name = "pageNum", value = "页码", dataType = "Integer")
    private int pageNum;

    @ApiModelProperty(name = "pageSize", value = "页大小", dataType = "Integer")
    private int pageSize;

    @ApiModelProperty(name = "objectId", value = "对象ID", dataType = "Long")
    private Long objectId;

    @ApiModelProperty(name = "objectType", value = "对象类型（app、Google_voice、alexa_voice）", dataType = "String")
    private String objectType;

    @ApiModelProperty(name = "tenantId", value = "租户主键", dataType = "Long")
    private Long tenantId;

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNum() {
        if (pageNum==0) {
            pageNum = 1;
        }
        return pageNum;
    }


    public Integer getPageSize() {
        if (pageSize==0) {
            pageSize = 20;
        }
        return pageSize;
    }

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
}
