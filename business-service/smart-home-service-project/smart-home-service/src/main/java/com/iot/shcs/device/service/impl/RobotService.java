package com.iot.shcs.device.service.impl;

import com.iot.common.helper.ApplicationContextHelper;
import com.iot.shcs.device.queue.bean.DeviceDeleteMessage;
import com.iot.shcs.device.queue.sender.DeviceDeleteSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author lucky
 * @ClassName RobotService
 * @Description robot 服务相关
 * @date 2018/12/21 16:24
 * @Version 1.0
 */
@Slf4j
@Component
public class RobotService {

    @Autowired
    private DeviceDeleteSender deviceDeleteSender;


    public void sendDeleteBatchDevIds(Long tenantId, Long userId, List<String> devIds) {

        if(CollectionUtils.isEmpty(devIds)) {
            return;
        }
        devIds.forEach(deviceId ->{
            // 通知robot删除设备
            ApplicationContextHelper.getBean(DeviceDeleteSender.class).send(DeviceDeleteMessage.builder()
                    .deviceId(deviceId)
                    .tenantId(tenantId)
                    .userId(userId).build());
        });

    }
}
