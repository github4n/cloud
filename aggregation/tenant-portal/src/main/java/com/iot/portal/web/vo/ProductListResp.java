package com.iot.portal.web.vo;

/**
 * 描述：
 * 创建人： LaiGuiMing
 * 创建时间： 2018/7/10 17:14
 */
public class ProductListResp {
    private Long id;
    private String name;
    private String iconUrl;
    private String iconFileId;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getIconFileId() {
        return iconFileId;
    }

    public void setIconFileId(String iconFileId) {
        this.iconFileId = iconFileId;
    }
}
