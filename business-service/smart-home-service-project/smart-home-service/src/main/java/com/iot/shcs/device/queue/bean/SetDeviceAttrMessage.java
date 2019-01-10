package com.iot.shcs.device.queue.bean;

import com.iot.common.mq.bean.BaseMessageEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 15:26 2018/10/11
 * @Modify by:
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class SetDeviceAttrMessage extends BaseMessageEntity implements Serializable {
    private Map<String, Object> payload;
    private Map<String, Object> attrMap;
    private String parentDeviceId;
    private String subDeviceId;
    private Object online;
    private Long tenantId;

    public Long createTenantId() {
        return tenantId;
    }
}
