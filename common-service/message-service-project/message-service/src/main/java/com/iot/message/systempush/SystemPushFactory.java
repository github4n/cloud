package com.iot.message.systempush;

import com.iot.message.PropertyConfigureUtil;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：系统推送
 * 功能描述：系统推送
 * 创建人： 李帅
 * 创建时间：2018年11月28日 下午1:56:52
 * 修改人：李帅
 * 修改时间：2018年11月28日 下午1:56:52
 */
public class SystemPushFactory {


	public static ISystemPush createSystemPush() throws Exception{
		ISystemPush systemPush=(ISystemPush)PropertyConfigureUtil.mapProps.get("implement");
		return systemPush;
	}

}
