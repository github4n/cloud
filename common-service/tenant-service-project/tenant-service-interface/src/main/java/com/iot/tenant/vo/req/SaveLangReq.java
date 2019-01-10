package com.iot.tenant.vo.req;

import com.iot.tenant.vo.LangKey;

import java.util.List;

/**
 * 描述：保存多语言配置请求
 * 创建人： LaiGuiMing
 * 创建时间： 2018/7/9 11:17
 */
public class SaveLangReq {
    private Long appId;
    private String chooseLang;
    private List<LangKey> langKeys;

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public List<LangKey> getLangKeys() {
        return langKeys;
    }

    public void setLangKeys(List<LangKey> langKeys) {
        this.langKeys = langKeys;
    }

    public String getChooseLang() {
        return chooseLang;
    }

    public void setChooseLang(String chooseLang) {
        this.chooseLang = chooseLang;
    }

    @Override
    public String toString() {
        return "SaveLangReq{" +
                "appId=" + appId +
                ", chooseLang='" + chooseLang + '\'' +
                ", langKeys=" + langKeys +
                '}';
    }
}
