package com.iot.system.vo;
/**
 *
 * 项目名称：IOT云平台
 * 模块名称：用户
 * 功能描述：国际化
 * 创建人： maochengyuan
 * 创建时间：2018/7/12 10:08
 * 修改人： maochengyuan
 * 修改时间：2018/7/12 10:08
 * 修改描述：
 */
public class LangInfoResp {

    /**
     * 语言类型
     */
    private String langType;

    /**
     * 语言key
     */
    private String LangKey;

    /**
     * 语言value
     */
    private String langValue;

    public String getLangType() {
        return langType;
    }

    public void setLangType(String langType) {
        this.langType = langType;
    }

    public String getLangKey() {
        return LangKey;
    }

    public void setLangKey(String langKey) {
        LangKey = langKey;
    }

    public String getLangValue() {
        return langValue;
    }

    public void setLangValue(String langValue) {
        this.langValue = langValue;
    }

}