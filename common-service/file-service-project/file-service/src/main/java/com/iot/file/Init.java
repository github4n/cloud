package com.iot.file;

import com.iot.common.util.CommonUtil;
import com.iot.file.contants.ModuleConstants;
import com.iot.file.storage.IStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

@Component(value = "file_init")
public class Init  {

	@Autowired  
    private YmlConfig ymlConfig;

	@PostConstruct
	public void init() {
	    try {
	    	Map<String, String> mapProps=ymlConfig.getMapProps();
			initProps(mapProps);
			ModuleConstants.expiration = CommonUtil.toInteger(mapProps.get("expiration"), 24);
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
		IStorage storage = (IStorage) clazz.newInstance();
		PropertyConfigureUtil.mapProps.put(implementKey, storage);
	}

}
