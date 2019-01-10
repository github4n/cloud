
package com.iot.comm.handler;

import com.iot.comm.bean.EmailMsgTemplate;
import com.iot.comm.service.IMailService;
import com.iot.comm.utils.ApplicationContextHelper;
import com.iot.comm.utils.CommConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * 163邮箱服务处理
 */
@Component(CommConstants.EMAIL_SMS)
public class EmailServiceMessageHandler extends AbstractEmailMessageHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceMessageHandler.class);


    /**
     * 数据校验
     *
     * @param emailMsgTemplate 消息
     */
    @Override
    public void check(EmailMsgTemplate emailMsgTemplate) {
        Assert.notEmpty(emailMsgTemplate.getReceiveEmails(), "接受者邮件不能为空");
        Assert.notNull(emailMsgTemplate.getContext(), "内容不能为空");
    }

    /**
     * 业务处理
     *
     * @param emailMsgTemplate 消息
     */
    public boolean process(EmailMsgTemplate emailMsgTemplate) {

        String[] receiveEmails = new String[emailMsgTemplate.getReceiveEmails().size()];
        emailMsgTemplate.getReceiveEmails().toArray(receiveEmails);
        IMailService mailService = ApplicationContextHelper.getBean(IMailService.class);
        if (mailService != null) {
            mailService.sendSimpleMail(
                    receiveEmails, emailMsgTemplate.getTitle(), emailMsgTemplate.getContext());
        }
        return true;
    }

    /**
     * 失败处理
     *
     * @param emailMsgTemplate 消息
     */
    public void fail(EmailMsgTemplate emailMsgTemplate) {
        LOGGER.error("邮件发送失败 ->  邮箱：{}", emailMsgTemplate.getReceiveEmails());
    }
}
