package com.iot.tenant.vo.resp;

import com.iot.tenant.vo.LangKey;

import java.util.List;

/**
 * 描述：获取配网引导应答
 * 创建人： LaiGuiMing
 * 创建时间： 2018/7/10 10:01
 */
public class GetGuideResp {
    private String smartImg;
    private String apImg;
    private String chooseLang;
    private List<LangKey> smartConfig;
    private List<LangKey> ap;

    public String getChooseLang() {
        return chooseLang;
    }

    public void setChooseLang(String chooseLang) {
        this.chooseLang = chooseLang;
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
