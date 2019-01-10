package com.iot.boss.vo.reply.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 项目名称：cloud
 * 功能描述：app留言入参
 * 创建人： yeshiyuan
 * 创建时间：2018/11/2 14:36
 * 修改人： yeshiyuan
 * 修改时间：2018/11/2 14:36
 * 修改描述：
 */
@ApiModel(description = "app留言入参")
public class AppLeaveMessageReq {

    @ApiModelProperty(name = "appId", value = "appId", dataType = "Long")
    private Long appId;

    @ApiModelProperty(name = "operateDesc", value = "操作描述", dataType = "String")
    private String operateDesc;

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getOperateDesc() {
        return operateDesc;
    }

    public void setOperateDesc(String operateDesc) {
        this.operateDesc = operateDesc;
    }
}
