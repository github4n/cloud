package com.iot.device.queue.bean;

import com.iot.common.mq.bean.BaseMessageEntity;
import com.iot.device.vo.req.device.UpdateDeviceStateReq;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class DeviceStateMessage extends BaseMessageEntity implements Serializable {

    List<UpdateDeviceStateReq> params;
}