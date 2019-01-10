package com.iot.shcs.device.queue.process;

import com.google.common.collect.Lists;
import com.iot.common.mq.process.AbsMessageProcess;
import com.iot.control.activity.api.OnlineStatusRecordApi;
import com.iot.control.activity.vo.req.OnlineStatusRecordReq;
import com.iot.device.vo.req.device.UpdateDeviceStatusReq;
import com.iot.device.vo.rsp.device.GetDeviceStatusInfoRespVo;
import com.iot.shcs.device.enums.OnlineStatusEnum;
import com.iot.shcs.device.enums.OnlineTypeEnum;
import com.iot.shcs.device.queue.bean.DeviceConnectMessage;
import com.iot.shcs.device.queue.bean.DeviceDisconnectMessage;
import com.iot.shcs.device.service.impl.DeviceCoreService;
import com.iot.shcs.security.service.impl.SecurityMqttService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * @Author: lucky @Descrpiton: @Date: 15:34 2018/10/11 @Modify by:
 */
@Slf4j
@Component
public class DeviceConnectMessageProcess extends AbsMessageProcess<DeviceConnectMessage> {

    @Autowired
    private DeviceCoreService deviceCoreService;
    @Autowired
    private SecurityMqttService securityMqttService;
    @Autowired
    private OnlineStatusRecordApi recordService;

    public void processMessage(DeviceConnectMessage message) {
        log.debug("DeviceConnectMessageProcess: {}", message.toString());
        Date start = new Date();
        log.info("DeviceConnectMessageProcess:start:{}", start.getTime());
        //直连设备上线
        processConnect(message, message.getDeviceId());

//        List<OnlineStatusRecordReq> targetOnlineStatusList = Lists.newArrayList();
//        buildOnlineStatusRecord(targetOnlineStatusList, message, message.getDeviceId());
//        processOnlineStatusRecord(targetOnlineStatusList);
        // 下发 安防密码给网关
        securityMqttService.setSecurityPasswdNotif(message.getTenantId(), message.getDeviceId());
        Date end = new Date();
        long consumerTime  = end.getTime() - start.getTime();
        log.info("DeviceConnectMessageProcess:end {}",consumerTime);
    }

    private void processConnect(DeviceConnectMessage message, String deviceId) {
        Long tenantId = message.getTenantId();
        Long lastLoginTime = message.getLastLoginTime();
        GetDeviceStatusInfoRespVo deviceStatus = deviceCoreService.getDeviceStatusByDeviceId(tenantId, deviceId);
        if (deviceStatus == null || (!OnlineStatusEnum.CONNECTED.getCode().equalsIgnoreCase(deviceStatus.getOnlineStatus()))) {
            //非在线需要更新成在线
            UpdateDeviceStatusReq updateDeviceStatusParam = UpdateDeviceStatusReq.builder()
                    .deviceId(deviceId).tenantId(tenantId).onlineStatus(OnlineStatusEnum.CONNECTED.getCode())
                    .lastLoginTime(new Date(lastLoginTime))
                    .build();
            deviceCoreService.saveOrUpdateDeviceStatus(updateDeviceStatusParam);
        }


    }

    private void buildOnlineStatusRecord(List<OnlineStatusRecordReq> targetList, DeviceConnectMessage message, String deviceId) {
        OnlineStatusRecordReq onlineStatusRecord = new OnlineStatusRecordReq();
        onlineStatusRecord.setId(deviceId);
        onlineStatusRecord.setType(OnlineTypeEnum.DEVICE.getCode());
        onlineStatusRecord.setStatus(message.getStatus());
        onlineStatusRecord.setRecordTime(new Date());
        onlineStatusRecord.setTenantId(message.getTenantId());
        recordService.saveOnlineStatusRecord(onlineStatusRecord);
    }
    private void processOnlineStatusRecord(List<OnlineStatusRecordReq> onlineStatusRecordList){

        if (!CollectionUtils.isEmpty(onlineStatusRecordList)) {
            recordService.saveBatchOnlineStatusRecord(onlineStatusRecordList);
        }
    }
}
