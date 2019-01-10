

package com.iot.comm.filter;

import com.iot.comm.bean.EmailMsgTemplate;
import com.iot.comm.bean.MobileMsgTemplate;
import com.iot.comm.config.MonitorPropertiesConfig;
import com.iot.comm.utils.CommConstants;
import com.iot.comm.utils.EnumSmsChannelTemplate;
import com.iot.comm.utils.MqQueueConstant;
import com.xiaoleilu.hutool.collection.CollUtil;
import de.codecentric.boot.admin.event.ClientApplicationEvent;
import de.codecentric.boot.admin.event.ClientApplicationStatusChangedEvent;
import de.codecentric.boot.admin.notify.AbstractStatusChangeNotifier;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 服务下线通知
 *
 * @author lucky
 * @date 2018/8/8
 */

public class StatusChangeNotifier extends AbstractStatusChangeNotifier {
    public static final String NORM_DATETIME_MS_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";

    private static final Logger LOGGER = LoggerFactory.getLogger(StatusChangeNotifier.class);
    private RabbitTemplate rabbitTemplate;
    private MonitorPropertiesConfig monitorMobilePropertiesConfig;

    public StatusChangeNotifier(MonitorPropertiesConfig monitorMobilePropertiesConfig, RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.monitorMobilePropertiesConfig = monitorMobilePropertiesConfig;
    }

    /**
     * 通知逻辑
     *
     * @param event 事件
     * @throws Exception 异常
     */
    @Override
    protected void doNotify(ClientApplicationEvent event) {
        if (event instanceof ClientApplicationStatusChangedEvent) {
            DateFormat dateFormat = new SimpleDateFormat(NORM_DATETIME_MS_PATTERN);
            String time = dateFormat.format(new Date(event.getTimestamp()));
            LOGGER.info("Application {} ({}) is {}", event.getApplication().getName(),
                    event.getApplication().getId(), ((ClientApplicationStatusChangedEvent) event).getTo().getStatus());
            String text = String.format("应用:%s 服务ID:%s 状态改变为：%s，时间：%s "
                    , event.getApplication().getName()
                    , event.getApplication().getId()
                    , ((ClientApplicationStatusChangedEvent) event).getTo().getStatus()
                    , time);

            JSONObject contextJson = new JSONObject();
            contextJson.put("name", event.getApplication().getName());
            contextJson.put("seid", event.getApplication().getId());
            contextJson.put("time", time);
            contextJson.put("content", text);

            //开启短信通知
            if (monitorMobilePropertiesConfig.getMobile().getEnabled()) {
                LOGGER.info("开始短信通知，内容：{}", text);
                rabbitTemplate.convertAndSend(MqQueueConstant.MOBILE_SERVICE_STATUS_CHANGE,
                        new MobileMsgTemplate(
                                CollUtil.join(monitorMobilePropertiesConfig.getMobile().getMobiles(), ","),
                                contextJson.toJSONString(),
                                CommConstants.ALIYUN_SMS,
                                EnumSmsChannelTemplate.SERVICE_STATUS_CHANGE.getSignName(),
                                EnumSmsChannelTemplate.SERVICE_STATUS_CHANGE.getTemplate()
                        ));
            }
            //开启邮箱通知
            if (monitorMobilePropertiesConfig.getEmail().getEnabled()) {
                LOGGER.info("开始email通知，内容：{}", text);
                rabbitTemplate.convertAndSend(MqQueueConstant.EMAIL_SERVICE_STATUS_CHANGE,
                        new EmailMsgTemplate(
                                monitorMobilePropertiesConfig.getEmail().getEmails(),
                                CommConstants.EMAIL_TITLE,
                                contextJson.toJSONString(),
                                CommConstants.EMAIL_SMS));
            }


        } else {
            LOGGER.info("Application {} ({}) {}", event.getApplication().getName(),
                    event.getApplication().getId(), event.getType());
        }
    }

}
