package com.iot.message.manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.iot.common.exception.BusinessException;
import com.iot.common.util.CommonUtil;
import com.iot.common.util.JsonUtil;
import com.iot.common.util.StringUtil;
import com.iot.exception.BusinessExceptionEnum;
import com.iot.message.contants.ModuleConstants;
import com.iot.message.dto.SystemPushControlDto;
import com.iot.message.dto.TenantMailInfoDto;
import com.iot.message.entity.AppCertInfo;
import com.iot.message.entity.PushNoticeLog;
import com.iot.message.entity.SystemPushControl;
import com.iot.message.entity.TenantMailInfo;
import com.iot.message.entity.UserPhoneRelate;
import com.iot.message.service.MessageService;
import com.iot.message.service.SystemService;
import com.iot.message.systempush.ISystemPush;
import com.iot.message.systempush.SystemPushFactory;
import com.iot.message.utils.ApnsUtil;
import com.iot.message.utils.PushContentUtil;
import com.iot.redis.RedisCacheUtil;

import freemarker.template.TemplateException;

@Service
public class SystemManager implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(SystemManager.class);

	
    private static Map<String, String> tempMap = new HashMap<String, String>();
    
//	1-邮件;2-IOS;3-Android; 4-MQTT;5-短信
	private String templateType2 = "2";
	private String templateType3 = "3";
	
	private String systemPushControlOff = "off";
//	private String systemPushControlOn = "on";
	
	public final static String DEFAULT_CHARSET = "utf-8";
	
    @Autowired
    private MessageService messageService;
    
    @Autowired
    private SystemService systemService;
    
    private ISystemPush systemPush;
    
    /**
     * 
     * 描述：默认构造
     * @author 李帅
     * @created 2018年11月28日 下午1:59:36
     * @since
     */
    @Override
   	public void run(String... arg0) throws Exception {
       	try {
   			log.info("get ISystemPush");
   			this.systemPush = SystemPushFactory.createSystemPush();
   			log.info("storage:{}",systemPush);
   		} catch (Exception e) {
   			log.error("SystemManagerServiceImpl init error:",e);
   		}
   		
   	}
	
    /**
	 * 
	 * 描述：系统消息单个推送
	 * @author 李帅
	 * @created 2018年3月12日 下午2:25:34
	 * @since 
	 * @param userId
	 * @param noticeMap
	 * @param retryNum
	 * @return
	 */
    public boolean systemSinglePush(String userId, Map<String, String> noticeMap,Integer retryNum) throws BusinessException{
    	if(StringUtil.isEmpty(userId)){
			throw new BusinessException(BusinessExceptionEnum.USERID_ISNULL);
		}
    	if(CommonUtil.isEmpty(noticeMap)){
			throw new BusinessException(BusinessExceptionEnum.NOTICEMAP_ISNULL);
		}
    	if(retryNum == null) {
    		retryNum = 0;
    	}
    	log.info("userId:{}, noticeMap:{}",userId, JsonUtil.toJson(noticeMap));
        try {
        	boolean flag = true;
        	List<String> userIds = new ArrayList<String>();
        	userIds.add(userId);
        	List<UserPhoneRelate> userPhoneRelates = systemService.getUserPhoneRelates(userIds);
        	
        	if (userPhoneRelates!=null && userPhoneRelates.size()>0){
        		if(StringUtil.isEmpty(userPhoneRelates.get(0).getPhoneId())){
        			throw new BusinessException(BusinessExceptionEnum.PHONEID_ISNULL);
        		}
        		SystemPushControl systemPushControl = getSystemPushControl(userId, userPhoneRelates.get(0).getAppId());
        		if(systemPushControl != null && systemPushControlOff.equals(systemPushControl.getSwitCh())) {
        			return true;
        		}
				if("1".equals(userPhoneRelates.get(0).getPhoneType())) {
					flag = pushIOSSystemNotification(userPhoneRelates.get(0).getAppId(), userPhoneRelates.get(0).getPhoneId(), noticeMap, retryNum);
				}else {
					flag = pushAndroidSystemNotification(userPhoneRelates.get(0).getAppId(), userPhoneRelates.get(0).getPhoneId(), noticeMap, retryNum);
				}
			}
            return flag;
        } catch (Exception e) {
        	log.error("",e);
			throw new BusinessException(BusinessExceptionEnum.SYSTEM_SINGLE_PUSH_ERROR);
        }
    }

    /**
	 * 
	 * 描述：系统消息批量推送
	 * @author 李帅
	 * @created 2018年3月12日 下午2:25:43
	 * @since 
	 * @param userIds
	 * @param noticeMap
	 * @param retryNum
	 * @return
	 */
    public boolean systemBatchPush(List<String> userIds, Map<String, String> noticeMap, Integer retryNum) throws BusinessException{
    	if(CommonUtil.isEmpty(userIds)){
			throw new BusinessException(BusinessExceptionEnum.USERIDS_ISNULL);
		}
    	if(CommonUtil.isEmpty(noticeMap)){
			throw new BusinessException(BusinessExceptionEnum.NOTICEMAP_ISNULL);
		}
    	if(retryNum == null) {
    		retryNum = 0;
    	}
        try {
        	List<UserPhoneRelate> userPhoneRelates = systemService.getUserPhoneRelates(userIds);
        	for(UserPhoneRelate userPhoneRelate : userPhoneRelates) {
        		if(StringUtil.isEmpty(userPhoneRelate.getPhoneId())){
        			throw new BusinessException(BusinessExceptionEnum.PHONEID_ISNULL);
        		}
        		SystemPushControl systemPushControl = getSystemPushControl(userPhoneRelate.getUserId(), userPhoneRelate.getAppId());
        		if(systemPushControl != null && systemPushControlOff.equals(systemPushControl.getSwitCh())) {
        			return true;
        		}
        		if("1".equals(userPhoneRelate.getPhoneType())) {
            		pushIOSSystemNotification(userPhoneRelate.getAppId(), userPhoneRelate.getPhoneId(), noticeMap, retryNum);
            	}else {
            		pushAndroidSystemNotification(userPhoneRelate.getAppId(), userPhoneRelate.getPhoneId(), noticeMap, retryNum);
            	}
        	}
            return true;
        } catch (Exception e) {
        	log.error("",e);
			throw new BusinessException(BusinessExceptionEnum.SYSTEM_BATCH_PUSH_ERROR);
        }
    }

    /**
	 * 
	 * 描述：新增或更新用户和手机端关系
	 * @author 李帅
	 * @created 2018年3月12日 下午3:13:21
	 * @since 
	 * @param userPhoneRelate
	 * @return
	 */
    public void addUserPhoneRelate(UserPhoneRelate userPhoneRelate) throws BusinessException{
    	if(StringUtil.isEmpty(userPhoneRelate.getUserId())){
			throw new BusinessException(BusinessExceptionEnum.USERID_ISNULL);
		}
    	if(StringUtil.isEmpty(userPhoneRelate.getPhoneId())){
			throw new BusinessException(BusinessExceptionEnum.PHONEID_ISNULL);
		}
    	if(StringUtil.isEmpty(userPhoneRelate.getPhoneType())){
			throw new BusinessException(BusinessExceptionEnum.PHINETYPE_ISNULL);
		}
    	if(userPhoneRelate.getAppId() == null || userPhoneRelate.getAppId() == 0){
			throw new BusinessException(BusinessExceptionEnum.APPID_ISNULL);
		}
    	if(StringUtil.isEmpty(userPhoneRelate.getCreator())){
			throw new BusinessException(BusinessExceptionEnum.CREATOR_ISNULL);
		}
        try {
        	List<String> userIds = new ArrayList<String>();
        	userIds.add(userPhoneRelate.getUserId());
        	List<UserPhoneRelate> userPhoneRelates = systemService.getUserPhoneRelates(userIds);
        	if(userPhoneRelates == null || userPhoneRelates.size() == 0) {
        		userPhoneRelate.setCreateTime(new Date());
        		userPhoneRelate.setDataStatus("0");
        		systemService.addUserPhoneRelate(userPhoneRelate);
        	}else {
				userPhoneRelate.setUpdateTime(new Date());
        		systemService.updateUserPhoneRelate(userPhoneRelate);
        	}
        } catch (Exception e) {
        	log.error("",e);
			throw new BusinessException(BusinessExceptionEnum.ADD_USER_PHONE_RELATE_ERROR);
        }
    }
    
    /**
     * 
     * 描述：IOS系统消息方法
	 * @author 李帅
	 * @created 2018年3月1日 下午4:10:25
     * @since 
     * @param appId
     * @param phoneId
     * @param noticeMap
     * @param retryNum
     * @return
     * @throws Exception
     */
	public boolean pushIOSSystemNotification(Long appId, String phoneId, Map<String, String> noticeMap, Integer retryNum)
			throws Exception {
		if (appId == null || appId == 0){
			throw new BusinessException(BusinessExceptionEnum.APPID_ISNULL);
		}
		if(StringUtil.isEmpty(phoneId)){
			throw new BusinessException(BusinessExceptionEnum.PHONEID_ISNULL);
		}
		//获取证书
		AppCertInfo appCertInfo = getAppCertInfo(appId);
		//组装日志信息
		String templateId = noticeMap.get("templateId");
		PushNoticeLog pushNoticeLog = new PushNoticeLog();
		pushNoticeLog.setPushFrom(appCertInfo.getTenantId().toString());
		pushNoticeLog.setPushTo(phoneId);
		pushNoticeLog.setPushType(templateType2);
		pushNoticeLog.setPushTime(new Date());
		pushNoticeLog.setTemplateId(templateId);
		pushNoticeLog.setNoticeSubject("");
		pushNoticeLog.setParamValue(noticeMap.toString());
		try {
			// 推送消息
			ApnsUtil.apnsNotification(appId, appCertInfo, phoneId, getPushInfo(templateId, noticeMap), noticeMap);
			pushNoticeLog.setResultCode("200");
			pushNoticeLog.setResultType("200");
			pushNoticeLog.setResultMessage("Success");
			pushNoticeLog.setResultAnswerTime(new Date());
			messageService.addPushNoticeLog(pushNoticeLog);
			return true;
		} catch (Exception e) {
			log.error("IOS Push Fail StackTrace:" + e.getStackTrace());
			log.error("IOS Push Fail Suppressed:" + e.getSuppressed());
			log.error("IOS Push Fail Cause:" + e.getCause());
			log.error("IOS Push Fail LocalizedMessage:" + e.getLocalizedMessage());
			log.error("IOS Push Fail Message:" + e.getMessage());
			pushNoticeLog.setResultCode("");
			pushNoticeLog.setResultType("");
			pushNoticeLog.setResultMessage(e.getMessage());
			pushNoticeLog.setResultAnswerTime(new Date());
			messageService.addPushNoticeLog(pushNoticeLog);
			if (0 < retryNum) {
				retryNum--;
				//回退未读数据量
				Integer unReadNum = RedisCacheUtil.valueObjGet(ModuleConstants.UNREADNUM + phoneId, Integer.class);
				RedisCacheUtil.valueObjSet(ModuleConstants.UNREADNUM + phoneId, unReadNum - 1);
				pushIOSSystemNotification(appId, phoneId, noticeMap, retryNum);
			}
			return false;
		}
	}
	
	/**
	 * 
	 * 描述：Android系统消息方法
	 * @author 李帅
	 * @created 2018年3月1日 下午4:10:25
	 * @since 
	 * @param appId
	 * @param phoneId
	 * @param noticeMap
	 * @param retryNum
	 * @return
	 * @throws Exception
	 */
	public boolean pushAndroidSystemNotification(Long appId, String phoneId, Map<String, String> noticeMap, Integer retryNum)
			throws Exception {
		if (appId == null || appId == 0){
			throw new BusinessException(BusinessExceptionEnum.APPID_ISNULL);
		}
		if(StringUtil.isEmpty(phoneId)){
			throw new BusinessException(BusinessExceptionEnum.PHONEID_ISNULL);
		}
		//获取证书
		AppCertInfo appCertInfo = getAppCertInfo(appId);
		//组装日志信息
		String templateId = noticeMap.get("templateId");
		PushNoticeLog pushNoticeLog = new PushNoticeLog();
		pushNoticeLog.setPushFrom(appCertInfo.getTenantId().toString());
		pushNoticeLog.setPushTo(phoneId);
		pushNoticeLog.setPushType(templateType3);
		pushNoticeLog.setPushTime(new Date());
		pushNoticeLog.setTemplateId(templateId);
		pushNoticeLog.setNoticeSubject("");
		pushNoticeLog.setParamValue(noticeMap.toString());
		
		try {
			// 推送消息
			systemPush.pushAndroidSystemNotification(appId, appCertInfo.getAndroidPushUrl(), appCertInfo.getAndroidPushKey(),
					phoneId, getPushInfo(templateId, noticeMap), noticeMap);
			
			pushNoticeLog.setResultCode("200");
			pushNoticeLog.setResultType("200");
			pushNoticeLog.setResultMessage("Success");
			pushNoticeLog.setResultAnswerTime(new Date());
			messageService.addPushNoticeLog(pushNoticeLog);
			return true;
		} catch (Exception e) {
			log.error("Android Push Fail StackTrace:" + e.getStackTrace());
			log.error("Android Push Fail Suppressed:" + e.getSuppressed());
			log.error("Android Push Fail Cause:" + e.getCause());
			log.error("Android Push Fail LocalizedMessage:" + e.getLocalizedMessage());
			log.error("Android Push Fail Message:" + e.getMessage());
			pushNoticeLog.setResultCode("");
			pushNoticeLog.setResultType("");
			pushNoticeLog.setResultMessage(e.getMessage());
			pushNoticeLog.setResultAnswerTime(new Date());
			messageService.addPushNoticeLog(pushNoticeLog);
			if (0 < retryNum) {
				retryNum--;
				//回退未读数据量
				Integer unReadNum = RedisCacheUtil.valueObjGet(ModuleConstants.UNREADNUM + phoneId.replaceAll("[^0-9a-zA-Z\u4e00-\u9fa5.，,。？“”]+", ""), Integer.class);
				RedisCacheUtil.valueObjSet(ModuleConstants.UNREADNUM + phoneId.replaceAll("[^0-9a-zA-Z\u4e00-\u9fa5.，,。？“”]+", ""), unReadNum - 1);
				pushAndroidSystemNotification(appId, phoneId, noticeMap, retryNum);
			}
			return false;
		}
	}

	/**
	 * 
	 * 描述：获取推送信息
	 * @author 李帅
	 * @created 2018年10月8日 上午11:17:26
	 * @since 
	 * @param templateId
	 * @param noticeMap
	 * @return
	 * @throws IOException
	 * @throws TemplateException
	 */
	public String getPushInfo(String templateId, Map<String, String> noticeMap) throws IOException, TemplateException {
		String temp = null;
		if (tempMap.containsKey(templateId)) {
			temp = tempMap.get(templateId);
		} else {
			try {
				temp = messageService.getPushNoticeTemplateById(null, templateId, null).getTemplateContent();
			} catch (Exception e) {
				e.printStackTrace();
			}
			tempMap.put(templateId, temp);
		}
		return PushContentUtil.generateHtmlFromFtl(noticeMap, temp);
	}
	
	/**
	 * @despriction：删除用户和手机端关系
	 * @author  yeshiyuan
	 * @created 2018/5/29 11:50
	 * @param userUuid
	 * @return
	 */
	public void delUserPhoneRelate(String userUuid) {
		if (StringUtil.isBlank(userUuid)){
			throw new BusinessException(BusinessExceptionEnum.USERID_ISNULL);
		}
		systemService.deleteUserPhoneRelate(userUuid);
	}
	
	/**
	 * 
	 * 描述：删除用户手机ID
	 * @author 李帅
	 * @created 2018年12月5日 下午5:43:56
	 * @since 
	 * @param userUuid
	 */
	public void deleteUserPhoneId(String userUuid) {
		if (StringUtil.isBlank(userUuid)){
			throw new BusinessException(BusinessExceptionEnum.USERID_ISNULL);
		}
		systemService.deleteUserPhoneId(userUuid);
	}
	
	/**
	 * 
	 * 描述：查询用户和手机端关系
	 * @author 李帅
	 * @created 2018年12月6日 上午9:53:44
	 * @since 
	 * @param userIds
	 * @return
	 */
	public List<UserPhoneRelate> getUserPhoneRelates(List<String> userIds) {
		if (userIds == null){
			throw new BusinessException(BusinessExceptionEnum.USERID_ISNULL);
		}
		return systemService.getUserPhoneRelates(userIds);
	}
	
	/**
	 * 
	 * 描述：查询APP证书信息
	 * @author 李帅
	 * @created 2018年7月24日 下午7:03:09
	 * @since 
	 * @param appId
	 * @return
	 */
	public AppCertInfo getAppCertInfo(Long appId) {
		if (appId == 0){
			throw new BusinessException(BusinessExceptionEnum.APPID_ISNULL);
		}
		AppCertInfo appCertInfo = RedisCacheUtil.valueObjGet(ModuleConstants.SYSTEMREDIS + appId, AppCertInfo.class);
    	if(appCertInfo == null) {
    		appCertInfo = systemService.getAppCertInfo(appId);
    		if(appCertInfo == null) {
    			throw new BusinessException(BusinessExceptionEnum.APPCERTINFO_ISNULL);
    		}
        	RedisCacheUtil.valueObjSet(ModuleConstants.SYSTEMREDIS + appId, appCertInfo);
    	}
		return appCertInfo;
	}
	
	/**
	 * 
	 * 描述：添加或修改APP证书信息
	 * @author 李帅
	 * @created 2018年7月24日 下午8:14:20
	 * @since 
	 * @param appCertInfoDto
	 * @throws IOException 
	 */
	public void addAppCertInfo(MultipartFile certInfo, Long tenantId, Long appId, String certPassWord,
			String testOrReleaseMark, String androidPushUrl, String androidPushKey){
		if(CommonUtil.isEmpty(certInfo)) {
			throw new BusinessException(BusinessExceptionEnum.APPCERTINFO_ISNULL);
		}
		byte[] cert;
		try {
			cert = certInfo.getBytes();
		} catch (IOException e) {
			throw new BusinessException(BusinessExceptionEnum.APPCERTINFO_ISNULL);
		}
        
		AppCertInfo appCertInfo = null;
		if (appId == 0){
			throw new BusinessException(BusinessExceptionEnum.APPID_ISNULL);
		}else {
			appCertInfo = systemService.getAppCertInfo(appId);
		}
		if(appCertInfo == null) {
			appCertInfo = new AppCertInfo();
			appCertInfo.setAppId(appId);
			appCertInfo.setCertInfo(cert);
			appCertInfo.setCertPassWord(certPassWord);
			appCertInfo.setTenantId(tenantId);
			if("test".equals(testOrReleaseMark)) {
				appCertInfo.setServiceHost("gateway.sandbox.push.apple.com");
				appCertInfo.setServicePort(2195);
			}else {
				appCertInfo.setServiceHost("gateway.push.apple.com");
				appCertInfo.setServicePort(2195);
			}
			appCertInfo.setAndroidPushKey(androidPushKey);
			appCertInfo.setAndroidPushUrl(androidPushUrl);
			systemService.addAppCertInfo(appCertInfo);
		}else {
			appCertInfo.setAppId(appId);
			appCertInfo.setCertInfo(cert);
			appCertInfo.setCertPassWord(certPassWord);
			appCertInfo.setTenantId(tenantId);
			if("test".equals(testOrReleaseMark)) {
				appCertInfo.setServiceHost("gateway.sandbox.push.apple.com");
				appCertInfo.setServicePort(2195);
			}else {
				appCertInfo.setServiceHost("gateway.push.apple.com");
				appCertInfo.setServicePort(2195);
			}
			appCertInfo.setAndroidPushKey(androidPushKey);
			appCertInfo.setAndroidPushUrl(androidPushUrl);
			systemService.updateAppCertInfo(appCertInfo);
			
		}
		RedisCacheUtil.valueObjSet(ModuleConstants.SYSTEMREDIS + appId, appCertInfo);
		
	}
	
	/**
	 * 
	 * 描述：查询租户邮箱信息
	 * @author 李帅
	 * @created 2018年7月24日 下午7:10:44
	 * @since 
	 * @param tenantId
	 * @return
	 */
	public TenantMailInfo getTenantMailInfo(Long appId) {
		TenantMailInfo tenantMailInfo = null;
    	if (appId == null) {
    		throw new BusinessException(BusinessExceptionEnum.TENANTID_ISNULL);
        }else {
        	tenantMailInfo = RedisCacheUtil.valueObjGet(ModuleConstants.MAILREDIS + appId, TenantMailInfo.class);
        	if(tenantMailInfo == null) {
        		tenantMailInfo = messageService.getTenantMailInfo(appId);
            	if(tenantMailInfo != null) {
            		RedisCacheUtil.valueObjSet(ModuleConstants.MAILREDIS + appId, tenantMailInfo);
            	}
        	}
        	
        }
		return tenantMailInfo;
	}
	
	/**
	 * 
	 * 描述：添加或修改租户邮箱信息
	 * @author 李帅
	 * @created 2018年7月24日 下午8:12:49
	 * @since 
	 * @param tenantMailInfoDto
	 */
	public void addTenantMailInfo(TenantMailInfoDto tenantMailInfoDto){
		TenantMailInfo tenantMailInfo = null;
		if(tenantMailInfoDto.getTenantId() == null) {
			throw new BusinessException(BusinessExceptionEnum.TENANTID_ISNULL);
		}else {
			tenantMailInfo = messageService.getTenantMailInfo(tenantMailInfoDto.getAppId());
		}
		if(tenantMailInfo == null) {
			tenantMailInfo = new TenantMailInfo();
			tenantMailInfo.setMailHost(tenantMailInfoDto.getMailHost());
			tenantMailInfo.setAppId(tenantMailInfoDto.getAppId());
			tenantMailInfo.setMailPort(tenantMailInfoDto.getMailPort());
			tenantMailInfo.setMailName(tenantMailInfoDto.getMailName());
			tenantMailInfo.setPassWord(tenantMailInfoDto.getPassWord());
			tenantMailInfo.setTenantId(tenantMailInfoDto.getTenantId());
			messageService.addTenantMailInfo(tenantMailInfo);
		}else {
			tenantMailInfo.setMailHost(tenantMailInfoDto.getMailHost());
			tenantMailInfo.setAppId(tenantMailInfoDto.getAppId());
			tenantMailInfo.setMailPort(tenantMailInfoDto.getMailPort());
			tenantMailInfo.setMailName(tenantMailInfoDto.getMailName());
			tenantMailInfo.setPassWord(tenantMailInfoDto.getPassWord());
			tenantMailInfo.setTenantId(tenantMailInfoDto.getTenantId());
			messageService.updateTenantMailInfo(tenantMailInfo);
		}
		RedisCacheUtil.valueObjSet(ModuleConstants.MAILREDIS + tenantMailInfoDto.getTenantId(), tenantMailInfo);
	}
	
	/**
	 * 
	 * 描述：清除手机未读记录
	 * @author 李帅
	 * @created 2018年8月1日 下午1:55:37
	 * @since 
	 * @param phoneId
	 */
	public void clearUnReadRecord(String phoneId){
		RedisCacheUtil.delete(ModuleConstants.UNREADNUM + phoneId.replaceAll("[^0-9a-zA-Z\u4e00-\u9fa5.，,。？“”]+",""));
	}
	
	/**
     * 
     * 描述：删除缓存
     * @author 李帅
     * @created 2018年9月7日 下午3:48:16
     * @since
     */
    public void clearSystemCache() {
    	tempMap = new HashMap<String, String>();
//    	ApnsUtil.clearApnsMap();
    	RedisCacheUtil.deleteBlurry(ModuleConstants.SYSTEMREDIS);
    }
    
    /**
     * 
     * 描述：查询系统推送控制
     * @author 李帅
     * @created 2018年11月16日 下午4:42:05
     * @since 
     * @param userId
     * @param appId
     * @return
     */
	public SystemPushControl getSystemPushControl(String userId, Long appId) {
		if (appId == null) {
			throw new BusinessException(BusinessExceptionEnum.APPID_ISNULL);
		}
		if (StringUtil.isEmpty(userId)) {
			throw new BusinessException(BusinessExceptionEnum.USERID_ISNULL);
		}
		SystemPushControl systemPushControl = RedisCacheUtil
				.valueObjGet(ModuleConstants.SYSTEMPUSHCONTROLREDIS + userId + ":" + appId, SystemPushControl.class);
		if (systemPushControl == null) {
			systemPushControl = systemService.getSystemPushControl(userId, appId);
			if(systemPushControl != null){
				RedisCacheUtil.valueObjSet(ModuleConstants.SYSTEMPUSHCONTROLREDIS + userId + ":" + appId,
						systemPushControl);
			}
		}
		return systemPushControl;
	}
	
	/**
	 * 
	 * 描述：添加或修改系统推送控制
	 * @author 李帅
	 * @created 2018年11月16日 下午4:43:14
	 * @since 
	 * @param systemPushControlDto
	 */
	public void addSystemPushControl(SystemPushControlDto systemPushControlDto) {
		if (systemPushControlDto.getTenantId() == null) {
			throw new BusinessException(BusinessExceptionEnum.TENANTID_ISNULL);
		}
		if (StringUtil.isEmpty(systemPushControlDto.getUserId())) {
			throw new BusinessException(BusinessExceptionEnum.USERID_ISNULL);
		}
		if (systemPushControlDto.getAppId() == null) {
			throw new BusinessException(BusinessExceptionEnum.APPID_ISNULL);
		}
		if (StringUtil.isEmpty(systemPushControlDto.getSwitCh())) {
			throw new BusinessException(BusinessExceptionEnum.SWITCH_ISNULL);
		}
		SystemPushControl systemPushControl = systemService.getSystemPushControl(systemPushControlDto.getUserId(),
				systemPushControlDto.getAppId());
		if (systemPushControl == null) {
			systemPushControl = new SystemPushControl();
			systemPushControl.setAppId(systemPushControlDto.getAppId());
			systemPushControl.setUserId(systemPushControlDto.getUserId());
			systemPushControl.setSwitCh(systemPushControlDto.getSwitCh());
			systemPushControl.setTenantId(systemPushControlDto.getTenantId());
			systemPushControl.setCreateTime(new Date());
			systemPushControl.setCreateBy(systemPushControlDto.getCreateBy());
			systemService.addSystemPushControl(systemPushControl);
		} else {
			systemPushControl.setAppId(systemPushControlDto.getAppId());
			systemPushControl.setUserId(systemPushControlDto.getUserId());
			systemPushControl.setSwitCh(systemPushControlDto.getSwitCh());
			systemPushControl.setTenantId(systemPushControlDto.getTenantId());
			systemPushControl.setUpdateTime(new Date());
			systemPushControl.setUpdateBy(systemPushControlDto.getCreateBy());
			systemService.updateSystemPushControl(systemPushControl);
		}
		RedisCacheUtil.valueObjSet(ModuleConstants.SYSTEMPUSHCONTROLREDIS + systemPushControlDto.getUserId() + ":"
				+ systemPushControlDto.getAppId(), systemPushControl);
	}
}
