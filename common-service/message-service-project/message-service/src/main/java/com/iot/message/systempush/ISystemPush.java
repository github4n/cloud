package com.iot.message.systempush;

import java.util.Map;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：系统推送
 * 功能描述：系统推送
 * 创建人： 李帅
 * 创建时间：2018年11月28日 下午1:57:04
 * 修改人：李帅
 * 修改时间：2018年11月28日 下午1:57:04
 */
public interface ISystemPush {
	/**
	 * 
	 * 描述：Android系统推送
	 * @author 李帅
	 * @created 2018年11月28日 下午1:57:13
	 * @since 
	 * @param appId
	 * @param pushUrl
	 * @param pushKey
	 * @param phoneId
	 * @param systemBody
	 * @param noticeMap
	 * @throws Exception
	 */
	void pushAndroidSystemNotification(Long appId, String pushUrl, String pushKey, String phoneId,
			String systemBody, Map<String, String> noticeMap) throws Exception;

}
