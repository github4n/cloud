package com.iot.shcs.device.queue.bean;

import com.iot.common.mq.bean.BaseMessageEntity;
import com.iot.mqttsdk.common.MqttMsg;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 10:07 2018/8/8
 * @Modify by:
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class DevAttrMessage extends BaseMessageEntity implements Serializable {
    private Long tenantId;
    private String reqTopic;
    private MqttMsg msg;

    @Override
    public Date getCreateTime() {
        return super.getCreateTime();
    }

    @Override
    public Long getTenantId() {
        return super.getTenantId();
    }

    @Override
    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
        super.setTenantId(tenantId);
    }
}
