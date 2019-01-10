package com.iot.tenant.vo.req;

import com.iot.tenant.vo.LangKey;

import java.util.List;

/**
 * 描述：保存配网引导请求
 * 创建人： LaiGuiMing
 * 创建时间： 2018/7/10 9:58
 */
public class SaveGuideReq {
    private Long appProductId;
    private String smartImg;
    private String apImg;
    private String chooseLang;
    private List<LangKey> smartConfig;
    private List<LangKey> ap;


    public Long getAppProductId() {
        return appProductId;
    }

    public void setAppProductId(Long appProductId) {
        this.appProductId = appProductId;
    }

    public String getSmartImg() {
        return smartImg;
    }

    public void setSmartImg(String smartImg) {
        this.smartImg = smartImg;
    }

    public String getApImg() {
        return apImg;
    }

    public void setApImg(String apImg) {
        this.apImg = apImg;
    }

    public String getChooseLang() {
        return chooseLang;
    }

    public void setChooseLang(String chooseLang) {
        this.chooseLang = chooseLang;
    }

    public List<LangKey> getSmartConfig() {
        return smartConfig;
    }

    public void setSmartConfig(List<LangKey> smartConfig) {
        this.smartConfig = smartConfig;
    }

    public List<LangKey> getAp() {
        return ap;
    }

    public void setAp(List<LangKey> ap) {
        this.ap = ap;
    }
}
