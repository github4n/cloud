package com.iot.shcs.security.queue.bean;

import com.iot.common.mq.bean.BaseMessageEntity;
import com.iot.mqttsdk.common.MqttMsg;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class GetStatusRespMessage extends BaseMessageEntity implements Serializable {
    private MqttMsg mqttMsg;
    private String topic;
    private String deviceUuid;
    private String userUuid;
    private Long tenantId;
    private Integer erroCode=200;
}
