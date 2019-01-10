package com.iot.message.restful;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageInfo;
import com.iot.message.api.MessageApi;
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
import com.iot.message.manager.MailManager;
import com.iot.message.manager.MessageManager;
import com.iot.message.manager.SmsManager;
import com.iot.message.manager.SystemManager;


@RestController
public class MessageRestful implements MessageApi {

    @Autowired
    private MessageManager messageManager;

    @Autowired
    private MailManager mailManager;
    
    @Autowired
    private SystemManager systemManager;
    
    @Autowired
    private SmsManager smsManager;
    
    
    /**
     * 
     * 描述：推送日志查询
     * @author 李帅
     * @created 2018年3月12日 下午3:29:43
     * @since 
     * @param pageNum
     * @param pageSize
     * @param pushNoticeLog
     * @return
     */
    @Override
    public PageInfo<PushNoticeLog> getPushNoticeLog(Integer pageNum, Integer pageSize, @RequestBody PushNoticeLog pushNoticeLog) {
        return messageManager.getPushNoticeLog(pageNum, pageSize, pushNoticeLog);
    }
    
    /**
     * 
     * 描述：推送模板的添加/更新功能
     * @author 李帅
     * @created 2018年3月12日 下午3:37:54
     * @since 
     * @param pushNoticeTemplate
     */
    @Override
    public void addPushNoticeTemplate(@RequestBody PushNoticeTemplate pushNoticeTemplate) {
    	messageManager.addPushNoticeTemplate(pushNoticeTemplate);
    }
    
    /**
     * 
     * 描述：推送模板查询功能
     * @author 李帅
     * @created 2018年3月12日 下午3:38:06
     * @since 
     * @param pageNum
     * @param pageSize
     * @param pushNoticeTemplate
     * @return
     */
    @Override
    public PageInfo<PushNoticeTemplate> getPushNoticeTemplate(Integer pageNum,Integer pageSize, @RequestBody PushNoticeTemplate pushNoticeTemplate) {
        return messageManager.getPushNoticeTemplate(pageNum, pageSize, pushNoticeTemplate);
    }
    
    /**
     * 
     * 描述：邮件服务-平台级单个推送
     * @author 李帅
     * @created 2018年3月12日 下午2:05:53
     * @since 
     * @param to
     * @param noticeMap
     * @param retryNum
     * @return
     */
    @Override
    public boolean mailSinglePush(Long appId, String to, @RequestBody Map<String, String> noticeMap,Integer retryNum, String langage) {
        return mailManager.mailSinglePush(appId, to, noticeMap, retryNum, langage);
    }
    
    /**
     * 
     * 描述：邮件服务-测试邮件推送配置
     * @author 李帅
     * @created 2018年12月10日 下午6:58:09
     * @since 
     * @param host
     * @param port
     * @param userName
     * @param passWord
     * @param message
     * @return
     */
    @Override
    public boolean testMailPushConfig(@RequestParam("host") String host, @RequestParam("port") Integer port, @RequestParam("userName") String userName, 
    		@RequestParam("passWord") String passWord, @RequestParam("message") String message) {
        return mailManager.testMailPushConfig(host, port, userName, passWord, message);
    }
    
    /**
     * 
     * 描述：邮件服务-平台级单个附件邮件推送
     * @author 李帅
     * @created 2018年11月12日 下午3:20:29
     * @since 
     * @param file
     * @param tenantId
     * @param to
     * @param subject
     * @param templateId
     * @param activateCode
     * @param retryNum
     * @param langage
     * @return
     */
    @Override
    public boolean annexMailSinglePush(@RequestPart(value = "files") MultipartFile[] files, @RequestParam("appId") Long appId, @RequestParam("message") String message, 
    		@RequestParam("retryNum") Integer retryNum, @RequestParam("langage") String langage) {
        return mailManager.annexMailSinglePush(files, appId, message, retryNum, langage);
    }
    
    /**
     * 
     * 描述：邮件服务-平台级批量推送
     * @author 李帅
     * @created 2018年3月12日 下午2:05:37
     * @since 
     * @param tos
     * @param noticeMap
     * @param retryNum
     * @return
     */
	@Override
    public boolean mailBatchPush(@RequestBody MailBatchDto mailBatchDto) {
        return mailManager.mailBatchPush(mailBatchDto.getAppId(), mailBatchDto.getTos(),
        		mailBatchDto.getNoticeMap(), mailBatchDto.getRetryNum(), mailBatchDto.getLangage());
    }
    
    /**
     * 
     * 描述：系统消息单个推送
     * @author 李帅
     * @created 2018年3月12日 下午2:45:46
     * @since 
     * @param userId
     * @param noticeMap
     * @param retryNum
     * @return
     */
    @Override
    public boolean systemSinglePush(String userId, @RequestBody Map<String, String> noticeMap,Integer retryNum) {
        return systemManager.systemSinglePush(userId, noticeMap, retryNum);
    }
    
    /**
     * 
     * 描述：系统消息批量推送
     * @author 李帅
     * @created 2018年3月12日 下午2:46:33
     * @since 
     * @param userIds
     * @param noticeMap
     * @param retryNum
     * @return
     */
    @Override
    public boolean systemBatchPush(@RequestBody SystemBatchDto systemBatchDto) {
        return systemManager.systemBatchPush(systemBatchDto.getUserIds(), systemBatchDto.getNoticeMap(), systemBatchDto.getRetryNum());
    }
    
    /**
     * 
     * 描述：新增或更新用户和手机端关系
     * @author 李帅
     * @created 2018年3月12日 下午7:22:43
     * @since 
     * @param userPhoneRelate
     */
    @Override
    public void addUserPhoneRelate(@RequestBody UserPhoneRelate userPhoneRelate) {
    	systemManager.addUserPhoneRelate(userPhoneRelate);
    }
    
    /**
     * 
     * 描述：短信消息单个推送
     * @author 李帅
     * @created 2018年3月12日 下午2:45:46
     * @since 
     * @param userId
     * @param noticeMap
     * @param retryNum
     * @return
     */
    @Override
    public boolean smsSinglePush(String userPhoneNo, @RequestBody Map<String, String> noticeMap,Integer retryNum) {
        return smsManager.smsSinglePush(userPhoneNo, noticeMap, retryNum);
    }
    
    /**
     * 
     * 描述：短信消息批量推送
     * @author 李帅
     * @created 2018年3月12日 下午2:46:33
     * @since 
     * @param userIds
     * @param noticeMap
     * @param retryNum
     * @return
     */
    @Override
    public boolean smsBatchPush(@RequestBody SmsBatchDto smsBatchDto) {
        return smsManager.smsBatchPush(smsBatchDto.getUserPhoneNos(), smsBatchDto.getNoticeMap(), smsBatchDto.getRetryNum());
    }

    /**
     * @despriction：删除用户和手机端关系
     * @author  yeshiyuan
     * @created 2018/5/29 11:50
     * @param userUuid
     * @return
     */
    @Override
    public void delUserPhoneRelate(@RequestParam("userUuid")  String userUuid) {
        systemManager.delUserPhoneRelate(userUuid);
    }
    
    /**
     * 
     * 描述：删除用户手机ID
     * @author 李帅
     * @created 2018年12月5日 下午5:43:36
     * @since 
     * @param userUuid
     */
    @Override
    public void deleteUserPhoneId(@RequestParam("userUuid")  String userUuid) {
        systemManager.deleteUserPhoneId(userUuid);
    }
    
    /**
     * 
     * 描述：查询用户和手机端关系
     * @author 李帅
     * @created 2018年12月6日 上午9:53:59
     * @since 
     * @param userIds
     * @return
     */
    @Override
    public List<UserPhoneRelate> getUserPhoneRelates(@RequestParam("userIds") List<String> userIds) {
        return systemManager.getUserPhoneRelates(userIds);
    }
    
    /**
     * 
     * 描述：查询APP证书信息
     * @author 李帅
     * @created 2018年7月24日 下午7:03:42
     * @since 
     * @param appId
     * @return
     */
    @Override
    public AppCertInfo getAppCertInfo(@RequestParam("appId") Long appId) {
        return systemManager.getAppCertInfo(appId);
    }
    
    /**
     * 
     * 描述：添加或修改APP证书信息
     * @author 李帅
     * @created 2018年7月24日 下午8:14:10
     * @since 
     * @param appCertInfoDto
     */
    @Override
    public void addAppCertInfo(@RequestPart(value = "file") MultipartFile file, @RequestParam("tenantId") Long tenantId, 
			@RequestParam("appId") Long appId, @RequestParam("certPassWord") String certPassWord, @RequestParam("testOrReleaseMark") String testOrReleaseMark,
			@RequestParam("androidPushUrl") String androidPushUrl, @RequestParam("androidPushKey") String androidPushKey) {
    	systemManager.addAppCertInfo(file, tenantId, appId, certPassWord, testOrReleaseMark, androidPushUrl, androidPushKey);
    }
    
    /**
     * 
     * 描述：查询租户邮箱信息
     * @author 李帅
     * @created 2018年7月24日 下午7:10:35
     * @since 
     * @param tenantId
     * @return
     */
    @Override
    public TenantMailInfo getTenantMailInfo(@RequestParam("appId") Long appId)  {
        return systemManager.getTenantMailInfo(appId);
    }
    
    /**
     * 
     * 描述：添加或修改租户邮箱信息
     * @author 李帅
     * @created 2018年7月24日 下午8:12:40
     * @since 
     * @param tenantMailInfoDto
     */
    @Override
    public void addTenantMailInfo(@RequestBody TenantMailInfoDto tenantMailInfoDto) {
    	systemManager.addTenantMailInfo(tenantMailInfoDto);
    }
    
    /**
     * 
     * 描述：清除手机未读记录
     * @author 李帅
     * @created 2018年8月1日 下午1:54:21
     * @since 
     * @param phoneId
     */
    @Override
    public void clearUnReadRecord(@RequestParam("phoneId") String phoneId){
        systemManager.clearUnReadRecord(phoneId);
    }
    
    /**
     * 
     * 描述：清除所有缓存
     * @author 李帅
     * @created 2018年9月7日 下午3:32:49
     * @since
     */
    @Override
	public void clearAllCache() {
		mailManager.clearMailCache();
		smsManager.clearSmsCache();
		systemManager.clearSystemCache();
	}
    
    /**
     * 
     * 描述：查询系统推送控制
     * @author 李帅
     * @created 2018年11月16日 下午4:41:49
     * @since 
     * @param userId
     * @param appId
     * @return
     */
    @Override
    public SystemPushControl getSystemPushControl(@RequestParam("userId") String userId, @RequestParam("appId") Long appId) {
        return systemManager.getSystemPushControl(userId, appId);
    }
    
    /**
     * 
     * 描述：添加或修改系统推送控制
     * @author 李帅
     * @created 2018年11月16日 下午4:43:01
     * @since 
     * @param systemPushControlDto
     */
    @Override
    public void addSystemPushControl(@RequestBody SystemPushControlDto systemPushControlDto) {
    	systemManager.addSystemPushControl(systemPushControlDto);
    }
}
