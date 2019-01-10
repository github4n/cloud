package com.iot.message;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：系统推送
 * 功能描述：系统推送
 * 创建人： 李帅
 * 创建时间：2018年11月28日 下午1:56:35
 * 修改人：李帅
 * 修改时间：2018年11月28日 下午1:56:35
 */
@ConfigurationProperties(prefix = "systemPush") 
public class YmlConfig{
	
	private Map<String, String> mapProps = new HashMap<String, String>(); //接收mapProps里面的属性值  

	public Map<String, String> getMapProps() {
		return mapProps;
	}

	public void setMapProps(Map<String, String> mapProps) {
		this.mapProps = mapProps;
	}

}
