package com.iot.tenant.vo;

import java.util.List;

/**
 * 描述：语言key配置类
 * 创建人： LaiGuiMing
 * 创建时间： 2018/7/9 13:37
 */
public class LangKey {
    /**
     * key值
     */
    private String key;

    private List<LangItem> items;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<LangItem> getItems() {
        return items;
    }

    public void setItems(List<LangItem> items) {
        this.items = items;
    }
}
