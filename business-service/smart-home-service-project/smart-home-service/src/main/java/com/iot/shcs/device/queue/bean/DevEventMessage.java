package com.iot.shcs.device.queue.bean;

import com.iot.common.mq.bean.BaseMessageEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @description:
 * @program: cloud
 * @return:
 * @author: chq
 * @date: 2018/10/18 17:19
 **/
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class DevEventMessage extends BaseMessageEntity implements Serializable {

    private Long tenantId;

    private String parentDeviceId;

    private String devId;

    private String event;

    private List<Object> arguments;

    private String seq;

    private String srcAddress;


}
