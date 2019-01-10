package com.iot.tenant.vo.req.network;

import com.iot.tenant.vo.req.lang.LangInfoReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：配网步骤帮助信息入参
 * 创建人： yeshiyuan
 * 创建时间：2018/10/8 15:22
 * 修改人： yeshiyuan
 * 修改时间：2018/10/8 15:22
 * 修改描述：
 */
@ApiModel(description = "配网步骤帮助信息入参")
public class NetworkStepHelpReq {

    @ApiModelProperty(name = "icon", value = "图标文件uuid", dataType = "String")
    private String icon;

    @ApiModelProperty(name = "iconUrl", value = "图片url", dataType = "String")
    private String iconUrl;

    @ApiModelProperty(name = "langInfos", value = "配网文案信息", dataType = "List")
    private List<LangInfoReq> langInfos;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<LangInfoReq> getLangInfos() {
        return langInfos;
    }

    public void setLangInfos(List<LangInfoReq> langInfos) {
        this.langInfos = langInfos;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
}
