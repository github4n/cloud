package com.iot.file.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 项目名称：cloud
 * 功能描述：想要返回哪些文件信息
 * 创建人： yeshiyuan
 * 创建时间：2018/8/30 15:52
 * 修改人： yeshiyuan
 * 修改时间：2018/8/30 15:52
 * 修改描述：
 */
@ApiModel(value = "想要返回哪些文件信息")
public class NeedReturnFileInfoReq {

    @ApiModelProperty(name = "tenantId", value = "租户id", dataType = "Long")
    private Long tenantId;

    @ApiModelProperty(name = "needMd5", value = "是否返回md5", dataType = "Boolean")
    private boolean needMd5;

    @ApiModelProperty(name = "needUrl", value = "是否返回url", dataType = "Boolean")
    private boolean needUrl;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public boolean isNeedMd5() {
        return needMd5;
    }

    public void setNeedMd5(boolean needMd5) {
        this.needMd5 = needMd5;
    }

    public boolean isNeedUrl() {
        return needUrl;
    }

    public void setNeedUrl(boolean needUrl) {
        this.needUrl = needUrl;
    }

    public NeedReturnFileInfoReq() {
    }

    public NeedReturnFileInfoReq(boolean needMd5, boolean needUrl) {
        this.needMd5 = needMd5;
        this.needUrl = needUrl;
    }

    public NeedReturnFileInfoReq(Long tenantId, boolean needMd5, boolean needUrl) {
        this.tenantId = tenantId;
        this.needMd5 = needMd5;
        this.needUrl = needUrl;
    }
}
