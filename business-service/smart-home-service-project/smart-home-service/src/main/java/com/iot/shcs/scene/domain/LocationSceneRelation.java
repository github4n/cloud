package com.iot.shcs.scene.domain;

public class LocationSceneRelation {
    private static final long serialVersionUID = 2025580783894328456L;

    private Long id;
    private Long locationSceneId;
    private Long locationId;
    private String startCron;
    private String endCron;

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

    public String getEndCron() {
        return endCron;
    }

    public void setEndCron(String endCron) {
        this.endCron = endCron;
    }
}
