package com.iot.shcs.device.vo;

import com.iot.mqttsdk.common.MqttMsg;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 14:32 2018/8/10
 * @Modify by:
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class DeviceAttrVO implements Serializable {

    private MqttMsg msg;

    private String reqTopic;

}
