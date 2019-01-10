package com.iot.portal.web.vo;

/**
 * 描述： 个性化设置请求
 * 创建人： LaiGuiMing
 * 创建时间： 2018/7/9 15:38
 */
public class SetIndividuationReq {
    private Long appId;
    private String logoFileId;
    private String loadImgFileId;
    private Integer theme;

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getLogoFileId() {
        return logoFileId;
    }

    public void setLogoFileId(String logoFileId) {
        this.logoFileId = logoFileId;
    }

    public String getLoadImgFileId() {
        return loadImgFileId;
    }

    public void setLoadImgFileId(String loadImgFileId) {
        this.loadImgFileId = loadImgFileId;
    }

    public Integer getTheme() {
        return theme;
    }

    public void setTheme(Integer theme) {
        this.theme = theme;
    }
}
