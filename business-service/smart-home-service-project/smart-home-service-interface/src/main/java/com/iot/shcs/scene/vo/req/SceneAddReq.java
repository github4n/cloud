package com.iot.shcs.scene.vo.req;

import java.io.Serializable;

/**
 * 项目名称: IOT云平台
 * 模块名称：
 * 功能描述： 封装 保存情景的属性
 * 创建人: yuChangXing
 * 创建时间: 2018/5/8 11:51
 * 修改人:
 * 修改时间：
 */
public class SceneAddReq implements Serializable {
    private static final long serialVersionUID = -2644110158557101062L;

    /** 情景名称*/
    private String sceneName;

    /** 空间ID*/
    private Long spaceId;

    /** 操作者*/
    private Long userId;

    /** 图标*/
    private String icon;

    // 1.设备类型  2.全量设置 3.业务类型
    private Integer setType;

    // 排序
    private Integer sort;
    
    private Long locationId;

    // 模板id
    private Long templateId;
    
    // 情景设备目标值
    private String deviceTarValues;
    
    // 情景Id
    private Long sceneId;
	
	public Long getLocationId() {
		return locationId;
	}

	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

    public String getSceneName() {
        return sceneName;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }

    public Long getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(Long spaceId) {
        this.spaceId = spaceId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

	public String getDeviceTarValues() {
		return deviceTarValues;
	}

	public void setDeviceTarValues(String deviceTarValues) {
		this.deviceTarValues = deviceTarValues;
	}

	public Long getSceneId() {
		return sceneId;
	}

	public void setSceneId(Long sceneId) {
		this.sceneId = sceneId;
	}
}
