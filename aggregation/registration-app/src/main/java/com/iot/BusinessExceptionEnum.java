package com.iot;

import com.iot.common.exception.IBusinessException;

public enum BusinessExceptionEnum implements IBusinessException {
	
	COMMOMN_EXCEPTION(-1, "Something wrong with the server, please try again."),

	PARAMETER_ILLEGALITY(-2,"Parameter illegality");
	
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
