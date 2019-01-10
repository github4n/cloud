package com.iot.tenant.vo.resp;

import com.iot.tenant.vo.LangKey;

import java.util.List;

/**
 * 描述：获取多语言应答
 * 创建人： LaiGuiMing
 * 创建时间： 2018/7/10 10:53
 */
public class GetLangResp {
    private String chooseLang;
    private List<LangKey> langKeys;

    public String getChooseLang() {
        return chooseLang;
    }

    public void setChooseLang(String chooseLang) {
        this.chooseLang = chooseLang;
    }

    public List<LangKey> getLangKeys() {
        return langKeys;
    }

    public void setLangKeys(List<LangKey> langKeys) {
        this.langKeys = langKeys;
    }
}
