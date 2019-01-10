package com.iot.boss.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 项目名称: IOT云平台
 * 模块名称：
 * 功能描述：
 * 创建人: chq
 * 创建时间: 2018/6/27 20:41
 * 修改人:
 * 修改时间：
 */
public class TemplateVoResp implements Serializable {

    private static final long serialVersionUID = -7276209311915920905L;

    /**
     * 安防模板列表
     */
    private List<Map> securityTempaltes;

    /**
     * ifttt模板列表
     */
    private List<Map> iftttTempaltes;

    /**
     * scene模板列表
     */
    private List<Map> sceneTempaltes;

    public List<Map> getSecurityTempaltes() {
        return securityTempaltes;
    }

    public void setSecurityTempaltes(List<Map> securityTempaltes) {
        this.securityTempaltes = securityTempaltes;
    }

    public List<Map> getIftttTempaltes() {
        return iftttTempaltes;
    }

    public void setIftttTempaltes(List<Map> iftttTempaltes) {
        this.iftttTempaltes = iftttTempaltes;
    }

    public List<Map> getSceneTempaltes() {
        return sceneTempaltes;
    }

    public void setSceneTempaltes(List<Map> sceneTempaltes) {
        this.sceneTempaltes = sceneTempaltes;
    }
}
