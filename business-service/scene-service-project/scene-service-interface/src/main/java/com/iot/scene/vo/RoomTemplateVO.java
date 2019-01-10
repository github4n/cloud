package com.iot.scene.vo;

public class RoomTemplateVO {
    /**
     * 
     * 项目名称：立达信IOT云平台
     * 模块名称：
     * 功能描述：房间挂载情景模板
     * 创建人： linjihuang
     * 创建时间：2018年01月17日 上午14:44:06
     * 修改人： linjihuang
     * 修改时间：2018年01月17日 上午14:44:06
     */
    private String sceneId;

    private String templateName;

    private String deviceId;

    private String deviceName;

    private String targetValue;

    private String businessType;
    
    private String parentId;

    public String getSceneId() {
        return sceneId;
    }

    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(String targetValue) {
        this.targetValue = targetValue;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

}
