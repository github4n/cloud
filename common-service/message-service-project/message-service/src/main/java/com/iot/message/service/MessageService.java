package com.iot.message.service;

import com.github.pagehelper.PageInfo;
import com.iot.message.entity.PushNoticeLog;
import com.iot.message.entity.PushNoticeTemplate;
import com.iot.message.entity.TenantMailInfo;

public interface MessageService {

	/**
	 * 
	 * 描述：添加推送日志
	 * @author 李帅
	 * @created 2018年3月12日 下午5:37:03
	 * @since 
	 * @param pushNoticeLog
	 */
    void addPushNoticeLog(PushNoticeLog pushNoticeLog);
    
	/**
	 * 
	 * 描述：推送日志查询
	 * @author 李帅
	 * @created 2018年3月12日 下午3:30:00
	 * @since 
	 * @param pageNum
	 * @param pageSize
	 * @param pushNoticeLog
	 * @return
	 */
    PageInfo<PushNoticeLog> getPushNoticeLog(Integer pageNum, Integer pageSize, PushNoticeLog pushNoticeLog);
    
    /**
     * 
     * 描述：推送模板的添加/更新功能
     * @author 李帅
     * @created 2018年3月12日 下午3:39:38
     * @since 
     * @param pushNoticeTemplate
     */
    void addPushNoticeTemplate(PushNoticeTemplate pushNoticeTemplate);
    
    /**
     * 
     * 描述：推送模板查询功能
     * @author 李帅
     * @created 2018年3月12日 下午3:39:50
     * @since 
     * @param pageNum
     * @param pageSize
     * @param pushNoticeTemplate
     * @return
     */
    PageInfo<PushNoticeTemplate> getPushNoticeTemplate(Integer pageNum,Integer pageSize, PushNoticeTemplate pushNoticeTemplate);
    
    /**
     * 
     * 描述：通过推送模板名称查询推送模板
     * @author 李帅
     * @created 2018年3月17日 下午2:24:36
     * @since 
     * @param templateName
     * @return
     */
    PushNoticeTemplate getPushNoticeTemplateById(Long tenantId, String templateId, String templateType);
    
    /**
     * 
     * 描述：查询租户邮箱信息
     * @author 李帅
     * @created 2018年7月24日 下午4:23:13
     * @since 
     * @param tenantId
     * @return
     */
    TenantMailInfo getTenantMailInfo(Long appId);
    
    /**
     * 
     * 描述：添加APP证书信息
     * @author 李帅
     * @created 2018年7月24日 下午8:14:35
     * @since 
     * @param tenantMailInfo
     */
    void addTenantMailInfo(TenantMailInfo tenantMailInfo);
    
    /**
     * 
     * 描述：修改APP证书信息
     * @author 李帅
     * @created 2018年7月24日 下午8:14:43
     * @since 
     * @param tenantMailInfo
     */
    void updateTenantMailInfo(TenantMailInfo tenantMailInfo);
}
