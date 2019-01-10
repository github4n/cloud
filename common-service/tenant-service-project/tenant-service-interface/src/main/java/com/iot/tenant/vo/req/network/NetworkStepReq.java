package com.iot.tenant.vo.req.network;

import com.iot.tenant.vo.req.lang.LangInfoReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：配网步骤信息入参
 * 创建人： yeshiyuan
 * 创建时间：2018/10/8 15:22
 * 修改人： yeshiyuan
 * 修改时间：2018/10/8 15:22
 * 修改描述：
 */
@ApiModel(description = "配网步骤信息入参")
public class NetworkStepReq {

    @ApiModelProperty(name = "step", value = "步骤标识", dataType = "Integer")
    private Integer step;

    @ApiModelProperty(name = "icon", value = "图标文件uuid", dataType = "String")
    private String icon;

    @ApiModelProperty(name = "help", value = "配网文案帮助信息", dataType = "NetworkStepHelpReq")
    private NetworkStepHelpReq help;

    @ApiModelProperty(name = "langInfos", value = "配网文案信息", dataType = "List")
    private List<LangInfoReq> langInfos;

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

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

    public NetworkStepHelpReq getHelp() {
        return help;
    }

    public void setHelp(NetworkStepHelpReq help) {
        this.help = help;
    }
}
