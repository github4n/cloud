package com.iot.file.api;

import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;


/**
 * 
 * 项目名称：IOT云平台
 * 模块名称：文件服务
 * 功能描述：本地文件api接口
 * 创建人： fenglijian
 * 创建时间：2018年7月6日 下午3:18:30
 */
@Api(tags = "本地file-service接口")
@FeignClient(value = "file-service",configuration = LocalFileApi.MultipartSupportConfig.class)
public interface LocalFileApi {
	
	@ApiOperation(value="文件上传", notes="文件上传")
	@ApiImplicitParams({
	@ApiImplicitParam(name = "file", value = "文件", required = true, dataType = "File", paramType = "query"),
	@ApiImplicitParam(name = "tenantId", value = "租户id",paramType = "query", required = true,dataType = "String"),
	@ApiImplicitParam(name = "path", value = "文件路径",paramType = "query", required = true,dataType = "String")})
	@RequestMapping(value = "/file/service/uploadFile", method = {RequestMethod.POST},produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String uploadFile(@RequestPart(value = "file") MultipartFile multipartFile, @RequestParam("tenantId") Long tenantId, @RequestParam("path") String path);

	@ApiOperation(value="解析excel", notes="解析excel")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "file", value = "文件", required = true, dataType = "File", paramType = "query")})
	@RequestMapping(value = "/resolveExcel", method = {RequestMethod.POST},produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	List<String[]> resolveExcel(@RequestPart(value="file") MultipartFile file);

	/**
	  * @despriction：feign文件上传配置
	  * @author  yeshiyuan
	  * @created 2018/5/8 14:25
	  * @return
	  */
	class MultipartSupportConfig {
		@Bean
		public Encoder feignFormEncoder() {
			return new SpringFormEncoder();
		}
	}

}
