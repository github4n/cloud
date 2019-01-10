package com.iot.message.api;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.cloud.netflix.feign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageInfo;
import com.iot.message.dto.MailBatchDto;
import com.iot.message.dto.SmsBatchDto;
import com.iot.message.dto.SystemBatchDto;
import com.iot.message.dto.SystemPushControlDto;
import com.iot.message.dto.TenantMailInfoDto;
import com.iot.message.entity.AppCertInfo;
import com.iot.message.entity.PushNoticeLog;
import com.iot.message.entity.PushNoticeTemplate;
import com.iot.message.entity.SystemPushControl;
import com.iot.message.entity.TenantMailInfo;
import com.iot.message.entity.UserPhoneRelate;

import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * 项目名称：IOT云平台
 * 模块名称：message服务api
 * 功能描述：message服务api
 * 创建人： 李帅
 * 创建时间：2018年3月12日 下午3:35:07
 * 修改人：李帅
 * 修改时间：2018年3月12日 下午3:35:07
 */

@Api(tags = "message service接口")
@FeignClient(value = "message-service",configuration = MessageApi.MultipartSupportConfig.class)
public interface MessageApi {

	/**
	  * @despriction：feign文件上传配置
	  * @author  yeshiyuan
	  * @created 2018/5/8 14:25
	  * @return
	  */
          
	class MultipartSupportConfig {
	    @Autowired  
	    private ObjectFactory<HttpMessageConverters> messageConverters;  
		@Bean
		public Encoder feignFormEncoder() {
			return new SpringFormEncoder(new SpringEncoder(messageConverters));
		}
	}
	
	/**
	 * 
	 * 描述：推送日志查询
	 * 
	 * @author 李帅
	 * @created 2018年3月12日 下午3:29:51
	 * @since
	 * @param pageNum
	 * @param pageSize
	 * @param pushNoticeLog
	 * @return
	 */
	@ApiOperation(value="推送日志查询", notes="推送日志查询")
    @ApiImplicitParams({@ApiImplicitParam(name = "pageNum", value = "页码", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页数目", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pushNoticeLog", value = "消息推送日志实体", required = true, dataType = "PushNoticeLog") })
	@RequestMapping(value = "/api/message/getPushNoticeLog", method = { RequestMethod.POST },consumes = MediaType.APPLICATION_JSON_VALUE)
	PageInfo<PushNoticeLog> getPushNoticeLog(@RequestParam("pageNum") Integer pageNum,
			@RequestParam("pageSize") Integer pageSize, @RequestBody PushNoticeLog pushNoticeLog);

	/**
	 * 
	 * 描述：推送模板的添加/更新功能
	 * 
	 * @author 李帅
	 * @created 2018年3月12日 下午3:31:50
	 * @since
	 * @param pushNoticeLog
	 * @return
	 */
	@ApiOperation(value="推送模板的添加/更新功能", notes="推送模板的添加/更新功能")
    @ApiImplicitParams({@ApiImplicitParam(name = "pushNoticeTemplate", value = "消息推送模板实体", required = true, dataType = "PushNoticeTemplate") })
	@RequestMapping(value = "/api/message/addPushNoticeTemplate", method = { RequestMethod.POST },consumes = MediaType.APPLICATION_JSON_VALUE)
	void addPushNoticeTemplate(@RequestBody PushNoticeTemplate pushNoticeTemplate);

	/**
	 * 
	 * 描述：推送模板查询功能
	 * 
	 * @author 李帅
	 * @created 2018年3月12日 下午3:29:51
	 * @since
	 * @param pageNum
	 * @param pageSize
	 * @param pushNoticeTemplate
	 * @return
	 */
	@ApiOperation(value="推送模板查询功能", notes="推送模板查询功能")
    @ApiImplicitParams({@ApiImplicitParam(name = "pageNum", value = "页码", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页数目", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pushNoticeTemplate", value = "消息推送模板实体", required = true, dataType = "PushNoticeTemplate") })
	@RequestMapping(value = "/api/message/getPushNoticeTemplate", method = { RequestMethod.POST },consumes = MediaType.APPLICATION_JSON_VALUE)
	PageInfo<PushNoticeTemplate> getPushNoticeTemplate(@RequestParam("pageNum") Integer pageNum,
			@RequestParam("pageSize") Integer pageSize, @RequestBody PushNoticeTemplate pushNoticeTemplate);

	/**
	 * 
	 * 描述：邮件服务-平台级单个推送
	 * 
	 * @author 李帅
	 * @created 2018年3月12日 下午2:00:56
	 * @since
	 * @param to
	 * @param noticeMap
	 * @param retryNum
	 * @return
	 */
	@ApiOperation(value="邮件服务-平台级单个推送", notes="邮件服务-平台级单个推送")
    @ApiImplicitParams({@ApiImplicitParam(name = "appId", value = "AppID", required = true, paramType = "query", dataType = "Long"),
    		@ApiImplicitParam(name = "to", value = "收件方邮箱", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "noticeMap", value = "邮件内容", required = true, dataType = "Map<String, String>"),
            @ApiImplicitParam(name = "retryNum", value = "重试次数", required = true, paramType = "query", dataType = "int"),
    		@ApiImplicitParam(name = "langage", value = "收件方邮箱", required = true, paramType = "query", dataType = "String") })
	@RequestMapping(value = "/api/message/mailSinglePush", method = { RequestMethod.POST },consumes = MediaType.APPLICATION_JSON_VALUE)
	boolean mailSinglePush(@RequestParam("appId") Long appId, @RequestParam("to") String to, @RequestBody Map<String, String> noticeMap,
			@RequestParam("retryNum") Integer retryNum, @RequestParam("langage") String langage);

	/**
	 * 
	 * 描述：邮件服务-平台级单个附件邮件推送
	 * 
	 * @author 李帅
	 * @created 2018年3月12日 下午2:00:56
	 * @since
	 * @param to
	 * @param noticeMap
	 * @param retryNum
	 * @return
	 */
	@ApiOperation(value="邮件服务-平台级单个附件邮件推送", notes="邮件服务-平台级单个附件邮件推送")
    @ApiImplicitParams({
	    	@ApiImplicitParam(name = "files",value = "文件",required = false,dataType = "File",paramType = "query"),
	    	@ApiImplicitParam(name = "appId", value = "AppID", required = true, paramType = "query", dataType = "Long"),
    		@ApiImplicitParam(name = "message", value = "验证码", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "retryNum", value = "重试次数", required = true, paramType = "query", dataType = "int"),
    		@ApiImplicitParam(name = "langage", value = "收件方邮箱", required = true, paramType = "query", dataType = "String") })
	@RequestMapping(value = "/api/message/annexMailSinglePush", method = { RequestMethod.POST }, produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	boolean annexMailSinglePush(@RequestPart(value = "files") MultipartFile[] files, @RequestParam("appId") Long appId, @RequestParam("message") String message, 
    		@RequestParam("retryNum") Integer retryNum, @RequestParam("langage") String langage);
	
	/**
	 * 
	 * 描述：邮件服务-测试邮件推送配置
	 * @author 李帅
	 * @created 2018年12月10日 下午6:43:23
	 * @since 
	 * @param host
	 * @param port
	 * @param userName
	 * @param passWord
	 * @param message
	 * @return
	 */
	@ApiOperation(value="邮件服务-测试邮件推送配置", notes="邮件服务-测试邮件推送配置")
    @ApiImplicitParams({
	    	@ApiImplicitParam(name = "host",value = "主机IP",required = true, paramType = "query", dataType = "String"),
	    	@ApiImplicitParam(name = "port", value = "端口号", required = true, paramType = "query", dataType = "Integer"),
    		@ApiImplicitParam(name = "userName", value = "用户名", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "passWord", value = "密码", required = true, paramType = "query", dataType = "String"),
    		@ApiImplicitParam(name = "message", value = "收件内容", required = true, paramType = "query", dataType = "String") })
	@RequestMapping(value = "/api/message/testMailPushConfig", method = { RequestMethod.GET })
	boolean testMailPushConfig(@RequestParam("host") String host, @RequestParam("port") Integer port, @RequestParam("userName") String userName, 
    		@RequestParam("passWord") String passWord, @RequestParam("message") String message);
	
	/**
	 * 
	 * 描述：邮件服务-平台级批量推送
	 * 
	 * @author 李帅
	 * @created 2018年3月12日 下午2:01:06
	 * @since
	 * @param tos
	 * @param noticeMap
	 * @param retryNum
	 * @return
	 */
	@ApiOperation(value="邮件服务-平台级批量推送", notes="邮件服务-平台级批量推送")
    @ApiImplicitParams({@ApiImplicitParam(name = "mailBatchDto", value = "邮件内容", required = true, dataType = "MailBatchDto")})
	@RequestMapping(value = "/api/message/mailBatchPush", method = { RequestMethod.POST },consumes = MediaType.APPLICATION_JSON_VALUE)
	boolean mailBatchPush(@RequestBody MailBatchDto mailBatchDto);

	/**
	 * 
	 * 描述：系统消息单个推送
	 * 
	 * @author 李帅
	 * @created 2018年3月12日 下午2:46:56
	 * @since
	 * @param userId
	 * @param noticeMap
	 * @param retryNum
	 * @return
	 */
	@ApiOperation(value="系统消息单个推送", notes="系统消息单个推送")
    @ApiImplicitParams({@ApiImplicitParam(name = "userId", value = "用户ID", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "noticeMap", value = "系统消息内容", required = true, dataType = "Map<String, String>"),
            @ApiImplicitParam(name = "retryNum", value = "重试次数", required = true, paramType = "query", dataType = "int")})
	@RequestMapping(value = "/api/message/systemSinglePush", method = { RequestMethod.POST },consumes = MediaType.APPLICATION_JSON_VALUE)
	boolean systemSinglePush(@RequestParam("userId") String userId, @RequestBody Map<String, String> noticeMap,
			@RequestParam("retryNum") Integer retryNum);

	/**
	 * 
	 * 描述：系统消息批量推送
	 * 
	 * @author 李帅
	 * @created 2018年3月12日 下午2:46:43
	 * @since
	 * @param userIds
	 * @param noticeMap
	 * @param retryNum
	 * @return
	 */
	@ApiOperation(value="系统消息批量推送", notes="系统消息批量推送")
    @ApiImplicitParams({@ApiImplicitParam(name = "systemBatchDto", value = "系统消息内容", required = true, dataType = "SystemBatchDto") })
	@RequestMapping(value = "/api/message/systemBatchPush", method = { RequestMethod.POST },consumes = MediaType.APPLICATION_JSON_VALUE)
	boolean systemBatchPush(@RequestBody SystemBatchDto systemBatchDto);

	/**
	 * 
	 * 描述：新增或更新用户和手机端关系
	 * @author 李帅
	 * @created 2018年3月12日 下午7:22:30
	 * @since 
	 * @param userPhoneRelate
	 */
	@ApiOperation(value="新增或更新用户和手机端关系", notes="新增或更新用户和手机端关系")
    @ApiImplicitParams({@ApiImplicitParam(name = "userPhoneRelate", value = "用户和手机端对应关系实体", required = true, dataType = "UserPhoneRelate") })
	@RequestMapping(value = "/api/message/addUserPhoneRelate", method = { RequestMethod.POST },consumes = MediaType.APPLICATION_JSON_VALUE)
	void addUserPhoneRelate(@RequestBody UserPhoneRelate userPhoneRelate);
	
	/**
	 * 
	 * 描述：短信消息单个推送
	 * 
	 * @author 李帅
	 * @created 2018年3月12日 下午2:46:56
	 * @since
	 * @param userId
	 * @param noticeMap
	 * @param retryNum
	 * @return
	 */
	@ApiOperation(value="短信消息单个推送", notes="短信消息单个推送")
    @ApiImplicitParams({@ApiImplicitParam(name = "userPhoneNo", value = "收信方手机号", required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "noticeMap", value = "短信消息内容", required = true, dataType = "Map<String, String>"),
        @ApiImplicitParam(name = "retryNum", value = "重试次数", required = true, paramType = "query", dataType = "int")})
	@RequestMapping(value = "/api/message/smsSinglePush", method = { RequestMethod.POST },consumes = MediaType.APPLICATION_JSON_VALUE)
	boolean smsSinglePush(@RequestParam("userPhoneNo") String userPhoneNo, @RequestBody Map<String, String> noticeMap,
			@RequestParam("retryNum") Integer retryNum);

	/**
	 * 
	 * 描述：短信消息批量推送
	 * 
	 * @author 李帅
	 * @created 2018年3月12日 下午2:46:43
	 * @since
	 * @param userIds
	 * @param noticeMap
	 * @param retryNum
	 * @return
	 */
	@ApiOperation(value="短信消息批量推送", notes="短信消息批量推送")
    @ApiImplicitParams({@ApiImplicitParam(name = "smsBatchDto", value = "短信消息内容", required = true, dataType = "SmsBatchDto")})
	@RequestMapping(value = "/api/message/smsBatchPush", method = { RequestMethod.POST },consumes = MediaType.APPLICATION_JSON_VALUE)
	boolean smsBatchPush(@RequestBody SmsBatchDto smsBatchDto);

	/**
	  * @despriction：删除用户和手机端关系
	  * @author  yeshiyuan
	  * @created 2018/5/29 11:50
	  * @param userPhoneRelate
	  * @return
	  */
	@ApiOperation(value="删除用户和手机端关系", notes="删除用户和手机端关系")
	@ApiImplicitParam(name = "userUuid", value = "用户uuid", required = true, paramType = "query", dataType = "string")
	@RequestMapping(value = "/api/message/service/system/delUserPhoneRelate", method = { RequestMethod.GET })
	void delUserPhoneRelate(@RequestParam("userUuid") String userUuid);
	
	/**
	 * 
	 * 描述：删除用户手机ID
	 * @author 李帅
	 * @created 2018年12月5日 下午5:43:14
	 * @since 
	 * @param userUuid
	 */
	@ApiOperation(value="删除用户手机ID", notes="删除用户手机ID")
	@ApiImplicitParam(name = "userUuid", value = "用户uuid", required = true, paramType = "query", dataType = "string")
	@RequestMapping(value = "/api/message/service/system/deleteUserPhoneId", method = { RequestMethod.GET })
	void deleteUserPhoneId(@RequestParam("userUuid") String userUuid);
	
	/**
	 * 
	 * 描述：查询用户和手机端关系
	 * @author 李帅
	 * @created 2018年12月6日 上午9:54:09
	 * @since 
	 * @param userIds
	 * @return
	 */
	@ApiOperation(value="查询用户和手机端关系", notes="查询用户和手机端关系")
	@RequestMapping(value = "/api/message/service/system/getUserPhoneRelates", method = { RequestMethod.POST },consumes = MediaType.APPLICATION_JSON_VALUE)
	List<UserPhoneRelate> getUserPhoneRelates(@RequestBody List<String> userIds);
	
	/**
	 * 
	 * 描述：查询APP证书信息
	 * @author 李帅
	 * @created 2018年7月24日 下午7:03:49
	 * @since 
	 * @param appId
	 * @return
	 */
	@ApiOperation(value="查询APP证书信息", notes="查询APP证书信息")
	@ApiImplicitParam(name = "appId", value = "APP应用id", required = true, paramType = "query", dataType = "Long")
	@RequestMapping(value = "/api/message/getAppCertInfo", method = { RequestMethod.GET })
	AppCertInfo getAppCertInfo(@RequestParam("appId") Long appId);
	
	/**
	 * 
	 * 描述：添加或修改APP证书信息
	 * @author 李帅
	 * @created 2018年7月24日 下午8:12:23
	 * @since 
	 * @param appCertInfoDto
	 */
	@ApiOperation(value="添加或修改APP证书信息", notes="添加或修改APP证书信息")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "file",value = "文件",required = true,dataType = "File",paramType = "query"),
    	@ApiImplicitParam(name = "tenantId", value = "租户id", required = true, paramType = "query", dataType = "Long"),
    	@ApiImplicitParam(name = "appId", value = "APP应用id", required = true, paramType = "query", dataType = "Long"),
        @ApiImplicitParam(name = "certPassWord", value = "证书密码", required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "testOrReleaseMark", value = "版本标识", required = true, paramType = "query", dataType = "String")
    })
	@RequestMapping(value = "/api/message/addAppCertInfo", method = { RequestMethod.POST }, produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	void addAppCertInfo(@RequestPart(value = "file") MultipartFile file, @RequestParam("tenantId") Long tenantId, 
			@RequestParam("appId") Long appId, @RequestParam("certPassWord") String certPassWord, @RequestParam("testOrReleaseMark") String testOrReleaseMark,
			@RequestParam("androidPushUrl") String androidPushUrl, @RequestParam("androidPushKey") String androidPushKey);
	
	/**
	 * 
	 * 描述：查询租户邮箱信息
	 * @author 李帅
	 * @created 2018年7月24日 下午7:09:32
	 * @since 
	 * @param tenantId
	 * @return
	 */
	@ApiOperation(value="查询租户邮箱信息", notes="查询租户邮箱信息")
	@ApiImplicitParam(name = "appId", value = "AppId", required = true, paramType = "query", dataType = "Long")
	@RequestMapping(value = "/api/message/getTenantMailInfo", method = { RequestMethod.GET })
	TenantMailInfo getTenantMailInfo(@RequestParam("appId") Long appId);
	
	/**
	 * 
	 * 描述：添加或修改租户邮箱信息
	 * @author 李帅
	 * @created 2018年7月24日 下午8:12:31
	 * @since 
	 * @param tenantMailInfoDto
	 */
	@ApiOperation(value="添加或修改租户邮箱信息", notes="添加或修改租户邮箱信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "tenantMailInfoDto", value = "租户邮箱信息", required = true, dataType = "TenantMailInfoDto") })
	@RequestMapping(value = "/api/message/addTenantMailInfo", method = { RequestMethod.POST },consumes = MediaType.APPLICATION_JSON_VALUE)
	void addTenantMailInfo(@RequestBody TenantMailInfoDto tenantMailInfoDto);
	
	/**
	 * 
	 * 描述：清除手机未读记录
	 * @author 李帅
	 * @created 2018年8月1日 下午1:47:54
	 * @since 
	 * @param phoneId
	 * @return
	 */
	@ApiOperation(value="清除手机未读记录", notes="清除手机未读记录")
	@ApiImplicitParam(name = "phoneId", value = "手机id", required = true, paramType = "query", dataType = "String")
	@RequestMapping(value = "/api/message/clearUnReadRecord", method = { RequestMethod.GET })
	void clearUnReadRecord(@RequestParam("phoneId") String phoneId);
	
	/**
	 * 
	 * 描述：清除所有缓存
	 * @author 李帅
	 * @created 2018年9月7日 下午3:32:56
	 * @since
	 */
	@ApiOperation(value="清除所有缓存", notes="清除所有缓存")
	@RequestMapping(value = "/api/message/clearAllCache", method = { RequestMethod.GET })
	void clearAllCache();
	
	/**
	 * 
	 * 描述：查询系统推送控制
	 * @author 李帅
	 * @created 2018年11月16日 下午4:41:37
	 * @since 
	 * @param userId
	 * @param appId
	 * @return
	 */
	@ApiOperation(value="查询系统推送控制", notes="查询系统推送控制")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "appId", value = "APP应用id", required = true, paramType = "query", dataType = "Long"),
    	@ApiImplicitParam(name = "userId", value = "用户id", required = true, paramType = "query", dataType = "String")
    })
	@RequestMapping(value = "/api/message/getSystemPushControl", method = { RequestMethod.GET })
	SystemPushControl getSystemPushControl(@RequestParam("userId") String userId, @RequestParam("appId") Long appId);
	
	/**
	 * 
	 * 描述：添加或修改系统推送控制
	 * @author 李帅
	 * @created 2018年11月16日 下午4:08:32
	 * @since 
	 * @param systemPushControlDto
	 */
	@ApiOperation(value="添加或修改系统推送控制", notes="添加或修改系统推送控制")
    @ApiImplicitParams({@ApiImplicitParam(name = "systemPushControlDto", value = "租户邮箱信息", required = true, dataType = "SystemPushControlDto") })
	@RequestMapping(value = "/api/message/addSystemPushControl", method = { RequestMethod.POST },consumes = MediaType.APPLICATION_JSON_VALUE)
	void addSystemPushControl(@RequestBody SystemPushControlDto systemPushControlDto);
}
