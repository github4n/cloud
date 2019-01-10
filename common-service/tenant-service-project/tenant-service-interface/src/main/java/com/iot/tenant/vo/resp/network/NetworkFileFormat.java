package com.iot.tenant.vo.resp.network;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Set;

/**
 * 项目名称：cloud
 * 功能描述：配网步骤文件格式
 * 创建人： yeshiyuan
 * 创建时间：2018/10/18 11:48
 * 修改人： yeshiyuan
 * 修改时间：2018/10/18 11:48
 * 修改描述：
 */
@ApiModel(description = "配网步骤文件格式")
public class NetworkFileFormat {

    @ApiModelProperty(name = "name", value = "产品名称", dataType = "String")
    private String name;

    @ApiModelProperty(name = "tabs", value = "配网信息", dataType = "List")
    private List<NetworkTabsFormat> tabs;

    @ApiModelProperty(name = "fileIds", value = "配网引导图列表", dataType = "Set")
    private Set<String> fileIds;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<NetworkTabsFormat> getTabs() {
        return tabs;
    }

    public void setTabs(List<NetworkTabsFormat> tabs) {
        this.tabs = tabs;
    }

    public Set<String> getFileIds() {
        return fileIds;
    }

    public void setFileIds(Set<String> fileIds) {
        this.fileIds = fileIds;
    }
}
