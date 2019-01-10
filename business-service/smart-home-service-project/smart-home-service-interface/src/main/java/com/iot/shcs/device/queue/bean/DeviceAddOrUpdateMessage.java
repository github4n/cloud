package com.iot.shcs.device.queue.bean;

import com.iot.common.mq.bean.BaseMessageEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

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
public class DeviceAddOrUpdateMessage extends BaseMessageEntity implements Serializable {
    boolean newAdd;
    private String deviceId;
    private Long tenantId;
    private Long userId;
}
