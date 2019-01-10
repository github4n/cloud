package com.iot.tenant.vo.resp.network;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 项目名称：cloud
 * 功能描述：配网步骤格式
 * 创建人： yeshiyuan
 * 创建时间：2018/10/18 11:53
 * 修改人： yeshiyuan
 * 修改时间：2018/10/18 11:53
 * 修改描述：
 */
@ApiModel(description = "配网步骤格式")
public class NetworkStepFormat {
    @ApiModelProperty(name = "description", value = "描述", dataType = "String")
    private String description;

    @ApiModelProperty(name = "icon", value = "图标", dataType = "String")
    private String icon;

    @ApiModelProperty(name = "next", value = "按钮文案", dataType = "String")
    private String next;

    @ApiModelProperty(name = "help", value = "帮助内容", dataType = "String")
    private String help;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getHelp() {
        return help;
    }

    public void setHelp(String help) {
        this.help = help;
    }
}
