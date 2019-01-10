package com.iot.shcs.common.exception;

import com.iot.common.exception.IBusinessException;

public enum BusinessExceptionEnum implements IBusinessException {

	COMMOMN_EXCEPTION(-1, "Something wrong with the server, please try again."),

	USER_IS_EXIT(21001, "User is exist."),
	
	PARAMETER_ILLEGALITY(-2,"Parameter illegality"),

	HOME_IS_EXIST(-3,"Home name is exist"),
	
	ROOM_IS_EXIST(-3,"Room name is exist"),
	
	SPACE_EXIST_DEVICE_MOUNT(11801,"space exist device mount"),
	
	SPACE_EXIST_TEMPLATE_MOUNT(11801,"space exist template mount"),
	
	SPACE_TYPE_MOUNT_ERROR(11800, "space type mount error"),

	SPACE_ID_IS_NULL(11801,"spaceId.is.null"),

	SPACE_IS_NOT_EXIST(11801,"space.is.not.exist"),

	USER_TENANT_ID_IS_NULL(11802, "user.tenant.id.is.null"),

	USER_ORG_ID_IS_NULL(11803, "user.org.id.is.null"),

	GET_ICOM_ERROR(11804, "get.icom.error"),

	GET_SECURITY_ERROR(11805, "get.security.error"),

	AUTO_IS_EXIST(-3, "Auto name is exist"),

	DEVICE_LICENSE_ERROR(400, "device.license.error"),

	DEVICE_MAC_ERROR(11806, "device.mac.error");
	
	
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
