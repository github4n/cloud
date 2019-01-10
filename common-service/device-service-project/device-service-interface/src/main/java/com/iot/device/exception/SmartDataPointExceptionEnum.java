package com.iot.device.exception;

import com.iot.common.exception.IBusinessException;

public enum SmartDataPointExceptionEnum implements IBusinessException {

	SMARTDATAPOINT_POINT_DEL_ERROR("smartDataPoint.delete.error");
	
	private int code;

	private String messageKey;
	
	private SmartDataPointExceptionEnum(String messageKey) {
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

