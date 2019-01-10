package com.iot.file.queue;

import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import com.iot.file.entity.FileBean;

/**
 * 
 * 项目名称：IOT云平台
 * 模块名称：文件服务
 * 功能描述：文件信息队列
 * 创建人： zhouzongwei
 * 创建时间：2018年3月14日 上午9:57:42
 * 修改人： zhouzongwei
 * 修改时间：2018年3月14日 上午9:57:42
 */
public class FileInfoQueue {
	
	 //存放文件信息队列
	 public static LinkedBlockingQueue<FileBean> fileInfoQueue=new LinkedBlockingQueue<>();
	 
	 private static final Logger logger = Logger.getLogger(FileInfoQueue.class);
	 
	 /**
	  * 
	  * 描述：从队列取出
	  * @author zhouzongwei
	  * @created 2018年3月14日 上午10:04:09
	  * @since 
	  * @return
	  */
	 public static FileBean take(){

		FileBean fileBean = null;
		try {
			fileBean = fileInfoQueue.take();
		} catch (InterruptedException e) {
			logger.error("", e);
		}
		return fileBean;
	 }
	 
	 /**
	  * 
	  * 描述：进入队列
	  * @author zhouzongwei
	  * @created 2018年3月14日 上午10:04:34
	  * @since 
	  * @param fileBean
	  */
	 public static void put(FileBean fileBean){
		try {
			fileInfoQueue.put(fileBean);
		} catch (InterruptedException e) {
			logger.error("", e);
		}
	 }

}
