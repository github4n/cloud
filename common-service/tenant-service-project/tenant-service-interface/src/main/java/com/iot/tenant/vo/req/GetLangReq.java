package com.iot.tenant.vo.req;

import java.util.List;

/**
 * 描述：获取多语言信息请求
 * 创建人： LaiGuiMing
 * 创建时间： 2018/7/9 19:42
 */
public class GetLangReq {
    private Long appId;
    private List<String> keys;

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public List<String> getKeys() {
        return keys;
    }

    public void setKeys(List<String> keys) {
        this.keys = keys;
    }

    @Override
    public String toString() {
        return "GetLangReq{" +
                "appId=" + appId +
                ", keys=" + keys +
                '}';
    }
}
