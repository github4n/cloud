package com.iot.control.scene.vo.req;

import java.io.Serializable;

/**
 * 项目名称: IOT云平台
 * 模块名称：
 * 功能描述： 封装 更新情景的属性
 * 创建人: yuChangXing
 * 创建时间: 2018/5/8 11:51
 * 修改人:
 * 修改时间：
 */
public class SceneUpdateReq implements Serializable {
    private static final long serialVersionUID = -2644110158557101062L;

    private Long sceneId;

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
    
    private Long locationId;
    
    // 静默状态
    private Integer silenceStatus;
	
	public Long getLocationId() {
		return locationId;
	}

	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

    // 排序
    private Integer sort;

    public Long getSceneId() {
        return sceneId;
    }

    public void setSceneId(Long sceneId) {
        this.sceneId = sceneId;
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

    public void setIcon(String icon) {
        this.icon = icon;
    }

	public Integer getSilenceStatus() {
		return silenceStatus;
	}

	public void setSilenceStatus(Integer silenceStatus) {
		this.silenceStatus = silenceStatus;
	}
}
