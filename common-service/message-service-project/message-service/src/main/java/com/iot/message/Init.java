package com.iot.message;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.iot.message.systempush.ISystemPush;

@Component(value = "systempush_init")
public class Init  {

	@Autowired  
    private YmlConfig ymlConfig;

	@PostConstruct
	public void init() {
	    try {
	    	Map<String, String> mapProps=ymlConfig.getMapProps();
			initProps(mapProps);
		} catch (Exception e) {
			e.printStackTrace();
		}  
		
	}


	/**
	 * 
	 * 描述：初始化存储属性
	 * @author zhouzongwei
	 * @created 2018年3月13日 上午11:14:13
	 * @since 
	 * @param mapProps
	 * @throws Exception
	 */
	private void initProps(Map<String, String> mapProps) throws Exception{
		String implementKey = null;
		String implementValue = null;
		for(Map.Entry<String, String> entry : mapProps.entrySet()){
			String key = entry.getKey().toString();  
			String value = entry.getValue().toString();
			PropertyConfigureUtil.mapProps.put(key,value);
			if ("implement".equals(key)) {
				implementKey=key;
				implementValue=value;
			}
		}
		//利用反射生成存储实例
		Class<?> clazz = Class.forName(implementValue);
		ISystemPush systemPush = (ISystemPush) clazz.newInstance();
		PropertyConfigureUtil.mapProps.put(implementKey, systemPush);
	}

}
