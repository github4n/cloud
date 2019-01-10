package com.iot.ifttt.vo;

import java.util.Map;

/**
 * 描述：通用请求类
 * 创建人： LaiGuiMing
 * 创建时间： 2018/11/21 10:10
 */
public class CommonReq {
    private User user;
    private Source ifttt_source;
    private String trigger_identity;
    private Map<String,Object> triggerFields;
    private Map<String,Object> actionFields;
    private Integer limit;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Source getIfttt_source() {
        return ifttt_source;
    }

    public void setIfttt_source(Source ifttt_source) {
        this.ifttt_source = ifttt_source;
    }

    public String getTrigger_identity() {
        return trigger_identity;
    }

    public void setTrigger_identity(String trigger_identity) {
        this.trigger_identity = trigger_identity;
    }

    public Map<String, Object> getTriggerFields() {
        return triggerFields;
    }

    public void setTriggerFields(Map<String, Object> triggerFields) {
        this.triggerFields = triggerFields;
    }

    public Map<String, Object> getActionFields() {
        return actionFields;
    }

    public void setActionFields(Map<String, Object> actionFields) {
        this.actionFields = actionFields;
    }

    @Override
    public String toString() {
        return "CommonReq{" +
                "user=" + user +
                ", ifttt_source=" + ifttt_source +
                ", trigger_identity='" + trigger_identity + '\'' +
                ", triggerFields=" + triggerFields +
                ", actionFields=" + actionFields +
                ", limit=" + limit +
                ", name='" + name + '\'' +
                '}';
    }

    //======================= 内部类 =======================

    public static class User{
        private String timezone;

        public String getTimezone() {
            return timezone;
        }

        public void setTimezone(String timezone) {
            this.timezone = timezone;
        }

        @Override
        public String toString() {
            return "User{" +
                    "timezone='" + timezone + '\'' +
                    '}';
        }
    }

    public static class Source{
        private String id;
        private String url;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public String toString() {
            return "Source{" +
                    "id='" + id + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }
    }
}
