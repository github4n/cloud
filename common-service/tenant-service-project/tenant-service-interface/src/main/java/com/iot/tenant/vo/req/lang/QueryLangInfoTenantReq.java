package com.iot.tenant.vo.req.lang;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 项目名称：cloud
 * 功能描述：查询基础文案入参（BOSS使用）
 * 创建人： yeshiyuan
 * 创建时间：2018/10/11 18:57
 * 修改人： yeshiyuan
 * 修改时间：2018/10/11 18:57
 * 修改描述：
 */
@ApiModel(description = "查询基础文案入参（BOSS使用）")
public class QueryLangInfoTenantReq {
    @ApiModelProperty(name = "objectId", value = "对象ID", dataType = "Long")
    private String objectId;

    @ApiModelProperty(name = "objectType", value = "对象类型", dataType = "String")
    private String objectType;

    @ApiModelProperty(name = "tenantId", value = "租户id", dataType = "Long")
    private Long tenantId;

    @ApiModelProperty(name = "belongModule", value = "所属模块", dataType = "String")
    private String belongModule;

    @ApiModelProperty(name = "langKey", value = "语言key", dataType = "String")
    private String langKey;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getBelongModule() {
        return belongModule;
    }

    public void setBelongModule(String belongModule) {
        this.belongModule = belongModule;
    }

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public QueryLangInfoTenantReq() {
    }

    public QueryLangInfoTenantReq(String objectId, String objectType, Long tenantId, String belongModule) {
        this.objectId = objectId;
        this.objectType = objectType;
        this.tenantId = tenantId;
        this.belongModule = belongModule;
    }
}
