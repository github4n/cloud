package com.iot.shcs.device.queue.bean;

import com.google.common.base.Preconditions;
import com.iot.common.mq.bean.BaseMessageEntity;
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
 * @Date: 15:26 2018/10/11
 * @Modify by:
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class DeviceDeleteMessage extends BaseMessageEntity implements Serializable {

    private String deviceId;

    private Long userId;

    private Long tenantId;

}
