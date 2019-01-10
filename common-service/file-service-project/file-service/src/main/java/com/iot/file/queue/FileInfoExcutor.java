package com.iot.file.queue;

import com.iot.file.entity.FileBean;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FileInfoExcutor {
	
	private static final Logger logger = Logger.getLogger(FileInfoConsumer.class);
	/** 线程池 */
	private static ExecutorService excutor = Executors.newCachedThreadPool();
	
	public static void addToQueue(final List<FileBean> fileBeanList, final Long tenantId, final String fileType){
		
		excutor.submit(new Runnable() {

			public void run() {

				try {

					if (fileBeanList != null) {
						for (int i=0;i<fileBeanList.size();i++) {
							// 异步存入到数据库
							FileBean fileBeanDB = new FileBean();
							fileBeanDB.setFileId(fileBeanList.get(i).getFileId());
							fileBeanDB.setFilePath(fileBeanList.get(i).getFilePath());
							fileBeanDB.setId(fileBeanList.get(i).getId());
							fileBeanDB.setTenantId(tenantId);
							fileBeanDB.setFileType(fileType);
							FileInfoProducer.addToQueue(fileBeanDB);
						}
					}

				} catch (Exception e) {
					logger.error("", e);
				}
			}
		});
	}

	public static void addToQueue(final List<FileBean> fileBeanList){
		if ( fileBeanList!=null && !fileBeanList.isEmpty() ){
			FileBean fileBean = fileBeanList.get(0);
			addToQueue(fileBeanList,fileBean.getTenantId(),fileBean.getFileType());
		}
	}
}
