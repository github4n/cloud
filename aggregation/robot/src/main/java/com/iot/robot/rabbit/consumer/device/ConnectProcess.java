package com.iot.robot.rabbit.consumer.device;

import com.iot.common.helper.ApplicationContextHelper;
import com.iot.common.mq.process.AbsMessageProcess;
import com.iot.robot.vo.SmartVoiceBoxNotifyVo;
import com.iot.shcs.device.queue.bean.DeviceConnectMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Descrpiton: 网关上线 处理器
 * @Author: yuChangXing
 * @Date: 2018/10/13 14:06
 * @Modify by:
 */

@Slf4j
@Component
public class ConnectProcess extends AbsMessageProcess<DeviceConnectMessage> {
    @Autowired
    private DeviceCommonProcess deviceProcess;

    @Override
    public void processMessage(DeviceConnectMessage message) {
        log.info("ConnectProcess------>>>>>>>>");
        String deviceUUID = message.getDeviceId();
        Integer onlineStatus = 1;

        try {
            SmartVoiceBoxNotifyVo notifyVo = SmartVoiceBoxNotifyVo.getInstance()
                    .setTenantId(message.getTenantId())
                    .setOnlineStatus(onlineStatus)
                    .setDeviceId(deviceUUID);
            deviceProcess.processDeviceStatusChange(notifyVo);
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("***** ConnectProcess, processMessage, error.");
        }
    }
}
