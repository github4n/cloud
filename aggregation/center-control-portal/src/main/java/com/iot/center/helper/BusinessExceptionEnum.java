package com.iot.center.helper;

import com.iot.common.exception.IBusinessException;

public enum BusinessExceptionEnum implements IBusinessException {
	
	ILLEGAL_CHARACTER(0, "illegal character, please try again."),
	
	COMMOMN_EXCEPTION(-1, "Something wrong with the server, please try again."),

	USER_NAME_PASSWORD_EXCEPTION(400, "username or password is error"),
	
	PASSWORD_ERROR_TIMES_EXCEPTION(500, "password is error 3 times,please 10 mins again"),
	
	SUPER_ADMIN_EXCEPITON(-1000, "super admin donot edit"),
	
	USER_NOT_LOGIN(0, "not login"),
	
	SCENENAME_IS_EXIST(-3, "Scene name is exist"),
	
	SCENE_TEMPLATE_NAME_IS_EXIST(10086, "Scene template name is exist"),
	
	USER_IS_EXIT(21001, "User is exist."),
	
	PARAMETER_ILLEGALITY(-2,"Parameter illegality"),
	
	SPACE_EXIST_DEVICE_MOUNT(11801,"space exist device mount"),
	
	DATA_REPEAT(10106,"data repeat"),
	
	 /**上传文件出错*/
    VIDEO_UPLOADFILE_ERROR("fileservice.upLoadFile.error"),
	
	/** 不存在目标值*/
	THERE_NO_TARGETVALUE(10123,"there is no targetValue"),
	
	FIND_RULE_LIST_FAILED(11303, "findRuleList failed"),
	
	SAVE_RULE_FAILED(11300, "save rule failed"),
	
	SAVE_RULE_PARAM_ERROR(11301, "save rule param error"),
	
	GET_RULEBYID_FAILED(11302, "getRuleById failed"),
	
	UPDATE_RULE_STATUS_FAILED(11305, "update rule status failed"),
	
	RUN_RULE_FAILED(11306, "run rule failed"),
	
	STATE_CALCULATOR_FAILED(11307, "state claculator failed"),
	
	RULE_SENSORS_EMPTY(11308, "rule's sensors empty"),
	
	RULE_TRIGGERS_EMPTY(11309, "rule's triggers empty"),
	
	RULE_ACTUATORS_EMPTY(11310, "rule's actuators empty"),
	
	RULE_RELATIONS_SIZE_ERROR(11311, "relations cannot be greater than 1"),
	
	RULE_RELATION_DID_NOT_CONNECTED_ANY(11312, "relation is not connected to any sensor"),
	
	RULE_RELATION_DID_NOT_CONNECTED_ALL(11313, "relation did not connected all sensors"),
	
	RULE_SENSOR_DID_NOT_CONNECTED_BY_RELATION(11314, "some sensor did not connected by relation"),
	
	RULE_ACTUATOR_DID_NOT_CONNECTED_BY_TRIGGER(11315, "some actuator did not connected by any trigger"),
	
	RULE_ACTUATOR_CONNECT_BY_MULTI(11317, "some actuator connect by multiple trigger"),
	
	RULE_ACTUATOR_RELATION_CONNECTION_WRONG(11318, "relations is not null but actuator did not connected the relation"),
	
	DELETE_BY_RULE_ID_FAILED(11304, "deleteByPrimaryKey failed"),
	
	SPACE_TYPE_MOUNT_ERROR(11800, "space type mount error"),
	
	NOT_EXIST_SPACE(11319,"not exist space"),
	
	BUILDID_OR_TYPES_EMPTY(11320,"buildId or types empty"),
	
	PARAM_IS_NULL(11321,"param is null"),
	
	PLEASE_SELECT_THE_SPACE_FIRST(11322,"Please select the space first"),
	
	PASSWORD_IS_ERROR(11323,"password is error"),
	
	CODE_IS_INVALID(11324,"code is invalid"),
	
	CODE_IS_ERROR(12324,"code is error"),
	
	SEND_IS_FAST(12325,"send is fast"),
	
	GATEWAY_UNCONNECT(11325,"gateway unconnect"),
	
	UPLOAD_FILE_EMPTY(11326,"upload file empty"),
	
	UPLOAD_FILE_EXCEPTION(11401,"upload file exception"),

	BUSINESS_TYPE_IS_EXIST(11327,"business type is exist"),
	
	SCENE_TEGOTHER_IS_EXIST(12327,"scene tegother is exist"),

	DEVICE_EXIST_DEVICE_HAS_CHILD(11329,"device delete error has chlid or device not exsit"),
	
	/** 有关联关系 删除失败*/
	DELETE_RELATION_DELETE_FILED(10719,"have relation not delete");
	/** 异常代码 */
	private int code;
	
	/** 异常描述 */
	private String messageKey;

	BusinessExceptionEnum(String messageKey) {
		this.code = 0;
		this.messageKey = messageKey;
	}

	/**
	 * 
	 * 描述：构建异常
	 * @author mao2080@sina.com
	 * @created 2017年3月21日 上午10:50:58
	 * @since 
	 * @param code 错误代码
	 * @param messageKey 错误描述
	 * @return
	 */
	BusinessExceptionEnum(Integer code, String messageKey) {
		this.code = code;
		this.messageKey = messageKey;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessageKey() {
		return messageKey;
	}

	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}
	
}
