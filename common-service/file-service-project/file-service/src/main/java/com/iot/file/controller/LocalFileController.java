package com.iot.file.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.iot.common.exception.BusinessException;
import com.iot.common.util.CommonUtil;
import com.iot.exception.BusinessExceptionEnum;
import com.iot.file.api.LocalFileApi;
import com.iot.file.service.LocalFileService;

import java.util.List;
import java.util.Map;

/**
 * 文件服务
 * 本地文件上传实现
 * @author fenglijian
 *
 */
@RestController
public class LocalFileController implements LocalFileApi {
	@Autowired
	private LocalFileService localFileService;
	
	@Override
	public String uploadFile(@RequestPart(value = "file") MultipartFile multipartFile, @RequestParam("tenantId") Long tenantId, @RequestParam("path") String path){
		String filePath = localFileService.uploadFile(multipartFile, tenantId, path);
		return filePath;
	}

	@Override
	public List<String[]> resolveExcel(@RequestPart(value = "file")MultipartFile file) {
          return localFileService.resolveExcel(file);
	}
}
