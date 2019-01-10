package com.iot.file.api;

import com.iot.file.dto.FileDto;
import com.iot.file.entity.FileBean;
import com.iot.file.vo.FileInfoResp;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.cloud.netflix.feign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;


/**
 * 
 * 项目名称：IOT云平台
 * 模块名称：文件服务
 * 功能描述：文件api接口
 * 创建人： zhouzongwei
 * 创建时间：2018年3月9日 下午3:18:30
 * 修改人： zhouzongwei
 * 修改时间：2018年3月9日 下午3:18:30
 */
@Api(tags = "file-service接口")
@FeignClient(value = "file-service",configuration = FileApi.MultipartSupportConfig.class)
public interface FileApi {
   
	/**
	 * 
	 * 描述：获取单个预签名Put 类型的Url
	 * @author zhouzongwei
	 * @created 2018年3月9日 下午3:07:27
	 * @since 
	 * @param tenantId:租户id
	 * @param fileType：文件类型
	 * @return
	 */
	@ApiOperation(value="获取单个预签名Put类型的Url", notes="获取单个预签名Put类型的Url")
	@ApiImplicitParams({@ApiImplicitParam(name = "tenantId", value = "租户id",paramType = "query", required = true,dataType = "String"),
        @ApiImplicitParam(name = "fileType", value = "文件类型", required = true, paramType = "query", dataType = "String") })
    @RequestMapping(value = "/api/file/service/getPutUrl", method = {RequestMethod.POST})
	FileDto getPutUrl(@RequestParam("tenantId") Long tenantId,@RequestParam("fileType") String fileType);
    
    /**
     * 
     * 描述：批量获取预签名Put 类型的Url
     * @author zhouzongwei
     * @created 2018年3月9日 下午3:07:58
     * @since 
     * @param tenantId:租户id
	 * @param fileType：文件类型
     * @param urlNum：批量个数
     * @return
     */
	@ApiOperation(value="批量获取预签名Put 类型的Url", notes="批量获取预签名Put 类型的Url")
	@ApiImplicitParams({@ApiImplicitParam(name = "tenantId", value = "租户id",paramType = "query", required = true,dataType = "String"),
        @ApiImplicitParam(name = "fileType", value = "文件类型", required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "urlNum", value = "批量个数", required = true, paramType = "query", dataType = "Integer")})
    @RequestMapping(value = "/api/file/service/getBatchPutUrl", method = {RequestMethod.POST})
    List<FileDto> getPutUrl(@RequestParam("tenantId") Long tenantId,@RequestParam("fileType") String fileType,@RequestParam("urlNum") Integer urlNum);
    
    /**
     * 
     * 描述：获取单个预签名Get 类型的Url
     * @author zhouzongwei
     * @created 2018年3月9日 下午3:09:04
     * @since 
     * @param fileId:文件id
     * @return
     */
	@ApiOperation(value="获取单个预签名Get 类型的Url", notes="获取单个预签名Get 类型的Url")
	@ApiImplicitParam(name = "fileId", value = "文件路径", required = true, dataType = "String", paramType = "query")
    @RequestMapping(value = "/api/file/service/getGetUrl", method = {RequestMethod.POST})
	FileDto getGetUrl(@RequestParam("fileId") String fileId);
    
    /**
     * 
     * 描述：批量获取预签名Get 类型的Url
     * @author zhouzongwei
     * @created 2018年3月9日 下午3:09:59
     * @since 
     * @param fileIds：文件id集合
     * @return
     */
	@ApiOperation(value="批量获取预签名Get 类型的Url", notes="批量获取预签名Get 类型的Url")
	@ApiImplicitParam(name = "fileIds", value = "文件id集合", required = true, dataType = "List", paramType = "query")
    @RequestMapping(value = "/api/file/service/getBatchGetUrl", method = {RequestMethod.POST})
	Map<String, String> getGetUrl(@RequestParam("fileIds")List<String> fileIds);
    
    /**
     * 
     * 描述：删除单个对象
     * @author zhouzongwei
     * @created 2018年3月9日 下午3:11:09
     * @since 
     * @param fileId：文件id
     */
	@ApiOperation(value="删除单个对象", notes="删除单个对象")
	@ApiImplicitParam(name = "fileId", value = "文件id", required = true, dataType = "String", paramType = "query")
    @RequestMapping(value = "/api/file/service/deleteObject", method = {RequestMethod.POST})
    int deleteObject(@RequestParam("fileId")String fileId);

    /**
     * 
     * 描述：批量删除对象
     * @author zhouzongwei
     * @created 2018年3月9日 下午3:16:38
     * @since 
     * @param fileIds：文件id集合
     */
	@ApiOperation(value="批量删除对象", notes="批量删除对象")
	@ApiImplicitParam(name = "fileIds", value = "文件id集合", required = true, dataType = "List", paramType = "query")
    @RequestMapping(value = "/api/file/service/deleteBatchObject", method = {RequestMethod.POST})
    int deleteObject(@RequestParam("fileIds")List<String> fileIds);

	/**
	 * 描述：根据路径删除文件 用于删除数据库没有存储数据的的文件 不删除数据库数据
	 * @author 490485964@qq.com
	 * @date 2018/5/25 14:01
	 * @param  path 文件在云上的路径
	 * @return
	 */
	@ApiOperation(value="根据路径删除单个对象", notes="根据路径删除单个对象")
	@ApiImplicitParam(name = "path", value = "路径", required = true, dataType = "String", paramType = "query")
	@RequestMapping(value = "/api/file/service/deleteObjectByPath", method = {RequestMethod.POST})
	int deleteObjectByPath(@RequestParam("path")String path);

	/**
	 * 描述：批量根据路径删除文件 用于删除数据库没有存储数据的的文件 不删除数据库数据
	 * @author 490485964@qq.com
	 * @date 2018/5/25 14:01
	 * @param pathList 文件在云上的路径集合
	 * @return
	 */
	@ApiOperation(value="根据路径批量删除对象", notes="根据路径批量删除对象")
	@ApiImplicitParam(name = "pathList", value = "文件路径集合", required = true, dataType = "List", paramType = "query")
	@RequestMapping(value = "/api/file/service/deleteBatchObjectByPath", method = {RequestMethod.POST})
	int deleteObjectByPath(@RequestParam("pathList")List<String> pathList);

	/**
	 * 描述：文件上传
	 * @author 490485964@qq.com
	 * @date 2018/4/11 10:17
	 * @param
	 * @return
	 */
	@Deprecated
	@ApiOperation(value="文件上传", notes="文件上传")
	@ApiImplicitParams({
	@ApiImplicitParam(name = "file", value = "文件", required = true, dataType = "File", paramType = "query"),
	@ApiImplicitParam(name = "tenantId", value = "租户id",paramType = "query", required = true,dataType = "String")})
	@RequestMapping(value = "/api/file/service/upLoadFile", method = {RequestMethod.POST},produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	String upLoadFile(@RequestPart(value = "file") MultipartFile file, @RequestParam("tenantId")Long tenantId);

	/**
	  * @despriction：文件修改覆盖
	  * @author  yeshiyuan
	  * @created 2018/4/24 10:29
	  * @param file 文件
	  * @param fileId 文件uuid
	  * @param filePath 文件路径
	  * @return
	  */
	@ApiOperation(value = "修改文件",notes = "修改文件")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "file",value = "文件",required = true,dataType = "File",paramType = "query"),
			@ApiImplicitParam(name = "fileId",value = "文件uuid",required = true,dataType = "String",paramType = "query"),
			@ApiImplicitParam(name = "filePath",value = "文件路径（可不传）",dataType = "String",paramType = "query")
	})
	@RequestMapping(value = "/api/file/service/updateFile",method = RequestMethod.POST,produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	void updateFile(@RequestPart(value = "file") MultipartFile file,@RequestParam("fileId") String fileId,@RequestParam(value = "filePath",required = false) String filePath);


	/**
	 * @despriction：通过文件uuid获取文件信息
	 * @author  yeshiyuan
	 * @created 2018/4/24 10:54
	 * @param fileId 文件uuid
	 * @return
	 */
	@ApiOperation(value = "通过文件uuid获取文件信息",notes = "通过文件uuid获取文件信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "fileId",value = "文件uuid",required = true,dataType = "String",paramType = "query")
	})
	@RequestMapping(value = "/api/file/service/getFileInfoByFileId",method = RequestMethod.POST)
	FileBean getFileInfoByFileId(@RequestParam("fileId") String fileId);

	/**
	  * @despriction：从redis中获取fileId进而删除s3和数据库的数据
	  * @author  yeshiyuan
	  * @created 2018/6/5 14:40
	  * @param redisTaskId
	  * @return
	  */
	@ApiOperation(value = "从redis中获取fileId进而删除s3和数据库的数据",notes = "从redis中获取fileId进而删除s3和数据库的数据")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "redisTaskId",value = "redis中待删除文件的taskId",required = true,dataType = "String",paramType = "query")
	})
	@RequestMapping(value = "/api/file/service/deleteFileByRedisTaskId",method = RequestMethod.POST)
	void deleteFileByRedisTaskId(@RequestParam("redisTaskId") String redisTaskId);

	/**
	 * @despriction：文件上传并获取到文件的MD5
	 * @author  yeshiyuan
	 * @created 2018/8/3 20:03
	 * @param null
	 * @return
	 */
	@Deprecated
	@ApiOperation(value="文件上传并获取到文件id、MD5", notes="文件上传并获取到文件id、MD5")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "file", value = "文件", required = true, dataType = "File", paramType = "query"),
			@ApiImplicitParam(name = "tenantId", value = "租户id",paramType = "query", required = true,dataType = "String")})
	@RequestMapping(value = "/api/file/service/upLoadFileAndGetMd5", method = {RequestMethod.POST},produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	FileInfoResp upLoadFileAndGetMd5(@RequestPart(value = "file") MultipartFile file, @RequestParam("tenantId")Long tenantId);

	/**
	  * @despriction：获取文件路径
	  * @author  yeshiyuan
	  * @created 2018/8/21 14:38
	  * @param null
	  * @return
	  */
	@ApiOperation(value="获取文件路径", notes="获取文件路径")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "fileIds", value = "文件id集合", required = true, paramType = "query")
	})
	@RequestMapping(value = "/api/file/service/batchGetFilePath", method = {RequestMethod.GET})
	Map<String, String> batchGetFilePath(@RequestParam("fileIds") List<String> fileIds);

	/**
	  * @despriction：文件上传并返回文件id、访问url
	  * @author  yeshiyuan
	  * @created 2018/8/30 15:32
	  * @param null
	  * @return
	  */
	@Deprecated
	@ApiOperation(value="文件上传并返回文件id、访问url", notes="文件上传并返回文件id、访问url")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "file", value = "文件", required = true, dataType = "File", paramType = "query"),
			@ApiImplicitParam(name = "tenantId", value = "租户id",paramType = "query", required = true,dataType = "String")
	})
	@RequestMapping(value = "/api/file/service/upLoadFileAndGetUrl", method = {RequestMethod.POST},produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	FileInfoResp upLoadFileAndGetUrl(@RequestPart(value = "file") MultipartFile file,@RequestParam(value = "tenantId") Long tenantId);

	/**
	  * @despriction：把redis中的文件信息保存至数据库
	  * @author  yeshiyuan
	  * @created 2018/10/9 19:10
	  * @return
	  */
	@ApiOperation(value = "把redis中的文件信息保存至数据库", notes = "把redis中的文件信息保存至数据库")
	@ApiImplicitParam(name = "fileId", value = "文件uuid", dataType = "String", paramType = "query")
	@RequestMapping(value = "/saveFileInfoToDb", method = RequestMethod.POST)
	void saveFileInfoToDb(@RequestParam("fileId") String fileId);

	/**
	 * @despriction：批量把redis中的文件信息保存至数据库
	 * @author  yeshiyuan
	 * @created 2018/10/9 19:10
	 * @return
	 */
	@ApiOperation(value = "批量把redis中的文件信息保存至数据库", notes = "批量把redis中的文件信息保存至数据库")
	@ApiImplicitParam(name = "fileIds", value = "文件uuid集合", dataType = "List", paramType = "query")
	@RequestMapping(value = "/saveFileInfosToDb", method = RequestMethod.POST)
	void saveFileInfosToDb(@RequestParam("fileIds") List<String> fileIds);



	@ApiOperation(value="获取对象md5", notes="获取对象md5")
	@ApiImplicitParam(name = "fileId", value = "文件id", required = true, dataType = "String", paramType = "query")
	@RequestMapping(value = "/api/file/service/getObjectContentMD5", method = {RequestMethod.POST})
	String getObjectContentMD5(@RequestParam("fileId") String fileId);

	/**
	  * @despriction：获取下载验证码（有效期单位秒）
	  * @author  yeshiyuan
	  * @created 2018/11/28 18:01
	  */
	@ApiOperation(value = "获取下载验证码（有效期单位秒）", notes = "获取下载验证码（有效期单位秒）")
	@RequestMapping(value = "/getDownloadCode", method = RequestMethod.GET)
	String getDownloadCode(@RequestParam("second") Integer second);

	/**
	 * @despriction：检验下载验证码是否过期
	 * @author  yeshiyuan
	 * @created 2018/11/28 19:11
	 */
	@ApiOperation(value = "检验下载验证码是否过期", notes = "检验下载验证码是否过期")
	@RequestMapping(value = "/checkDownloadCode", method = RequestMethod.GET)
	boolean checkDownloadCode(@RequestParam("uuid") String uuid);

	/**
	  * @despriction：feign文件上传配置
	  * @author  yeshiyuan
	  * @created 2018/5/8 14:25
	  * @return
	  */
	class MultipartSupportConfig {
		@Bean
		public Encoder feignFormEncoder() {
			return new SpringFormEncoder(new SpringEncoder(new ObjectFactory<HttpMessageConverters>() {
				@Override
				public HttpMessageConverters getObject() throws BeansException {
					return new HttpMessageConverters(new RestTemplate().getMessageConverters());
				}
			}));
		}
	}
}
