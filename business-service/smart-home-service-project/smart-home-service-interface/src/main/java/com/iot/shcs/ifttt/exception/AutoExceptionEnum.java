package com.iot.shcs.ifttt.exception;


import com.iot.common.exception.IBusinessException;

public enum AutoExceptionEnum implements IBusinessException {

	RULE_ID_IS_NULL(11302, "RuleId.is.null"),
	RULE_IS_NULL(11303, "Rule.is.null"),
	DELETE_RULE_FAILED(11304, "delete.rule.failed"),
	SAVE_SENSOR_FAILED(11305, "save.sensor.failed"),
	SAVE_RELATION_FAILED(11306, "save.relation.failed"),
	SAVE_TRIGGER_FAILED(11307, "save.trigger.failed"),
	SAVE_ACTUATOR_FAILED(11308, "save.actuator.failed"),
	IFTTT_TEMPLATE_IS_NULL(11309, "ifttt.template.is.null"),
	IFTTT_TEMPLATEID_IS_NULL(11310, "ifttt.templateId.is.null"),
	IFTTT_TEMPLATEID_DEVICEIDS_IS_NULL(11311, "ifttt.template.deviceIds.is.null"),
	TEMPLATE_IS_NULL(113012, "template.is.null"),
	AUTO_IS_EXIST(-3, "Auto name is exist"),

	///////////////////////////////////////IFTTT执行异常////////////////////////////////////////
	SET_SECURITY_STATUS_ERROR(113013, "set.security.status.error"),
	CHECK_NAME_ERROR(113014, "ifttt.check.name.error"),
	EXEC_IFTTT_BY_RULE_ERROR(113015, "exec.ifttt.by.rule.error"),
	ADD_IFTTT_ERROR(113016, "add.ifttt.error"),
	DEL_IFTTT_BY_DEVICEID_ERROR(113017, "del.ifttt.by.deviceId.error"),
	DELETE_IFTTT_ERROR(113018,"delete.ifttt.error"),
	GET_IFTTT_ERROR(113019,"get.ifttt.error"),
	SAVE_RULE_ERROR(113020,"save.rule.error"),
	SAVE_IFTTT_ERROR(113021,"save.ifttt.error"),
	GET_IFTTT_LIST_ERROR(113022, "get.ifttt.list.error"),
	SET_IFTTT_ENABLE_ERROR(113025, "set.ifttt.enable.error"),
	TEMPLATE_IS_ERROR(113023, "template.is.error"),
	UPDATE_RULE_STATUS_FAILED(11400, "update.rule.status.failed");
	private int code;

	private String messageKey;

	AutoExceptionEnum(String messageKey) {
		code = 0;
		this.messageKey = messageKey;
	}

	AutoExceptionEnum(int code, String messageKey) {
		this.code = code;
		this.messageKey = messageKey;
	}

	@Override
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	@Override
	public String getMessageKey() {
		return messageKey;
	}

	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}

}
