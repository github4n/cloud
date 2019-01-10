package com.iot.tenant.vo;

/**
 * 描述：语言配置项
 * 创建人： LaiGuiMing
 * 创建时间： 2018/7/9 11:50
 */
public class LangItem {
    /**
     * 语言标识
     */
    private Integer lang;
    /**
     * 内容
     */
    private String content;

    public Integer getLang() {
        return lang;
    }

    public void setLang(Integer lang) {
        this.lang = lang;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
