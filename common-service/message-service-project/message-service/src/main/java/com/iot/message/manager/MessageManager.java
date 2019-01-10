package com.iot.message.manager;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.iot.common.exception.BusinessException;
import com.iot.common.util.CommonUtil;
import com.iot.common.util.StringUtil;
import com.iot.exception.BusinessExceptionEnum;
import com.iot.message.entity.PushNoticeLog;
import com.iot.message.entity.PushNoticeTemplate;
import com.iot.message.service.MessageService;

@Service
public class MessageManager {

	/**日志*/
	private static final Log LOGER = LogFactory.getLog(MessageManager.class);
	
    @Autowired
    private MessageService messageService;

    /**
     * 
     * 描述：添加推送日志
     * @author 李帅
     * @created 2018年3月12日 下午5:36:56
     * @since 
     * @param pushNoticeLog
     */
    public void addPushNoticeLog(PushNoticeLog pushNoticeLog) throws BusinessException{
    	if(CommonUtil.isEmpty(pushNoticeLog)){
			throw new BusinessException(BusinessExceptionEnum.PUSHLOG_ISNULL);
		}
        try {
//        	pushNoticeLog.setId(RedisCacheUtil.incr(ModuleConstants.DB_TABLE_PUSH_NOTICE_LOG, 0L));
        	messageService.addPushNoticeLog(pushNoticeLog);
		} catch (Exception e) {
			LOGER.error("",e);
			throw new BusinessException(BusinessExceptionEnum.ADD_PUSHNOTICE_LOG_ERROR);
		}
    }
    
    /**
     * 
     * 描述：推送日志查询
     * @author 李帅
     * @created 2018年3月12日 下午3:30:08
     * @since 
     * @param pageNum
     * @param pageSize
     * @param pushNoticeLog
     * @return
     */
    public PageInfo<PushNoticeLog> getPushNoticeLog(Integer pageNum, Integer pageSize, PushNoticeLog pushNoticeLog) throws BusinessException{
    	PageInfo<PushNoticeLog> pageInfo= new PageInfo<>();
        try {
        	pageInfo = messageService.getPushNoticeLog(pageNum, pageSize, pushNoticeLog);
        } catch (Exception e) {
        	LOGER.error("",e);
			throw new BusinessException(BusinessExceptionEnum.GET_PUSHNOTICE_LOG_ERROR);
        }
        return pageInfo;
    }
    
    /**
     * 
     * 描述：推送模板的添加/更新功能
     * @author 李帅
     * @created 2018年3月12日 下午3:44:15
     * @since 
     * @param pushNoticeTemplate
     */
    public void addPushNoticeTemplate(PushNoticeTemplate pushNoticeTemplate) throws BusinessException{
    	if(StringUtil.isEmpty(pushNoticeTemplate.getTemplateId())){
			throw new BusinessException(BusinessExceptionEnum.TEMPLATEID_ISNULL);
		}
    	if(StringUtil.isEmpty(pushNoticeTemplate.getTemplateContent())){
			throw new BusinessException(BusinessExceptionEnum.TEMPLATECONTENT_ISNULL);
		}
    	if(StringUtil.isEmpty(pushNoticeTemplate.getTemplateType())){
			throw new BusinessException(BusinessExceptionEnum.TEMPLATETYPE_ISNULL);
		}
    	if(StringUtil.isEmpty(pushNoticeTemplate.getCreator())){
			throw new BusinessException(BusinessExceptionEnum.CREATOR_ISNULL);
		}
        try {
//        	pushNoticeTemplate.setId(RedisCacheUtil.incr(ModuleConstants.DB_TABLE_PUSH_NOTICE_TEMPLATE, 0l));
        	pushNoticeTemplate.setCreateTime(new Date());
        	pushNoticeTemplate.setIsDelete("0");
        	messageService.addPushNoticeTemplate(pushNoticeTemplate);
		} catch (Exception e) {
			LOGER.error("",e);
			throw new BusinessException(BusinessExceptionEnum.ADD_PUSHNOTICE_TEMPLATE_ERROR);
		}
    }
    
    /**
     * 
     * 描述：推送模板查询功能
     * @author 李帅
     * @created 2018年3月12日 下午3:44:25
     * @since 
     * @param pageNum
     * @param pageSize
     * @param pushNoticeTemplate
     * @return
     */
    public PageInfo<PushNoticeTemplate> getPushNoticeTemplate(Integer pageNum, Integer pageSize, PushNoticeTemplate pushNoticeTemplate) throws BusinessException{
    	PageInfo<PushNoticeTemplate> pageInfo = new PageInfo<>();
        try {
        	pageInfo = messageService.getPushNoticeTemplate(pageNum, pageSize, pushNoticeTemplate);
        } catch (Exception e) {
        	LOGER.error("",e);
			throw new BusinessException(BusinessExceptionEnum.GET_PUSHNOTICE_TEMPLATE_ERROR);
        }
        return pageInfo;
    }

}
