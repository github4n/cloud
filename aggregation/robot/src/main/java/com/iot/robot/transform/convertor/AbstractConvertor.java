package com.iot.robot.transform.convertor;

import com.iot.robot.norm.KeyValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

// 云端的控制属性 转换器（robot-->云端）、（云端-->robot）
@SuppressWarnings({"rawtypes"})
public abstract class AbstractConvertor<T, U> implements ValueConvertor<T, U>, ValueRevertor<T, U> {

	public Logger LOGGER = LoggerFactory.getLogger(AbstractConvertor.class);

	public YunKeyValue<T> valueConvert(KeyValue<U> value, String endpointId) {
		return null;
	}
	
    //将设备支持的转换器取出来
	public static final List<ValueConvertor> selectConvertor(List<AbstractConvertor> p, List<String> ts) {
		List<ValueConvertor> pc = new ArrayList<ValueConvertor>();
		for (AbstractConvertor convertor : p) {
			if (ts.contains(convertor)) {
				int indexOf = p.indexOf(convertor);
				AbstractConvertor target = p.get(indexOf);
				pc.add(target);
			}
		}
		return pc;
	}
	
	//选择当前功能点的转换器
	public static final ValueRevertor selectRevertor(List<AbstractConvertor> p, String fun) {
		for (AbstractConvertor abstractConvertor : p) {
			if (abstractConvertor.equals(fun)) {
				return abstractConvertor;
			}
		}
		return null; 
	}

	public abstract String getCommandKey();
}
