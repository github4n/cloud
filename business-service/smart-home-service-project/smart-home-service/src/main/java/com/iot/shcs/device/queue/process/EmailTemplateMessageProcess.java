package com.iot.shcs.device.queue.process;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.iot.common.mq.process.AbsMessageProcess;
import com.iot.message.api.MessageApi;
import com.iot.message.dto.MailBatchDto;
import com.iot.shcs.device.queue.bean.EmailTemplateMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author: lucky @Descrpiton: @Date: 15:34 2018/10/11 @Modify by:
 */
@Slf4j
@Component
public class EmailTemplateMessageProcess extends AbsMessageProcess<EmailTemplateMessage> {
    @Autowired
    private MessageApi messageApi;

    public void processMessage(EmailTemplateMessage message) {
        long startTime = System.currentTimeMillis();
        log.debug("消息中心接收到 邮件 发送请求-> 信息体：{} ", JSON.toJSONString(message));
        Long appId = -1L;//立达信的租户
        Map<String, String> noticeMap = Maps.newHashMap();
        noticeMap.put("message", message.getContent());
        noticeMap.put("templateId", "EN00011");
        noticeMap.put("subject", message.getTitle());

        MailBatchDto mailBatchPush = new MailBatchDto();
        mailBatchPush.setNoticeMap(noticeMap);
        mailBatchPush.setRetryNum(1);
        mailBatchPush.setAppId(appId);
        mailBatchPush.setTos(message.getReceiveTos());
        mailBatchPush.setLangage("zh_CN");

        messageApi.mailBatchPush(mailBatchPush);

//        String[] receiveTos = message.getReceiveTos().toArray(new String[message.getReceiveTos().size()]);
//        mailService.sendSimpleMail(receiveTos, message.getTitle(), message.getContent());
        long useTime = System.currentTimeMillis() - startTime;
        log.debug("调用 {} 邮箱网关处理完毕，耗时 {}毫秒", useTime);
    }
}
