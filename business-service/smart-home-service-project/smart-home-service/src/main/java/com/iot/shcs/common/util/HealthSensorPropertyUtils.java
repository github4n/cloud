package com.iot.shcs.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Properties;

@Component
public class HealthSensorPropertyUtils {
	private static final Logger logger = LoggerFactory.getLogger(HealthSensorPropertyUtils.class);

	private static final String PROPS_FILE_NAME = "health-sensor.properties";

	public static final String PROPERTY_HEALTH_SENSOR_TYPES = "health.sensor.types";
	public static final String PROPERTY_HEALTH_SENSOR_PROPERTYNAMES = "health.sensor.propertyNames";

	private static Properties props;

	static {
		loadProps();
	}

	synchronized static private void loadProps() {
		logger.info("开始加载properties文件内容.......");
		props = new Properties();
		InputStream in = null;
//		try {
//			// 第一种，通过类加载器进行获取properties文件流
//			in = HealthSensorPropertyUtils.class.getClassLoader().getResourceAsStream(PROPS_FILE_NAME);
//			// 第二种，通过类进行获取properties文件流
//			// in = PropertyUtil.class.getResourceAsStream("/jdbc.properties");
//			props.load(in);
//		} catch (FileNotFoundException e) {
//			logger.error("{}文件未找到", PROPS_FILE_NAME);
//		} catch (IOException e) {
//			logger.error("出现IOException");
//		} finally {
//			try {
//				if (null != in) {
//					in.close();
//				}
//			} catch (IOException e) {
//				logger.error("{}文件流关闭出现异常", PROPS_FILE_NAME);
//			}
//		}
		logger.info("加载properties文件内容完成...........");
		logger.info("properties文件内容：" + props);
	}

	public static String getProperty(String key) {
		if (null == props) {
			loadProps();
		}
		return props.getProperty(key);
	}

	public static String getProperty(String key, String defaultValue) {
		if (null == props) {
			loadProps();
		}
		return props.getProperty(key, defaultValue);
	}
}
