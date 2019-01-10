
package com.iot.comm.utils;

/**
 * 短信通道模板
 */
public enum EnumSmsChannelTemplate {
    /**
     * 服务异常提醒
     */
    SERVICE_STATUS_CHANGE("serviceStatusChange", "cloud");


    /**
     * 模板名称
     */
    private String template;
    /**
     * 模板签名
     */
    private String signName;

    EnumSmsChannelTemplate(String template, String signName) {
        this.template = template;
        this.signName = signName;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }
}
