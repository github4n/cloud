package com.iot.tenant.vo.resp.lang;

import com.iot.tenant.vo.req.lang.LangInfoReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：基础文案出参(BOSS专用)
 * 创建人： yeshiyuan
 * 创建时间：2018/9/30 11:31
 * 修改人： yeshiyuan
 * 修改时间：2018/9/30 11:31
 * 修改描述：
 */
@ApiModel(description = "基础文案出参(BOSS专用)")
public class LangInfoBaseResp {

    @ApiModelProperty(name = "objectId", value = "对象ID", dataType = "Long")
    private Long objectId;

    @ApiModelProperty(name = "objectType", value = "对象类型", dataType = "String")
    private String objectType;

    @ApiModelProperty(name = "belongModule", value = "所属模块", dataType = "String")
    private String belongModule;

    @ApiModelProperty(name = "langInfos", value = "语言信息", dataType = "List")
    private List<LangInfoReq> langInfos;

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

    public LangInfoBaseResp() {
    }

    public LangInfoBaseResp(Long objectId, String objectType, String belongModule) {
        this.objectId = objectId;
        this.objectType = objectType;
        this.belongModule = belongModule;
    }
}
