package com.iot.file;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 
 * 项目名称：IOT云平台
 * 模块名称：文件服务
 * 功能描述：接收yml文件的参数配置
 * 创建人： zhouzongwei
 * 创建时间：2018年3月13日 下午1:43:35
 * 修改人： zhouzongwei
 * 修改时间：2018年3月13日 下午1:43:35
 */
@ConfigurationProperties(prefix = "fileStorage") 
public class YmlConfig{
	
	private Map<String, String> mapProps = new HashMap<String, String>(); //接收mapProps里面的属性值  

	public Map<String, String> getMapProps() {
		return mapProps;
	}

	public void setMapProps(Map<String, String> mapProps) {
		this.mapProps = mapProps;
	}

	

}
