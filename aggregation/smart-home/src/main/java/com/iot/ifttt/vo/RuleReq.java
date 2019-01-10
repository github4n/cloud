package com.iot.ifttt.vo;

import lombok.Data;
import lombok.ToString;

/**
 * 描述：保存rule请求
 * 创建人： LaiGuiMing
 * 创建时间： 2018/5/8 17:52
 */
@Data
@ToString
public class RuleReq {

    private String cookieUserId;
    private String cookieUserToken;
    private String autoId;
    private String name;
    private String icon;
    private String homeId;
    private Integer enable;
    private String type;
    private String templateFlag;
    private String seq;
    private Integer devSceneId;
    private Integer devTimerId;
    private Integer visiable; //是否可见

    public String getCookieUserId() {
        return cookieUserId;
    }

    public void setCookieUserId(String cookieUserId) {
        this.cookieUserId = cookieUserId;
    }

    public String getCookieUserToken() {
        return cookieUserToken;
    }

    public void setCookieUserToken(String cookieUserToken) {
        this.cookieUserToken = cookieUserToken;
    }

    public String getAutoId() {
        return autoId;
    }

    public void setAutoId(String autoId) {
        this.autoId = autoId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getHomeId() {
        return homeId;
    }

    public void setHomeId(String homeId) {
        this.homeId = homeId;
    }

    public Integer getEnable() {
        return enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTemplateFlag() {
        return templateFlag;
    }

    public void setTemplateFlag(String templateFlag) {
        this.templateFlag = templateFlag;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    @Override
    public String toString() {
        return "RuleReq{" +
                "cookieUserId='" + cookieUserId + '\'' +
                ", cookieUserToken='" + cookieUserToken + '\'' +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", homeId='" + homeId + '\'' +
                ", enable='" + enable + '\'' +
                ", type='" + type + '\'' +
                ", seq='" + seq + '\'' +
                ", templateFlag='" + templateFlag + '\'' +
                '}';
    }
}
