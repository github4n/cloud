package com.iot.exception;

import com.iot.common.exception.IBusinessException;

/** 
 * 
 * 项目名称：IOT视频云
 * 模块名称：服务层
 * 功能描述：文件服务异常枚举
 * 创建人： mao2080@sina.com 
 * 创建时间：2018/3/22 15:16
 * 修改人： mao2080@sina.com 
 * 修改时间：2018/3/22 15:16
 * 修改描述：
 */
public enum BusinessExceptionEnum implements IBusinessException {

    /**未知异常*/
    UNKNOWN_EXCEPTION("messageService.video.unknow.exception"),
    
    /**推送日志为空*/
    PUSHLOG_ISNULL("messageService.push.Log.isnull"),
    
    /**添加推送日志出错*/
    ADD_PUSHNOTICE_LOG_ERROR("messageService.add.pushnotice.log.error"),
    
    /**添加推送日志出错*/
    GET_PUSHNOTICE_LOG_ERROR("messageService.get.pushnotice.log.error"),
    
    /**模板id为空*/
    TEMPLATEID_ISNULL("messageService.templateId.isnull"),
    
    /**模板信息为空*/
    TEMPLATE_INFO_ISNULL("messageService.template.info.isnull"),
    
    /**模板内容为空*/
    TEMPLATECONTENT_ISNULL("messageService.templateContent.isnull"),
    
    /**模板类型为空*/
    TEMPLATETYPE_ISNULL("messageService.templateType.isnull"),
    
    /**创建人为空*/
    CREATOR_ISNULL("messageService.creator.isnull"),
    
    /**推送模板的添加出错*/
    ADD_PUSHNOTICE_TEMPLATE_ERROR("messageService.add.pushnotice.template.error"),
    
    /**添加推送模板出错*/
    GET_PUSHNOTICE_TEMPLATE_ERROR("messageService.get.pushnotice.template.error"),
    
    /**to为空*/
    TO_ISNULL("messageService.to.isnull"),
    
    /**tenantId为空*/
    TENANTID_ISNULL("messageService.tenantId.isnull"),
    
    /**tenantMailInfo为空*/
    TENANTMAILINFO_ISNULL("messageService.tenantMailInfo.isnull"),
    
    /**tos为空*/
    TOS_ISNULL("messageService.tos.isnull"),
    
    /**noticeMap为空*/
    NOTICEMAP_ISNULL("messageService.noticeMap.isnull"),
    
    /**邮件服务-平台级单个推送出错*/
    MAIL_SINGLE_PUSH_ERROR("messageService.mail.single.push.error"),
    
    /**邮件服务-平台级批量推送出错*/
    MAIL_BATCH_PUSH_ERROR("messageService.mail.batch.push.error"),
    
    /**邮件服务-平台级单个附件邮件推送出错*/
    ANNEX_MAIL_SINGLE_PUSH_ERROR("messageService.annex.mail.single.push.error"),
    
    /**邮件服务-测试邮件推送配置出错*/
    TEST_MAIL_PUSH_ERROR("messageService.test.mail.push.config.error"),
    
    /**userPhoneNo为空*/
    USERPHONENO_ISNULL("messageService.userPhoneNo.isnull"),
    
    /**userPhoneNos为空*/
    USERPHONENOS_ISNULL("messageService.userPhoneNos.isnull"),
    
    /**短信消息级单个推送出错*/
    SMS_SINGLE_PUSH_ERROR("messageService.sms.single.push.error"),
    
    /**短信消息批量推送出错*/
    SMS_BATCH_PUSH_ERROR("messageService.sms.batch.push.error"),
    
    /**userId为空*/
    USERID_ISNULL("messageService.userId.isnull"),
    
    /**appId为空*/
    APPID_ISNULL("messageService.appId.isnull"),
    
    /**userIds为空*/
    USERIDS_ISNULL("messageService.userIds.isnull"),
    
    /**系统消息单个推送出错*/
    SYSTEM_SINGLE_PUSH_ERROR("messageService.system.single.push.error"),
    
    /**系统消息批量推送出错*/
    SYSTEM_BATCH_PUSH_ERROR("messageService.system.batch.push.error"),
    
    /**系统消息批量推送出错*/
    ADD_USER_PHONE_RELATE_ERROR("messageService.add.user.phone.relate.error"),
    
    /**phoneId为空*/
    PHONEID_ISNULL("messageService.phoneId.isnull"),
    
    /**phoneType为空*/
    PHINETYPE_ISNULL("messageService.phoneType.isnull"),
    
    /**appCertInfo为空*/
    APPCERTINFO_ISNULL("messageService.appCertInfo.isnull"),
    
    /**IOS信息太长*/
    IOS_MESSAGE_TOO_LONG("messageService.IOS.meessage.too.long."),
    
    SWITCH_ISNULL("messageService.switch.isnull"),
    ;

	private String messageKey;

    BusinessExceptionEnum(String messageKey) {
        this.messageKey = messageKey;
    }

    @Override
    public int getCode() {
        return 0;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    @Override
    public String getMessageKey() {
        return messageKey;
    }

}