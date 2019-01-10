package com.iot.message.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class PushNoticeLog {

    /**
     * 推送id
     */
    private Long id;

    /**
     * 发信方
     */
    private String pushFrom;
    
    /**
     * 收信方
     */
    private String pushTo;
    
    
    /**
     * 推送类型
     */
    private String pushType;
    
    /**
     * 推送时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date pushTime;

    /**
     * 模板id
     */
    private String templateId;

    /**
     * 主题
     */
    private String noticeSubject;

    /**
     * 参数值
     */
    private String paramValue;

    /**
     * 推送结果代码
     */
    private String resultCode;

    /**
     * 推送结果类型
     */
    private String resultType;

    /**
     * 推送结果信息
     */
    private String resultMessage;
    
    /**
     * 结果应答时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date resultAnswerTime;

    /**
     * 数据状态
     */
    private String dataStatus;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPushFrom() {
		return pushFrom;
	}

	public void setPushFrom(String pushFrom) {
		this.pushFrom = pushFrom;
	}

	public String getPushTo() {
		return pushTo;
	}

	public void setPushTo(String pushTo) {
		this.pushTo = pushTo;
	}

	public String getPushType() {
		return pushType;
	}

	public void setPushType(String pushType) {
		this.pushType = pushType;
	}

	public Date getPushTime() {
		return pushTime;
	}

	public void setPushTime(Date pushTime) {
		this.pushTime = pushTime;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getNoticeSubject() {
		return noticeSubject;
	}

	public void setNoticeSubject(String noticeSubject) {
		this.noticeSubject = noticeSubject;
	}

	public String getParamValue() {
		return paramValue;
	}

	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultType() {
		return resultType;
	}

	public void setResultType(String resultType) {
		this.resultType = resultType;
	}

	public String getResultMessage() {
		return resultMessage;
	}

	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}

	public Date getResultAnswerTime() {
		return resultAnswerTime;
	}

	public void setResultAnswerTime(Date resultAnswerTime) {
		this.resultAnswerTime = resultAnswerTime;
	}

	public String getDataStatus() {
		return dataStatus;
	}

	public void setDataStatus(String dataStatus) {
		this.dataStatus = dataStatus;
	}

}