package com.iot.message.systempush.impl;

import java.util.Map;

import com.iot.message.systempush.ISystemPush;
import com.iot.message.utils.JpshUtil;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：Android系统消息方法
 * 功能描述：Android系统消息方法
 * 创建人： 李帅
 * 创建时间：2018年11月28日 下午1:59:09
 * 修改人：李帅
 * 修改时间：2018年11月28日 下午1:59:09
 */
public class JPush implements ISystemPush {

	/**
	 * 
	 * 描述：Android系统消息方法
	 * @author 李帅
	 * @created 2018年3月1日 下午4:10:25
	 * @since 
	 * @param appId
	 * @param phoneId
	 * @param noticeMap
	 * @param retryNum
	 * @return
	 * @throws Exception
	 */
	@Override
	public void pushAndroidSystemNotification(Long appId, String pushUrl, String pushKey, String phoneId,
			String systemBody, Map<String, String> noticeMap) throws Exception {
		JpshUtil.jpushNotification(appId, pushUrl, pushKey,	phoneId, systemBody, noticeMap);
	}

}
