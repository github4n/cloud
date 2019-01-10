package com.iot.building.scene.vo.resp;

public class LocationSceneRelationResp {
    private static final long serialVersionUID = 2025580783894328456L;

    private Long id;
    private Long locationSceneId;
    private Long locationId;
    private String startCron;
    private String endCron;
    private String locationSceneName;
    private String sceneName;
    private Long tenantId;
    
    public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public String getSceneName() {
        return sceneName;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }

    public String getEndCron() {
        return endCron;
    }

    public void setEndCron(String endCron) {
        this.endCron = endCron;
    }

    public String getLocationSceneName() {
        return locationSceneName;
    }

    public void setLocationSceneName(String locationSceneName) {
        this.locationSceneName = locationSceneName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLocationSceneId() {
        return locationSceneId;
    }

    public void setLocationSceneId(Long locationSceneId) {
        this.locationSceneId = locationSceneId;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public String getStartCron() {
        return startCron;
    }

    public void setStartCron(String startCron) {
        this.startCron = startCron;
    }

}
