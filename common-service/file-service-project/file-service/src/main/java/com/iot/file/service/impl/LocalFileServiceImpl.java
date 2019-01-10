package com.iot.file.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.google.common.collect.Maps;
import com.iot.file.entity.FileBean;
import com.iot.file.queue.FileInfoProducer;
import com.iot.file.util.FileUtil;
import com.iot.util.ExcelUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.iot.common.exception.BusinessException;
import com.iot.common.util.CommonUtil;
import com.iot.exception.BusinessExceptionEnum;
import com.iot.file.service.LocalFileService;

/**
 * 文件服务
 * 本地文件上传实现
 * @author fenglijian
 *
 */
@Service
public class LocalFileServiceImpl implements LocalFileService {
	private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

	@Override
	public String uploadFile(MultipartFile multipartFile, Long tenantId, String path) {
		if(CommonUtil.isEmpty(multipartFile)){
			throw new BusinessException(BusinessExceptionEnum.VIDEO_UPLOADFILE_ERROR);
		}
		String fileName = multipartFile.getOriginalFilename();
		String fileType = fileName.substring(fileName.lastIndexOf(".")+1);
		try{
			HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
			// 获取web上下文路径
			String contextPath = httpServletRequest.getSession().getServletContext().getRealPath("/");
			File contextFile = new File(contextPath);
			// 获取和web平级目录下的upload目录
//			String parentContextPath = contextFile.getParentFile().getParentFile().getAbsolutePath()+ File.separator + "upload" + File.separator + tenantId + File.separator;
			File parentContextFile = new File(path);
			
			if(! parentContextFile.exists()) {
				parentContextFile.mkdirs();
			}
			//String path = saveFile(multipartFile.getInputStream(), parentContextPath, fileName);
			path = path + fileName;
			multipartFile.transferTo(new File(path));
//			String storageKey = tenantId+"/"+fileType+"/"+fileName;
			//返回key格式：1000/ts/a1b9a1479d724fb6a275baa994fdacb8.ts    ->   租户id/文件类型/fileId.文件后缀
			//文件信息保存至数据库（推送至队列，由定时任务去存储）
//			FileBean fileBean = FileUtil.createFileBean(storageKey,fileType);
//			FileInfoProducer.addToQueue(fileBean);
			return fileName;
		}catch(Exception e){
			logger.error("上传文件出错:",e);
			throw new  BusinessException(BusinessExceptionEnum.VIDEO_UPLOADFILE_ERROR);
		}
	}


	@Override
	public List<String[]> resolveExcel(MultipartFile multipartFile) {
		try {
			List<String[]> resolveExcel =   ExcelUtils.resolveExcel(multipartFile);
			return resolveExcel;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}


	public String saveFile(InputStream stream,String path,String fileName){
		try{
			String realPath = path + File.separator + fileName;
			FileOutputStream fs = new FileOutputStream( path + File.separator + fileName);
	        byte[] buffer = new byte[1024*1024];
	        int byteSum = 0;
	        int byteRead = 0; 
	        while ((byteRead=stream.read(buffer))!=-1)
	        {
	        	byteSum += byteRead;
	        	fs.write(buffer, 0, byteRead);
	        	fs.flush();
	        } 
	        fs.close();
	        stream.close(); 
	        
	        return realPath;
		}catch(Exception e){
			logger.error("保存文件出错:",e);
			throw new  BusinessException(BusinessExceptionEnum.VIDEO_UPLOADFILE_ERROR);
		}
	}

}
