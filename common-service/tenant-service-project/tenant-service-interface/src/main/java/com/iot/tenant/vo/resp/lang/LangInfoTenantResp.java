package com.iot.tenant.vo.resp.lang;

import com.iot.tenant.vo.req.lang.LangInfoReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：租户文案出参(portal专用)
 * 创建人： yeshiyuan
 * 创建时间：2018/9/30 11:31
 * 修改人： yeshiyuan
 * 修改时间：2018/9/30 11:31
 * 修改描述：
 */
@ApiModel(description = "租户文案出参(portal专用)")
public class LangInfoTenantResp {

    @ApiModelProperty(name = "objectId", value = "对象ID", dataType = "String")
    private String objectId;

    @ApiModelProperty(name = "objectType", value = "对象类型", dataType = "String")
    private String objectType;

    @ApiModelProperty(name = "belongModule", value = "所属模块", dataType = "String")
    private String belongModule;

    @ApiModelProperty(name = "langInfos", value = "语言信息", dataType = "List")
    private List<LangInfoReq> langInfos;

    @ApiModelProperty(name = "langTypes", value = "语言类型排序", dataType = "List")
    private List<String> langTypes;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public List<LangInfoReq> getLangInfos() {
        return langInfos;
    }

    public void setLangInfos(List<LangInfoReq> langInfos) {
        this.langInfos = langInfos;
    }

    public String getBelongModule() {
        return belongModule;
    }

    public void setBelongModule(String belongModule) {
        this.belongModule = belongModule;
    }

    public List<String> getLangTypes() {
        return langTypes;
    }

    public void setLangTypes(List<String> langTypes) {
        this.langTypes = langTypes;
    }

    public LangInfoTenantResp() {
    }

    public LangInfoTenantResp(String objectId, String objectType, String belongModule) {
        this.objectId = objectId;
        this.objectType = objectType;
        this.belongModule = belongModule;
    }
}
