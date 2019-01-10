package com.iot.tenant.vo.resp.lang;

/**
 * 项目名称：cloud
 * 功能描述：文案key value
 * 创建人： yeshiyuan
 * 创建时间：2018/10/17 14:28
 * 修改人： yeshiyuan
 * 修改时间：2018/10/17 14:28
 * 修改描述：
 */
public class LangKeyValueResp {

    private String key;

    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public LangKeyValueResp(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
