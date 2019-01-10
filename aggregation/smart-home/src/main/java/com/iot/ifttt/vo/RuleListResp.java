package com.iot.ifttt.vo;

import lombok.Data;
import lombok.ToString;

/**
 * 描述：rule列表应答
 * 创建人： LaiGuiMing
 * 创建时间： 2018/5/9 9:35
 */
@Data
@ToString
public class RuleListResp {
    private String autoId;
    private String name;
    private Integer enable;
    private String type;
    private String icon;
    private String time;
    private String executeTime; //执行时间
    private String executeDate; //执行日期
    private Integer devSceneId;
    private Integer devTimerId;

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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(String executeTime) {
        this.executeTime = executeTime;
    }

    public String getExecuteDate() {
        return executeDate;
    }

    public void setExecuteDate(String executeDate) {
        this.executeDate = executeDate;
    }
}
