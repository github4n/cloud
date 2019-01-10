package com.iot.portal.ota.vo;

import java.util.List;

/**
 * 项目名称：立达信IOT云平台
 * 模块名称：ota
 * 功能描述：ota
 * 创建人： nongchongwei
 * 创建时间：2018年07月24日 16:58
 * 修改人： nongchongwei
 * 修改时间：2018年07月24日 16:58
 */
public class UpgradePlanDetailVo {
    /**
     * 明细id
     */
    private String id;

    /**
     * 是否有过渡版本 0/1
     */
    private Integer hasTransition;

    /**
     * 替换版本号
     */
    private List<String> substituteVersionList;

    /**
     * 过度版本号
     */
    private List<String> transitionVersionList;

    public Integer getHasTransition() {
        return hasTransition;
    }

    public void setHasTransition(Integer hasTransition) {
        this.hasTransition = hasTransition;
    }

    public List<String> getSubstituteVersionList() {
        return substituteVersionList;
    }

    public void setSubstituteVersionList(List<String> substituteVersionList) {
        this.substituteVersionList = substituteVersionList;
    }

    public List<String> getTransitionVersionList() {
        return transitionVersionList;
    }

    public void setTransitionVersionList(List<String> transitionVersionList) {
        this.transitionVersionList = transitionVersionList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}