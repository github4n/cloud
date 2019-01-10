package com.iot.control.ifttt.entity;

public class Trigger {
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