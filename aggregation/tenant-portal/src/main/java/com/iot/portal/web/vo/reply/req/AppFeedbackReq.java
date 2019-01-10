package com.iot.portal.web.vo.reply.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 项目名称：cloud
 * 功能描述：app反馈入参
 * 创建人： yeshiyuan
 * 创建时间：2018/11/2 14:36
 * 修改人： yeshiyuan
 * 修改时间：2018/11/2 14:36
 * 修改描述：
 */
@ApiModel(description = "app反馈入参")
public class AppFeedbackReq {

    @ApiModelProperty(name = "appId", value = "appId", dataType = "Long")
    private Long appId;

    @ApiModelProperty(name = "feedbackContent", value = "反馈内容", dataType = "String")
    private String feedbackContent;

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getFeedbackContent() {
        return feedbackContent;
    }

    public void setFeedbackContent(String feedbackContent) {
        this.feedbackContent = feedbackContent;
    }
}
