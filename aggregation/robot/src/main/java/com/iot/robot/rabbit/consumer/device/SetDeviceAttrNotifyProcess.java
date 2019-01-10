package com.iot.robot.rabbit.consumer.device;

import com.iot.common.helper.ApplicationContextHelper;
import com.iot.common.mq.process.AbsMessageProcess;
import com.iot.robot.vo.SmartVoiceBoxNotifyVo;
import com.iot.shcs.device.queue.bean.SetDeviceAttrMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Descrpiton: 设备属性通知 处理器
 * @Author: yuChangXing
 * @Date: 2018/10/13 14:06
 * @Modify by:
 */

@Slf4j
@Component
public class SetDeviceAttrNotifyProcess extends AbsMessageProcess<SetDeviceAttrMessage> {
    @Autowired
    private DeviceCommonProcess deviceProcess;

    @Override
    public void processMessage(SetDeviceAttrMessage message) {
        log.info("SetDeviceAttrNotifyProcess------>>>>>>>>");
        try {
            String deviceUUID = message.getSubDeviceId();
            Map<String, Object> attrMap = message.getAttrMap();
            Integer onlineStatus = (Integer) message.getOnline();

            SmartVoiceBoxNotifyVo notifyVo = SmartVoiceBoxNotifyVo.getInstance()
                    .setTenantId(message.getTenantId())
                    .setAttrMap(attrMap)
                    .setOnlineStatus(onlineStatus)
                    .setDeviceId(deviceUUID);
            deviceProcess.processDeviceStatusChange(notifyVo);
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("***** SetDeviceAttrNotifyProcess, processMessage, error.");
        }
    }
}
