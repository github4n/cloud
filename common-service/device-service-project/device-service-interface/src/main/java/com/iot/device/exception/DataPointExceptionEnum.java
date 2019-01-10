package com.iot.device.exception;

import com.iot.common.exception.IBusinessException;

public enum DataPointExceptionEnum implements IBusinessException {

	POINT_IS_USED_BY_PRODUCT("point.is.used.by.product"),
	POINT_IS_USED_BY_DEVTYPE("point.is.used.by.deviceType"),
	POINT_IS_NOT_CUSTOM("point.is.not.custom"),
	POINT_ADD_ERROR("point.add.error"),
	POINT_UPDATE_ERROR("point.update.error"),
	POINT_DEL_ERROR("point.del.error"),
	CUSTOM_POINT_DEL_ERROR("custom.point.delete.error");
	
	private int code;

	private String messageKey;
	
	private DataPointExceptionEnum(String messageKey) {
		code = 0;
		this.messageKey = messageKey;
    }
	@Override
	public int getCode() {
		// TODO Auto-generated method stub
		return code;
	}

	@Override
	public String getMessageKey() {
		// TODO Auto-generated method stub
		return messageKey;
	}

}
