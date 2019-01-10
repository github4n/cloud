package com.iot.building.ifttt.entity;

import io.swagger.annotations.ApiModelProperty;

public class Trigger {
    private String sensorPosition;
    private String sensorType;
    private String sensorProperties;
    private String sensorDeviceId;

    private String actuctorPosition;
    private String actuctorType;
    private String actuctorProperties;
    private String actuctorDeviceId;

    public String getSensorDeviceId() {
        return sensorDeviceId;
    }

    public void setSensorDeviceId(String sensorDeviceId) {
        this.sensorDeviceId = sensorDeviceId;
    }

    public String getActuctorDeviceId() {
        return actuctorDeviceId;
    }

    public void setActuctorDeviceId(String actuctorDeviceId) {
        this.actuctorDeviceId = actuctorDeviceId;
    }

    public String getSensorProperties() {
        return sensorProperties;
    }

    public void setSensorProperties(String sensorProperties) {
        this.sensorProperties = sensorProperties;
    }

    public String getActuctorProperties() {
        return actuctorProperties;
    }

    public void setActuctorProperties(String actuctorProperties) {
        this.actuctorProperties = actuctorProperties;
    }

    public String getSensorPosition() {
        return sensorPosition;
    }

    public void setSensorPosition(String sensorPosition) {
        this.sensorPosition = sensorPosition;
    }

    public String getSensorType() {
        return sensorType;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

    public String getActuctorPosition() {
        return actuctorPosition;
    }

    public void setActuctorPosition(String actuctorPosition) {
        this.actuctorPosition = actuctorPosition;
    }

    public String getActuctorType() {
        return actuctorType;
    }

    public void setActuctorType(String actuctorType) {
        this.actuctorType = actuctorType;
    }
    private Long appletId;

    public Long getAppletId() {
        return appletId;
    }

    public void setAppletId(Long appletId) {
        this.appletId = appletId;
    }

    private Long id;

    private String lineId;

    private String sourceLabel;

    private Long start;

    private String destinationLabel;

    private Long end;

    private Integer invocationPolicy;

    private String statusTrigger;

    private Long ruleId;

    private Long tenantId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLineId() {
        return lineId;
    }

    public void setLineId(String lineId) {
        this.lineId = lineId == null ? null : lineId.trim();
    }

    public String getSourceLabel() {
        return sourceLabel;
    }

    public void setSourceLabel(String sourceLabel) {
        this.sourceLabel = sourceLabel == null ? null : sourceLabel.trim();
    }

    public Long getStart() {
        return start;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public String getDestinationLabel() {
        return destinationLabel;
    }

    public void setDestinationLabel(String destinationLabel) {
        this.destinationLabel = destinationLabel == null ? null : destinationLabel.trim();
    }

    public Long getEnd() {
        return end;
    }

    public void setEnd(Long end) {
        this.end = end;
    }

    public Integer getInvocationPolicy() {
        return invocationPolicy;
    }

    public void setInvocationPolicy(Integer invocationPolicy) {
        this.invocationPolicy = invocationPolicy;
    }

    public String getStatusTrigger() {
        return statusTrigger;
    }

    public void setStatusTrigger(String statusTrigger) {
        this.statusTrigger = statusTrigger == null ? null : statusTrigger.trim();
    }

    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }
}