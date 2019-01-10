package com.iot.file.storage.impl;

import com.iot.file.entity.FileBean;
import com.iot.file.storage.IStorage;
import com.iot.common.exception.BusinessException;

import java.io.File;
import java.io.InputStream;
import java.util.List;


/**
 * 
 * 项目名称：IOT云平台
 * 模块名称：文件服务
 * 功能描述：谷歌存储实现类
 * 创建人： zhouzongwei
 * 创建时间：2018年3月12日 下午3:19:16
 * 修改人： zhouzongwei
 * 修改时间：2018年3月12日 下午3:19:16
 */
public class GoogleStorage implements IStorage {

	@Override
	public FileBean getPutUrl(Long tenantId, String fileType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FileBean> getPutUrl(Long tenantId, String fileType, Integer urlNum) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FileBean getGetUrl(String filePath) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FileBean> getGetUrl(List<FileBean> filePaths) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteObject(String filePath) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteObject(List<String> filePaths) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 
	 * 描述：单个上传文件到s3
	 * @author mao2080@sina.com
	 * @created 2017年4月11日 下午5:13:43
	 * @since 
	 * @param filePath 文件路径
	 * @param file 文件对象
	 * @return 文件ID
	 * @throws BusinessException
	 */
	public String putObject(String filePath, File file, int expiration) throws Exception{
		return null;
	}

	@Override
	public String putObject(String filePath, File file, String fileType) throws Exception {
		return null;
	}

	@Override
	public String putObject(String filePath, File file) throws Exception {
		return null;
	}

	@Override
	public FileBean getUploadUrl(String preFilePath, String fileType) throws Exception {
		return null;
	}

	@Override
	public String putObject(String preFilePath, String fileType, InputStream inputStream) throws Exception {
		return null;
	}

	@Override
	public String putObject(String absolutePath, InputStream inputStream) throws Exception {
		return null;
	}

	@Override
	public String getObjectContentMD5(String path) throws Exception {
		return null;
	}
}
