package com.iot.tenant.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 项目名称：cloud
 * 功能描述：租户的文案信息实体类
 * 创建人： yeshiyuan
 * 创建时间：2018/9/29 15:12
 * 修改人： yeshiyuan
 * 修改时间：2018/9/29 15:12
 * 修改描述：
 */
@ApiModel(description = "租户的文案信息实体类")
public class LangInfoTenant {

    @ApiModelProperty(name = "id", value = "主键ID", dataType = "Long")
    private Long id;

    @ApiModelProperty(name = "tenantId", value = "租户主键", dataType = "Long")
    private Long tenantId;

    @ApiModelProperty(name = "objectType", value = "对象类型(ProdcutOTA、AppConfig、deviceNetwork、deviceType)", dataType = "String")
    private String objectType;

    @ApiModelProperty(name = "objectId", value = "对象ID(当object_type为deviceNetwork时为appId_productId)", dataType = "String")
    private String objectId;

    @ApiModelProperty(name = "belongModule", value = "所属模块", dataType = "String")
    private String belongModule;

    @ApiModelProperty(name = "langType", value = "语言类型", dataType = "String")
    private String langType;

    @ApiModelProperty(name = "langKey", value = "国际化键", dataType = "String")
    private String langKey;

    @ApiModelProperty(name = "langValue", value = "国际化内容", dataType = "String")
    private String langValue;

    @ApiModelProperty(name = "createBy", value = "创建人", dataType = "Long")
    private Long createBy;

    @ApiModelProperty(name = "createTime", value = "创建时间", dataType = "Date")
    private Date createTime;

    @ApiModelProperty(name = "updateBy", value = "更新人", dataType = "Long")
    private Long updateBy;

    @ApiModelProperty(name = "updateTime", value = "修改时间", dataType = "Date")
    private Date updateTime;

    @ApiModelProperty(name = "isDeleted", value = "数据有效性", dataType = "String")
    private String isDeleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getLangType() {
        return langType;
    }

    public void setLangType(String langType) {
        this.langType = langType;
    }

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public String getLangValue() {
        return langValue;
    }

    public void setLangValue(String langValue) {
        this.langValue = langValue;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getBelongModule() {
        return belongModule;
    }

    public void setBelongModule(String belongModule) {
        this.belongModule = belongModule;
    }

    public LangInfoTenant() {
    }

    public LangInfoTenant(Long tenantId, String objectType, String objectId, String langType, String langKey, String langValue, Long createBy, Date createTime,String belongModule) {
        this.tenantId = tenantId;
        this.objectType = objectType;
        this.objectId = objectId;
        this.langType = langType;
        this.langKey = langKey;
        this.langValue = langValue;
        this.createBy = createBy;
        this.createTime = createTime;
        this.belongModule = belongModule;
    }

    public LangInfoTenant(Long tenantId, String objectType, String objectId, String langType, String langKey, String langValue, Long updateBy, Date updateTime) {
        this.tenantId = tenantId;
        this.objectType = objectType;
        this.objectId = objectId;
        this.langType = langType;
        this.langKey = langKey;
        this.langValue = langValue;
        this.updateBy = updateBy;
        this.updateTime = updateTime;
    }

    public LangInfoTenant(String langKey, String langType, String langValue) {
        this.langType = langType;
        this.langKey = langKey;
        this.langValue = langValue;
    }
}
