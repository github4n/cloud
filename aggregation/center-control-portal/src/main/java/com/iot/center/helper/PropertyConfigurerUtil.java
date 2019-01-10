package com.iot.center.helper;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;


/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：常用工具
 * 功能描述：配置文件读取工具类
 * 创建人： mao2080@sina.com
 * 创建时间：2017年3月22日 上午9:16:10
 * 修改人： mao2080@sina.com
 * 修改时间：2017年3月22日 上午9:16:10
 */
public class PropertyConfigurerUtil extends PropertyPlaceholderConfigurer{
	
	/**日志*/
	private static final Log log = LogFactory.getLog(PropertyConfigurerUtil.class);
	
	/**存放Property信息*/
	public static Map<String, String> PROPERTY_MAP = new HashMap<String, String>();

	@Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props) throws BeansException {
        try {
			super.processProperties(beanFactory, props);
			for (Map.Entry<Object, Object> entry : props.entrySet()) {
				PROPERTY_MAP.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
			}
		} catch (Exception e) {
			log.error(e);
		}
    }
	
	public static String getPropertyValue(String key){
		return PROPERTY_MAP.get(key);
	}

}
