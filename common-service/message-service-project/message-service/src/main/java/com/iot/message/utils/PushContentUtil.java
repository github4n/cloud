package com.iot.message.utils;

import java.io.IOException;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import freemarker.template.TemplateException;

@Service
public class PushContentUtil {

	private static final Logger log = Logger.getLogger(PushContentUtil.class);

	/**
	 * 
	 * 描述：获取模板内容
	 * 
	 * @author 李帅
	 * @created 2017年6月6日 上午10:40:31
	 * @since
	 * @param name
	 *            模板文件的名称
	 * @param map
	 *            与模板内容转换对象
	 * @return
	 * @throws IOException
	 * @throws TemplateException
	 */
	public static String generateHtmlFromFtl(Map<String, String> map, String temp)
			throws IOException, TemplateException {
		temp = maptoHtmlFromTemplate(temp, map);
		return temp.toString();
	}

	/**
	 * 
	 * 描述：模板内容替换
	 * 
	 * @author 李帅
	 * @created 2018年3月17日 上午11:18:14
	 * @since
	 * @param temp
	 * @param map
	 * @return
	 * @throws IOException
	 * @throws TemplateException
	 */
	public static String maptoHtmlFromTemplate(String temp, Map<String, String> map) throws IOException {
		String emailContext = temp.toString();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			if (temp.contains("${" + entry.getKey().toString() + "}")) {
				emailContext = emailContext.replace("${" + entry.getKey().toString() + "}",
						entry.getValue().toString());
			}
		}
		log.debug("emailContext" + emailContext);
		return emailContext;
	}
}
