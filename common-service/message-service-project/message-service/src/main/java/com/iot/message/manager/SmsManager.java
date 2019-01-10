package com.iot.message.manager;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.iot.common.exception.BusinessException;
import com.iot.common.util.CommonUtil;
import com.iot.common.util.StringUtil;
import com.iot.exception.BusinessExceptionEnum;
import com.iot.message.entity.PushNoticeLog;
import com.iot.message.service.MessageService;
import com.iot.message.utils.PushContentUtil;

@SuppressWarnings("deprecation")
@Service
public class SmsManager {

	private static final Logger log = Logger.getLogger(SmsManager.class);

	private static Map<String, String> tempMap = new HashMap<String, String>();
	
//	1-邮件;2-IOS;3-Android; 4-MQTT;5-短信
	private String templateType = "5";
	
	@Autowired
	private MessageService messageService;

	@Value("${sms.accessKeyId}")
	private String accessKeyId;
    
    @Value("${sms.secretKey}")
	private String secretKey;
    
    @Value("${sms.endpoint}")
	private String endpoint;
    
	private AmazonSNS snsClient;

	@PostConstruct
	public void init(){
		try {
			snsClient = new AmazonSNSClient(new AWSCredentials() {
				@Override
				public String getAWSAccessKeyId() {
					return accessKeyId;
				}

				@Override
				public String getAWSSecretKey() {
					return secretKey;
				}
			});
			snsClient.setEndpoint(endpoint);
			snsClient.setRegion(Region.getRegion(Regions.US_EAST_1));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	public SmsManager() {

	}

	/**
	 * 
	 * 描述：短信消息单个推送
	 * 
	 * @author 李帅
	 * @created 2018年3月14日 下午3:37:08
	 * @since
	 * @param userPhoneNo
	 * @param noticeMap
	 * @param retryNum
	 * @return
	 */
	public boolean smsSinglePush(String userPhoneNo, Map<String, String> noticeMap, Integer retryNum) throws BusinessException{
		if(StringUtil.isEmpty(userPhoneNo)){
			throw new BusinessException(BusinessExceptionEnum.USERPHONENO_ISNULL);
		}
		if(CommonUtil.isEmpty(noticeMap)){
			throw new BusinessException(BusinessExceptionEnum.NOTICEMAP_ISNULL);
		}
		if(retryNum == null) {
    		retryNum = 0;
    	}
		try {
			boolean flag = true;
			flag = pushSMSNotification(userPhoneNo, noticeMap, retryNum);
			return flag;
		} catch (Exception e) {
			log.error("",e);
			throw new BusinessException(BusinessExceptionEnum.SMS_SINGLE_PUSH_ERROR);
		}
	}

	/**
	 * 
	 * 描述：短信消息批量推送
	 * 
	 * @author 李帅
	 * @created 2018年3月14日 下午3:36:57
	 * @since
	 * @param userPhoneNos
	 * @param noticeMap
	 * @param retryNum
	 * @return
	 */
	public boolean smsBatchPush(List<String> userPhoneNos, Map<String, String> noticeMap, Integer retryNum) throws BusinessException{
		if(CommonUtil.isEmpty(userPhoneNos)){
			throw new BusinessException(BusinessExceptionEnum.USERPHONENOS_ISNULL);
		}
		if(CommonUtil.isEmpty(noticeMap)){
			throw new BusinessException(BusinessExceptionEnum.NOTICEMAP_ISNULL);
		}
		if(retryNum == null) {
    		retryNum = 0;
    	}
		try {
			for (String userPhoneNo : userPhoneNos) {
				pushSMSNotification(userPhoneNo, noticeMap, retryNum);
			}
			return true;
		} catch (Exception e) {
			log.error("",e);
			throw new BusinessException(BusinessExceptionEnum.SMS_BATCH_PUSH_ERROR);
		}
	}

	/**
	 * 
	 * 描述：短信消息推送方法
	 * 
	 * @author 李帅
	 * @created 2018年3月14日 下午3:36:47
	 * @since
	 * @param userPhoneNo
	 * @param noticeMap
	 * @param retryNum
	 * @throws Exception
	 */
	public boolean pushSMSNotification(String userPhoneNo, Map<String, String> noticeMap, Integer retryNum) throws BusinessException{

		String templateId = noticeMap.get("templateId");

		PushNoticeLog pushNoticeLog = new PushNoticeLog();
//		pushNoticeLog.setId(RedisCacheUtil.incr(ModuleConstants.DB_TABLE_PUSH_NOTICE_LOG, 0L));
		pushNoticeLog.setPushFrom("Leedarson");
		pushNoticeLog.setPushTo(userPhoneNo);
		pushNoticeLog.setPushType(templateType);
		pushNoticeLog.setPushTime(new Date());
		pushNoticeLog.setTemplateId(templateId);
		pushNoticeLog.setNoticeSubject("");
		pushNoticeLog.setParamValue(noticeMap.toString());

		try {
			//通过模板文件名称获取指定模板
			String temp = null;
			if (tempMap.containsKey(templateId)) {
				// 缓存中有该模板直接返回
				temp = tempMap.get(templateId);
			}else {
				// 缓存中没有该模板时，生成新模板并放入缓存中
				try {
					temp = messageService.getPushNoticeTemplateById(null, templateId, templateType).getTemplateContent();
				} catch (Exception e) {
					e.printStackTrace();
				}
				tempMap.put(templateId, temp);
			}
			String smsBody = PushContentUtil.generateHtmlFromFtl(noticeMap, temp);
			
			Map<String, MessageAttributeValue> smsAttributes = new HashMap<String, MessageAttributeValue>();
			smsAttributes.put("AWS.SNS.SMS.SenderID",
					new MessageAttributeValue().withStringValue("leedarson").withDataType("String"));
			smsAttributes.put("AWS.SNS.SMS.MaxPrice",
					new MessageAttributeValue().withStringValue("0.50").withDataType("Number"));
			smsAttributes.put("AWS.SNS.SMS.SMSType",
					new MessageAttributeValue().withStringValue("Promotional").withDataType("String"));
			PublishResult result = snsClient.publish(new PublishRequest().withMessage(smsBody)
					.withPhoneNumber(userPhoneNo).withMessageAttributes(smsAttributes));
			System.out.println(result);
			// 添加推送日志
			pushNoticeLog.setResultCode("200");
			pushNoticeLog.setResultType("200");
			pushNoticeLog.setResultMessage("Success");
			pushNoticeLog.setResultAnswerTime(new Date());
			messageService.addPushNoticeLog(pushNoticeLog);
			return true;
		} catch (Exception e) {
			log.error("Push Email Fail StackTrace:" + e.getStackTrace());
			log.error("Push Email Fail Suppressed:" + e.getSuppressed());
			log.error("Push Email Fail Cause:" + e.getCause());
			log.error("Push Email Fail LocalizedMessage:" + e.getLocalizedMessage());
			log.error("Push Email Fail Message:" + e.getMessage());
			// Thread.sleep(60000);
			// 添加推送日志
			pushNoticeLog.setResultCode("");
			pushNoticeLog.setResultType("");
			pushNoticeLog.setResultMessage(e.getMessage());
			pushNoticeLog.setResultAnswerTime(new Date());
			messageService.addPushNoticeLog(pushNoticeLog);
			if (0 < retryNum) {
				retryNum--;
				pushSMSNotification(userPhoneNo, noticeMap, retryNum);
			}
			return false;
		}
	}

	/**
     * 
     * 描述：删除缓存
     * @author 李帅
     * @created 2018年9月7日 下午3:48:16
     * @since
     */
    public void clearSmsCache() {
    	tempMap = new HashMap<String, String>();
    }
}
