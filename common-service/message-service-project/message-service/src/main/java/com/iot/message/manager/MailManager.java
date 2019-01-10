package com.iot.message.manager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.iot.common.exception.BusinessException;
import com.iot.common.util.CommonUtil;
import com.iot.common.util.StringUtil;
import com.iot.exception.BusinessExceptionEnum;
import com.iot.message.contants.ModuleConstants;
import com.iot.message.entity.PushNoticeLog;
import com.iot.message.entity.PushNoticeTemplate;
import com.iot.message.entity.TenantMailInfo;
import com.iot.message.service.MessageService;
import com.iot.message.utils.PushContentUtil;
import com.iot.redis.RedisCacheUtil;
import com.iot.system.api.LangApi;

@Service
public class MailManager {

    private static final Logger log = Logger.getLogger(MailManager.class);

    private static Map<String, String> tempMap = new HashMap<String, String>();

    //	1-邮件;2-IOS;3-Android; 4-MQTT;5-短信
    private String templateType = "1";

    @Autowired
    private MessageService messageService;

    @Autowired
    private LangApi langApi;
    
    /**
     * 描述：邮件服务-平台级单个推送
     *
     * @param to
     * @param noticeMap
     * @param retryNum
     * @return
     * @author 李帅
     * @created 2018年3月12日 下午2:15:37
     * @since
     */
    public boolean mailSinglePush(Long appId, String to, Map<String, String> noticeMap, Integer retryNum, String langage) throws BusinessException {
    	TenantMailInfo tenantMailInfo = null;
    	if (appId == null) {
    		throw new BusinessException(BusinessExceptionEnum.APPID_ISNULL);
        }else {
        	tenantMailInfo = RedisCacheUtil.valueObjGet(ModuleConstants.MAILREDIS + appId, TenantMailInfo.class);
        	if(tenantMailInfo == null) {
        		tenantMailInfo = messageService.getTenantMailInfo(appId);
            	if(tenantMailInfo == null) {
            		throw new BusinessException(BusinessExceptionEnum.TENANTMAILINFO_ISNULL);
            	}
            	RedisCacheUtil.valueObjSet(ModuleConstants.MAILREDIS + appId, tenantMailInfo);
        	}
        	
        }
        if (StringUtil.isEmpty(to)) {
            throw new BusinessException(BusinessExceptionEnum.TO_ISNULL);
        }
        if (CommonUtil.isEmpty(noticeMap)) {
            throw new BusinessException(BusinessExceptionEnum.NOTICEMAP_ISNULL);
        }
        if (retryNum == null) {
            retryNum = 0;
        }
        if(StringUtil.isEmpty(langage)) {
        	langage = "en_US";
        }
        try {
        	boolean flag = true;
        	flag = pushEmailNotification(tenantMailInfo.getTenantId(), appId, tenantMailInfo.getMailHost(), tenantMailInfo.getMailPort(), tenantMailInfo.getMailName(),
            		tenantMailInfo.getPassWord(), to, noticeMap, retryNum, langage);
            return flag;
        } catch (Exception e) {
            log.error("", e);
            throw new BusinessException(BusinessExceptionEnum.MAIL_SINGLE_PUSH_ERROR);
        }
    }

    /**
     * 描述：邮件服务-平台级单个附件邮件推送
     *
     * @param to
     * @param noticeMap
     * @param retryNum
     * @return
     * @author 李帅
     * @created 2018年3月12日 下午2:15:37
     * @since
     */
    public boolean annexMailSinglePush(MultipartFile[] files, Long appId, String message,
    		Integer retryNum, String langage) throws BusinessException {
    	TenantMailInfo tenantMailInfo = null;
    	if (appId == null) {
    		throw new BusinessException(BusinessExceptionEnum.APPID_ISNULL);
        }else {
        	tenantMailInfo = RedisCacheUtil.valueObjGet(ModuleConstants.MAILREDIS + appId, TenantMailInfo.class);
        	if(tenantMailInfo == null) {
        		tenantMailInfo = messageService.getTenantMailInfo(appId);
            	if(tenantMailInfo == null) {
            		throw new BusinessException(BusinessExceptionEnum.TENANTMAILINFO_ISNULL);
            	}
            	RedisCacheUtil.valueObjSet(ModuleConstants.MAILREDIS + appId, tenantMailInfo);
        	}
        	
        }
        if (retryNum == null) {
            retryNum = 0;
        }
        if(StringUtil.isEmpty(langage)) {
        	langage = "en_US";
        }
        try {
        	boolean flag = true;
        	flag = pushAnnexEmailNotification(tenantMailInfo.getTenantId(), appId, tenantMailInfo.getMailHost(), tenantMailInfo.getMailPort(), tenantMailInfo.getMailName(),
            		tenantMailInfo.getPassWord(), message, retryNum, langage, files);
            return flag;
        } catch (Exception e) {
            log.error("", e);
            throw new BusinessException(BusinessExceptionEnum.ANNEX_MAIL_SINGLE_PUSH_ERROR);
        }
    }
    
    /**
     * 
     * 描述：邮件服务-测试邮件推送配置
     * @author 李帅
     * @created 2018年12月10日 下午6:57:59
     * @since 
     * @param host
     * @param port
     * @param userName
     * @param passWord
     * @param message
     * @return
     */
    public boolean testMailPushConfig(String host, Integer port, String userName, String passWord, String message){
    	try {
			Properties props = new Properties();
			props.setProperty("mail.debug", "true");
			props.setProperty("mail.smtp.auth", "true");
			props.setProperty("mail.host", host);
			if(port == 465) {
				props.put("mail.smtp.ssl.enable", "true");
			    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			    props.put("mail.smtp.socketFactory.port", "465");
			}
			props.put("mail.smtp.port", port);
			props.setProperty("mail.transport.protocol", "smtp");
			Session session = Session.getInstance(props);
			if (StringUtil.isNotBlank(host) && host.contains("gmail.com")) {
			    session = Session.getDefaultInstance(props,
			            new Authenticator() {
			                protected PasswordAuthentication getPasswordAuthentication() {
			                    return new PasswordAuthentication(userName, passWord);
			                }
			            });
			}
			// 创建邮件对象
			Message msg = new MimeMessage(session);
			msg.setSubject("Mail Push Config Test");
			// 设置邮件内容
			msg.setContent(message, "text/html; charset=utf-8");

			msg.setSentDate(new Date());
			msg.setFrom(new InternetAddress(userName));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(userName));
			if (userName.contains("@outlook")) {
			    msg.addRecipient(Message.RecipientType.CC, new InternetAddress(userName));
			}
			msg.saveChanges();
			Transport transport = session.getTransport("smtp");
			// 连接邮件服务器
			transport.connect(userName, passWord);
			// 发送邮件
			transport.sendMessage(msg, msg.getAllRecipients());
			// 关闭连接
			transport.close();
			return true;
		} catch (Exception e) {
			throw new BusinessException(BusinessExceptionEnum.TEST_MAIL_PUSH_ERROR);
		}
    }
    
    /**
     * 描述：邮件服务-平台级批量推送
     *
     * @param tos
     * @param noticeMap
     * @param retryNum
     * @return
     * @author 李帅
     * @created 2018年3月12日 下午2:15:43
     * @since
     */
    public boolean mailBatchPush(Long appId, List<String> tos, Map<String, String> noticeMap, 
    		Integer retryNum, String langage) throws BusinessException {
    	TenantMailInfo tenantMailInfo = null;
    	if (appId == null) {
    		throw new BusinessException(BusinessExceptionEnum.APPID_ISNULL);
        }else {
        	tenantMailInfo = RedisCacheUtil.valueObjGet(ModuleConstants.MAILREDIS + appId, TenantMailInfo.class);
        	if(tenantMailInfo == null) {
        		tenantMailInfo = messageService.getTenantMailInfo(appId);
            	if(tenantMailInfo == null) {
            		throw new BusinessException(BusinessExceptionEnum.TENANTMAILINFO_ISNULL);
            	}
            	RedisCacheUtil.valueObjSet(ModuleConstants.MAILREDIS + appId, tenantMailInfo);
        	}
        	
        }
        if (CommonUtil.isEmpty(tos)) {
            throw new BusinessException(BusinessExceptionEnum.TOS_ISNULL);
        }
        if (CommonUtil.isEmpty(noticeMap)) {
            throw new BusinessException(BusinessExceptionEnum.NOTICEMAP_ISNULL);
        }
        if (retryNum == null) {
            retryNum = 0;
        }
        if(StringUtil.isEmpty(langage)) {
        	langage = "en_US";
        }
        try {
            for (String to : tos) {
                pushEmailNotification(tenantMailInfo.getTenantId(), appId, tenantMailInfo.getMailHost(), tenantMailInfo.getMailPort(), tenantMailInfo.getMailName(),
                		tenantMailInfo.getPassWord(), to, noticeMap, retryNum, langage);
            }
            return true;
        } catch (Exception e) {
            log.error("", e);
            throw new BusinessException(BusinessExceptionEnum.MAIL_BATCH_PUSH_ERROR);
        }
    }

    /**
     * 描述：邮件消息推送方法
     *
     * @param to
     * @param subject
     * @param templateName
     * @param map
     * @throws Exception
     * @author 李帅
     * @created 2018年3月1日 下午4:10:25
     * @since
     */
    public boolean pushEmailNotification(Long tenantId, Long appId, String host, int port, String userName, String passWord, String to, 
    		Map<String, String> noticeMap, Integer retryNum, String langage) throws BusinessException {

        String subject = noticeMap.get("subject");
        String templateId = noticeMap.get("templateId");

        PushNoticeLog pushNoticeLog = new PushNoticeLog();
        pushNoticeLog.setPushFrom(userName);
        pushNoticeLog.setPushTo(to);
        pushNoticeLog.setPushType(templateType);
        pushNoticeLog.setPushTime(new Date());
        pushNoticeLog.setTemplateId(templateId);
        pushNoticeLog.setNoticeSubject(subject);
        pushNoticeLog.setParamValue(noticeMap.toString());

        try {
            Properties props = new Properties();
            props.setProperty("mail.debug", "true");
            props.setProperty("mail.smtp.auth", "true");
            props.setProperty("mail.host", host);
//            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//            if (StringUtil.isNotBlank(host) && host.contains("leedarson.com")) {
//                props.put("mail.smtp.port", "465");
//			}
            if(port == 465) {
            	props.put("mail.smtp.ssl.enable", "true");
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.put("mail.smtp.socketFactory.port", "465");
            }
            props.put("mail.smtp.port", port);
            props.setProperty("mail.transport.protocol", "smtp");
            Session session = Session.getInstance(props);
            log.info("host:" + host);
            if (StringUtil.isNotBlank(host) && host.contains("gmail.com")) {
//                props.put("mail.smtp.ssl.enable", "true");
//                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//                props.put("mail.smtp.port", "465");
//                props.put("mail.smtp.socketFactory.port", "465");
                session = Session.getDefaultInstance(props,
                        new Authenticator() {
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(userName, passWord);
                            }
                        });
			}
            // 创建邮件对象
            Message msg = new MimeMessage(session);
            msg.setSubject(subject);

            //通过模板文件名称获取指定模板
            String temp = null;
            if (tempMap.containsKey(tenantId + "_" + templateId + '_' + langage)) {
                // 缓存中有该模板直接返回
                temp = tempMap.get(tenantId + "_" + templateId + '_' + langage);
            } else {
                // 缓存中没有该模板时，生成新模板并放入缓存中
				try {
					String langKey = null;
					PushNoticeTemplate pushNoticeTemplate = messageService.getPushNoticeTemplateById(tenantId, templateId, templateType);
					if (pushNoticeTemplate != null) {
						langKey = pushNoticeTemplate.getTemplateContent();
					} else {
						pushNoticeTemplate = messageService.getPushNoticeTemplateById(null, templateId, templateType);
						if (pushNoticeTemplate == null) {
							throw new BusinessException(BusinessExceptionEnum.TEMPLATECONTENT_ISNULL);
						}
						langKey = pushNoticeTemplate.getTemplateContent();
					}
//              if("EN00001".equals(templateId) && (tenantId == 0 || tenantId == 1 || tenantId == 2)) {
//            		langKey = messageService.getPushNoticeTemplateById(tenantId, templateId, templateType).getTemplateContent();
//            	}else {
//            		langKey = messageService.getPushNoticeTemplateById(null, templateId, templateType).getTemplateContent();
//            	}
					Collection<String> langKeys = new ArrayList<String>();
					langKeys.add(langKey);
					Map<String, String> langTempMap = langApi.getLangValueByKey(langKeys, langage);
					temp = langTempMap.get(langKey);
					if (temp == null) {
						throw new BusinessException(BusinessExceptionEnum.TEMPLATE_INFO_ISNULL);
					}
				} catch (Exception e) {
                    e.printStackTrace();
                }
                tempMap.put(tenantId + "_" + templateId + '_' + langage, temp);
            }
            // 设置邮件内容
            String maiBody = PushContentUtil.generateHtmlFromFtl(noticeMap, temp);
            msg.setContent(maiBody, "text/html; charset=utf-8");

            msg.setSentDate(new Date());
            msg.setFrom(new InternetAddress(userName));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            if (to.contains("@outlook")) {
                msg.addRecipient(Message.RecipientType.CC, new InternetAddress(userName));
            }
            msg.saveChanges();
            Transport transport = session.getTransport("smtp");
            // 连接邮件服务器
            transport.connect(userName, passWord);
            // 发送邮件
            transport.sendMessage(msg, msg.getAllRecipients());
            // 关闭连接
            transport.close();
            // 添加推送日志
            pushNoticeLog.setResultCode("200");
            pushNoticeLog.setResultType("200");
            pushNoticeLog.setResultMessage("Success");
            pushNoticeLog.setResultAnswerTime(new Date());
            messageService.addPushNoticeLog(pushNoticeLog);
            return true;
        } catch (Exception e) {
            log.error("Push Email Fail StackTrace:" + e.getStackTrace().toString());
            log.error("Push Email Fail Suppressed:" + e.getSuppressed().toString());
            log.error("Push Email Fail Cause:" + e.getCause());
            log.error("Push Email Fail LocalizedMessage:" + e.getLocalizedMessage());
            log.error("Push Email Fail Message:" + e.getMessage());
            log.error("Push Email Fail: ", e);
            // 添加推送日志
            pushNoticeLog.setResultCode("");
            pushNoticeLog.setResultType("");
            pushNoticeLog.setResultMessage(e.getMessage());
            pushNoticeLog.setResultAnswerTime(new Date());
            messageService.addPushNoticeLog(pushNoticeLog);
            if (retryNum > 0) {
            	retryNum--;
                pushEmailNotification(tenantId, appId, host, port, userName, passWord, to, noticeMap, retryNum, langage);
            }
            return false;
        }
    }
    
    /**
     * 
     * 描述：附件邮件消息推送方法
     * @author 李帅
     * @created 2018年11月12日 下午6:09:04
     * @since 
     * @param tenantId
     * @param host
     * @param port
     * @param userName
     * @param passWord
     * @param message
     * @param retryNum
     * @param langage
     * @param files
     * @return
     * @throws BusinessException
     */
	public boolean pushAnnexEmailNotification(Long tenantId, Long appId, String host, int port, String userName, String passWord, 
			String message, Integer retryNum, String langage, MultipartFile[] files) throws BusinessException {
    	String subject = null;
		String templateId = null;
    	if("zh_CN".equals(langage)) {
    		subject = "用户反馈";
    		templateId = "CH00011";
    	}else {
    		subject = "User Feedback";
    		templateId = "EN00011";
    	}
		
		String to = "customer_service@leedarsoncom";
		PushNoticeLog pushNoticeLog = new PushNoticeLog();
		pushNoticeLog.setPushFrom(userName);
		pushNoticeLog.setPushTo(to);
		pushNoticeLog.setPushType(templateType);
		pushNoticeLog.setPushTime(new Date());
		pushNoticeLog.setTemplateId(templateId);
		pushNoticeLog.setNoticeSubject(subject);
		pushNoticeLog.setParamValue(message);
		try {
			Properties props = new Properties();
			props.setProperty("mail.debug", "true");
			props.setProperty("mail.smtp.auth", "true");
			props.setProperty("mail.host", host);
			if(port == 465) {
            	props.put("mail.smtp.ssl.enable", "true");
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.put("mail.smtp.socketFactory.port", "465");
            }
			props.put("mail.smtp.port", port);
			props.setProperty("mail.transport.protocol", "smtp");
			Session session = Session.getInstance(props);
			log.info("host:" + host);
			if (StringUtil.isNotBlank(host) && host.contains("gmail.com")) {
//				props.put("mail.smtp.ssl.enable", "true");
//				props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//				props.put("mail.smtp.port", "465");
//				props.put("mail.smtp.socketFactory.port", "465");
				session = Session.getDefaultInstance(props, new Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(userName, passWord);
					}
				});
			}
			// 创建邮件对象
			Message msg = new MimeMessage(session);
			msg.setSubject(subject);
			// 通过模板文件名称获取指定模板
			String temp = null;
			if (tempMap.containsKey(tenantId + "_" + templateId + '_' + langage)) {
				// 缓存中有该模板直接返回
				temp = tempMap.get(tenantId + "_" + templateId + '_' + langage);
			} else {
				// 缓存中没有该模板时，生成新模板并放入缓存中
				try {
					String langKey = messageService.getPushNoticeTemplateById(null, templateId, templateType).getTemplateContent();
					Collection<String> langKeys = new ArrayList<String>();
					langKeys.add(langKey);
					Map<String, String> langTempMap = langApi.getLangValueByKey(langKeys, langage);
					temp = langTempMap.get(langKey);
				} catch (Exception e) {
					e.printStackTrace();
				}
				tempMap.put(tenantId + "_" + templateId + '_' + langage, temp);
			}
			// 设置邮件内容
			Map<String, String> noticeMap = new HashMap<String, String>();
			noticeMap.put("message", message);
			String maiBody = PushContentUtil.generateHtmlFromFtl(noticeMap, temp);
			msg.setSentDate(new Date());
			msg.setFrom(new InternetAddress(userName));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			if (to.contains("@outlook")) {
				msg.addRecipient(Message.RecipientType.CC, new InternetAddress(userName));
			}
			// 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
			Multipart multipart = new MimeMultipart();
			// 添加邮件正文
			BodyPart contentPart = new MimeBodyPart();
			contentPart.setContent(maiBody, "text/html;charset=UTF-8");
			multipart.addBodyPart(contentPart);
			// 添加附件的内容
//			List<File> fileLists = new ArrayList<File>();
			for(MultipartFile multipartFile : files) {
				if (multipartFile != null) {
					BodyPart attachmentBodyPart = new MimeBodyPart();
					File file = null;  
		            if(multipartFile.equals("")||multipartFile.getSize()<=0){  
		            	continue;  
		            }else{  
		                InputStream ins = multipartFile.getInputStream();  
		                file = new File(multipartFile.getOriginalFilename());  
		                inputStreamToFile(ins, file); 
		            }  
					DataSource source = new FileDataSource(file);
					attachmentBodyPart.setDataHandler(new DataHandler(source));
					// 网上流传的解决文件名乱码的方法，其实用MimeUtility.encodeWord就可以很方便的搞定,这里很重要，通过下面的Base64编码的转换可以保证你的中文附件标题名在发送时不会变成乱码
					sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
					attachmentBodyPart.setFileName("=?GBK?B?" + enc.encode(multipartFile.getOriginalFilename().getBytes()) + "?=");
					attachmentBodyPart.setFileName(MimeUtility.encodeWord(multipartFile.getOriginalFilename()));
					multipart.addBodyPart(attachmentBodyPart);
				}
			}
			
			// 将multipart对象放到message中
			msg.setContent(multipart);
			// 保存邮件
			msg.saveChanges();
			Transport transport = session.getTransport("smtp");
			// 连接邮件服务器
			transport.connect(userName, passWord);
			// 发送邮件
			transport.sendMessage(msg, msg.getAllRecipients());
			// 关闭连接
			transport.close();
			// 添加推送日志
			pushNoticeLog.setResultCode("200");
			pushNoticeLog.setResultType("200");
			pushNoticeLog.setResultMessage("Success");
			pushNoticeLog.setResultAnswerTime(new Date());
			messageService.addPushNoticeLog(pushNoticeLog);
			return true;
		} catch (Exception e) {
			log.error("Push Email Fail StackTrace:" + e.getStackTrace().toString());
			log.error("Push Email Fail Suppressed:" + e.getSuppressed().toString());
			log.error("Push Email Fail Cause:" + e.getCause());
			log.error("Push Email Fail LocalizedMessage:" + e.getLocalizedMessage());
			log.error("Push Email Fail Message:" + e.getMessage());
			log.error("Push Email Fail: ", e);
			// 添加推送日志
			pushNoticeLog.setResultCode("");
			pushNoticeLog.setResultType("");
			pushNoticeLog.setResultMessage(e.getMessage());
			pushNoticeLog.setResultAnswerTime(new Date());
			messageService.addPushNoticeLog(pushNoticeLog);
			if (retryNum > 0) {
				retryNum--;
				pushAnnexEmailNotification(tenantId, appId, host, port, userName, passWord, message, retryNum, langage, files);
			}
			return false;
		}
	}
    
    public static void inputStreamToFile(InputStream ins,File file) {  
        try {  
         OutputStream os = new FileOutputStream(file);  
         int bytesRead = 0;  
         byte[] buffer = new byte[8192];  
         while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {  
          os.write(buffer, 0, bytesRead);  
         }  
         os.close();  
         ins.close();  
        } catch (Exception e) {  
         e.printStackTrace();  
        }  
       }  
    
    /**
     * 
     * 描述：删除缓存
     * @author 李帅
     * @created 2018年9月7日 下午3:48:16
     * @since
     */
    public void clearMailCache() {
    	tempMap = new HashMap<String, String>();
    	RedisCacheUtil.deleteBlurry(ModuleConstants.MAILREDIS);
    }
}
