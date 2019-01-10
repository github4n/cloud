package com.iot.file.queue;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.iot.file.dao.FileMapper;
import com.iot.file.entity.FileBean;

import java.util.Date;

/**
 * 
 * 项目名称：IOT云平台
 * 模块名称：文件服务
 * 功能描述：文件信息消费者
 * 创建人： zhouzongwei
 * 创建时间：2018年3月14日 上午10:16:30
 * 修改人： zhouzongwei
 * 修改时间：2018年3月14日 上午10:16:30
 */
@Component
public class FileInfoConsumer {
	
	private static final Logger logger = Logger.getLogger(FileInfoConsumer.class);
	
	@Autowired
	private FileMapper fileMapper;
	
	public FileInfoConsumer(){
		//启动线程
		WorkThread workThread =new WorkThread();
		workThread.start();
	}
  
	class WorkThread extends Thread{
		
		public void run(){
			
			while(true){
				
			 try {
				 
				FileBean fileBean=FileInfoQueue.fileInfoQueue.take();
				
				if(fileBean!=null){
					fileBean.setCreateTime(new Date());
					fileMapper.insertFileInfo(fileBean);
				}
			} catch (Exception e) {
			 	e.printStackTrace();
				logger.error("",e);
			}
		}
	  }
  }
}
