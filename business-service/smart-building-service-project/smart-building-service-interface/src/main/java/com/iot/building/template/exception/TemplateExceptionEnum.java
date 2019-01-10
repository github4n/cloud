package com.iot.building.template.exception;


import com.iot.common.exception.IBusinessException;

public enum TemplateExceptionEnum implements IBusinessException {

	TEMPLATE_NAME_IS_NULL(100, "template.name.is.null"),
	TEMPLATE_TYPE_IS_NULL(101, "template.type.is.null"),
	TEMPLATE_IS_NOT_EXIST(103, "template.is.not.exist"),
	TEMPLATE_SCENE_CONTENT_IS_EMPTY(104, "template.scene.content.is.empty"),
	PARAMETER_DEVICE_ID_LIST_IS_EMPTY(105, "parameter.deviceIdList.is.empty"),
	TEMPLATE_NAME_IS_REPEAT(201, "template.name.is.repeat");

	private int code;

	private String messageKey;

	TemplateExceptionEnum(String messageKey) {
		code = 0;
		this.messageKey = messageKey;
	}

	TemplateExceptionEnum(int code, String messageKey) {
		this.code = code;
		this.messageKey = messageKey;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}

	@Override
	public int getCode() {
		return code;
	}

	@Override
	public String getMessageKey() {
		return messageKey;
	}

}
