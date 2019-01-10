package com.iot.message.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class PushNoticeTemplate {

	/**
     * 主键id
     */
    private Long id;
    
    /**
     * 模板id
     */
    private String templateId;

    /**
     * 模板名称
     */
    private String templateName;
    
    /**
     * 模板内容
     */
    private String templateContent;
    
    /**
     * 模板类型
     */
    private String templateType;
    
    /**
     * 创建人
     */
    private String creator;
    
    /**
     * 推送时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 是否已删除
     */
    private String isDelete;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getTemplateContent() {
		return templateContent;
	}

	public void setTemplateContent(String templateContent) {
		this.templateContent = templateContent;
	}

	public String getTemplateType() {
		return templateType;
	}

	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}

    
}