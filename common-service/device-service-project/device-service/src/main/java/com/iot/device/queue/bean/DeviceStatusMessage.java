package com.iot.device.queue.bean;

import com.iot.common.mq.bean.BaseMessageEntity;
import com.iot.device.model.DeviceStatus;
import com.iot.device.vo.req.device.UpdateDeviceStatusReq;
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
public class DeviceStatusMessage extends BaseMessageEntity implements Serializable {
    //外部直接组装好进行更新
    private List<DeviceStatus> deviceStatuses;
}