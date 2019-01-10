package com.iot.file.queue;

import com.iot.file.entity.FileBean;

/**
 * 
 * 项目名称：IOT云平台
 * 模块名称：文件服务
 * 功能描述：把文件信息入队列
 * 创建人： zhouzongwei
 * 创建时间：2018年3月14日 上午10:10:02
 * 修改人： zhouzongwei
 * 修改时间：2018年3月14日 上午10:10:02
 */
public class FileInfoProducer {
	
	public static void addToQueue(FileBean fileBean) {
		FileInfoQueue.fileInfoQueue.add(fileBean);
	}


}
