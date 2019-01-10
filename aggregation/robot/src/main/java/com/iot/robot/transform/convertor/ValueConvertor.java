package com.iot.robot.transform.convertor;

import com.iot.robot.norm.KeyValue;

// 云端的控制属性 转换器（robot-->云端）
public interface ValueConvertor<T,U> {
	YunKeyValue<T> valueConvert(KeyValue<U> value, String endpointId);
}
