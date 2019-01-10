package com.iot.file.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 文件服务
 * 本地文件上传实现
 * @author fenglijian
 *
 */
public interface LocalFileService {
	
	public String uploadFile(MultipartFile multipartFile, Long tenantId, String path);

	List<String[]> resolveExcel(MultipartFile multipartFile);
}
