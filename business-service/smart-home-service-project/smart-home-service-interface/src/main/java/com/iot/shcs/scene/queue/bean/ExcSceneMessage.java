package com.iot.shcs.scene.queue.bean;

import com.iot.common.mq.bean.BaseMessageEntity;
import com.iot.mqttsdk.common.MqttMsg;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * zhangyue
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class ExcSceneMessage extends BaseMessageEntity implements Serializable {

    private MqttMsg data;

    private String topic;

    private Long tenantId;


    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }


}
