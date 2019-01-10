package com.iot.message.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.iot.message.dao.MessageMapper;
import com.iot.message.entity.PushNoticeLog;
import com.iot.message.entity.PushNoticeTemplate;
import com.iot.message.entity.TenantMailInfo;
import com.iot.message.service.MessageService;

@Service("messageService")
public class MessageServiceImpl implements MessageService {


    @Autowired
    private MessageMapper messageMapper;

    /**
     * 
     * 描述：添加推送日志
     * @author 李帅
     * @created 2018年3月12日 下午5:36:56
     * @since 
     * @param pushNoticeLog
     */
    @Override
	public void addPushNoticeLog(PushNoticeLog pushNoticeLog) {
		savePushNoticeLog(pushNoticeLog);
	}
    
    /**
     * 
     * 描述：异步保存推送日志
     * @author 李帅
     * @created 2018年3月15日 下午4:28:26
     * @since 
     * @param pushNoticeLog
     */
	public void savePushNoticeLog( final PushNoticeLog pushNoticeLog) {
		new Thread() {
			public void run() {
				try {
					messageMapper.addPushNoticeLog(pushNoticeLog);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
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
    @Override
    public PageInfo<PushNoticeLog> getPushNoticeLog(Integer pageNum, Integer pageSize, PushNoticeLog pushNoticeLog) {
        if (null == pageNum) {
            pageNum = 0;
        }
        if (null == pageSize || 0 == pageSize) {
            pageSize=10;
        }
        PageHelper.startPage(pageNum,pageSize,true);
        List<PushNoticeLog> listMessage= null;
        listMessage = messageMapper.getPushNoticeLog(pushNoticeLog);
        PageInfo<PushNoticeLog> pageInfo = new PageInfo<>(listMessage);
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
    @Override
	public void addPushNoticeTemplate(PushNoticeTemplate pushNoticeTemplate) {
		messageMapper.addPushNoticeTemplate(pushNoticeTemplate);
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
    @Override
    public PageInfo<PushNoticeTemplate> getPushNoticeTemplate(Integer pageNum, Integer pageSize, PushNoticeTemplate pushNoticeTemplate) {
        if (null == pageNum) {
            pageNum = 0;
        }
        if (null == pageSize || 0 == pageSize) {
            pageSize=10;
        }
        PageHelper.startPage(pageNum,pageSize,true);
        List<PushNoticeTemplate> listMessage= null;
        listMessage = messageMapper.getPushNoticeTemplate(pushNoticeTemplate);
        PageInfo<PushNoticeTemplate> pageInfo = new PageInfo<PushNoticeTemplate>(listMessage);
        return pageInfo;
    }
    
    /**
     * 
     * 描述：通过推送模板名称查询推送模板
     * @author 李帅
     * @created 2018年3月17日 下午2:24:17
     * @since 
     * @param templateName
     * @return
     */
    @Override
    public PushNoticeTemplate getPushNoticeTemplateById(Long tenantId, String templateId, String templateType) {
    	return messageMapper.getPushNoticeTemplateById(tenantId, templateId, templateType);
    }
    
    /**
     * 
     * 描述：查询租户邮箱信息
     * @author 李帅
     * @created 2018年7月24日 下午4:23:06
     * @since 
     * @param tenantId
     * @return
     */
    @Override
    public TenantMailInfo getTenantMailInfo(Long appId){
    	return messageMapper.getTenantMailInfo(appId);
    }
    
    /**
     * 
     * 描述：添加APP证书信息
     * @author 李帅
     * @created 2018年7月24日 下午8:14:52
     * @since 
     * @param tenantMailInfo
     */
    @Override
    public void addTenantMailInfo(TenantMailInfo tenantMailInfo) {
    	messageMapper.addTenantMailInfo(tenantMailInfo);
    }
    
    /**
     * 
     * 描述：修改APP证书信息
     * @author 李帅
     * @created 2018年7月24日 下午8:15:00
     * @since 
     * @param tenantMailInfo
     */
    @Override
    public void updateTenantMailInfo(TenantMailInfo tenantMailInfo) {
    	messageMapper.updateTenantMailInfo(tenantMailInfo);
    }
}
