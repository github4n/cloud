package com.iot.file.storage;

import com.iot.file.PropertyConfigureUtil;

/**
 * 
 * 项目名称：IOT云平台
 * 模块名称：文件服务
 * 功能描述：存储工厂
 * 创建人： zhouzongwei
 * 创建时间：2018年3月12日 下午3:20:21
 * 修改人： zhouzongwei
 * 修改时间：2018年3月12日 下午3:20:21
 */
public class StorageFactory {


	public static IStorage createStorage() throws Exception{
		IStorage storage=(IStorage)PropertyConfigureUtil.mapProps.get("implement");
		return storage;
	}

}
