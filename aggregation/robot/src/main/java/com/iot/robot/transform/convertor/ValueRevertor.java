package com.iot.robot.transform.convertor;

import com.iot.robot.norm.KeyValue;

//云端的控制属性 转换器（云端-->robot）
public interface ValueRevertor<T, U> {
	public KeyValue<U> toCommonKV(YunKeyValue<T> value);
}
