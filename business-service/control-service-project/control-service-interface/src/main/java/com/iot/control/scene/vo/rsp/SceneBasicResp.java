package com.iot.control.scene.vo.rsp;

import java.io.Serializable;

/**
 * 项目名称: IOT云平台
 * 模块名称：
 * 功能描述：
 * 创建人: yuChangXing
 * 创建时间: 2018/4/24 9:47
 * 修改人:
 * 修改时间：
 */
public class SceneBasicResp implements Serializable {

    private static final long serialVersionUID = 2025580783894328456L;

    // 情景id
    private Long sceneId;

    // 情景名称
    private String name;

    // 图标
    private String icon;

    // 统计 sceneDetail 数量
    private Integer ruleCount;

    // 1.设备类型  2.全量设置 3.业务类型
    private Integer setType;

    // 排序
    private Integer sort;
    
    private Long locationId;

    private Long createBy;


    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Long getLocationId() {
		return locationId;
	}

	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

    public Long getSceneId() {
        return sceneId;
    }

    public void setSceneId(Long sceneId) {
        this.sceneId = sceneId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getRuleCount() {
        return ruleCount;
    }

    public void setRuleCount(Integer ruleCount) {
        this.ruleCount = ruleCount;
    }

    public Integer getSetType() {
        return setType;
    }

    public void setSetType(Integer setType) {
        this.setType = setType;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
