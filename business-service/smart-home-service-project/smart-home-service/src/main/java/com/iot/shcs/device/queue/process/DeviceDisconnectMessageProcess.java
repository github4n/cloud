package com.iot.shcs.device.queue.process;

import com.google.common.collect.Lists;
import com.iot.common.mq.process.AbsMessageProcess;
import com.iot.control.activity.api.OnlineStatusRecordApi;
import com.iot.control.activity.vo.req.OnlineStatusRecordReq;
import com.iot.control.device.vo.resp.ListUserDeviceInfoRespVo;
import com.iot.device.api.DeviceStatusCoreApi;
import com.iot.device.vo.req.device.UpdateDeviceStatusReq;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import com.iot.device.vo.rsp.device.GetDeviceStatusInfoRespVo;
import com.iot.device.vo.rsp.device.ListDeviceInfoRespVo;
import com.iot.device.vo.rsp.device.ListDeviceStatusRespVo;
import com.iot.shcs.contants.AppPushMessageKey;
import com.iot.shcs.device.enums.OnlineStatusEnum;
import com.iot.shcs.device.enums.OnlineTypeEnum;
import com.iot.shcs.device.queue.bean.DeviceDisconnectMessage;
import com.iot.shcs.device.service.impl.DeviceCoreService;
import com.iot.shcs.device.service.impl.PushMessageService;
import com.iot.user.api.UserApi;
import com.iot.user.vo.FetchUserResp;
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
public class DeviceDisconnectMessageProcess extends AbsMessageProcess<DeviceDisconnectMessage> {

    @Autowired
    private DeviceCoreService deviceCoreService;

    @Autowired
    private DeviceStatusCoreApi deviceStatusCoreApi;

    @Autowired
    private PushMessageService pushMessageService;

    @Autowired
    private OnlineStatusRecordApi recordService;

    @Autowired
    private UserApi userApi;


    public void processMessage(DeviceDisconnectMessage message) {
        log.debug("processDisconnect-receive data:{}", message.toString());
        Date start = new Date();

        log.info("DeviceDisconnectMessageProcess:start:{}", start.getTime());

        Long tenantId = message.getTenantId();
        List<String> deviceIds = Lists.newArrayList();
        deviceIds.add(message.getDeviceId());
        List<UpdateDeviceStatusReq> deviceStatusReqList = Lists.newArrayList();
        //直连设备离线

        List<ListDeviceInfoRespVo> unDirects = deviceCoreService.listDevicesByParentId(message.getDeviceId());
        if (!CollectionUtils.isEmpty(unDirects)) {

            for (ListDeviceInfoRespVo device : unDirects) {
                deviceIds.add(device.getUuid());
            }
        }
        //批量更新 下线
        List<ListDeviceStatusRespVo> resultChildDeviceList = deviceCoreService.listDeviceStatusByDeviceIds(tenantId, deviceIds);
        if (!CollectionUtils.isEmpty(resultChildDeviceList)) {
            resultChildDeviceList.forEach(deviceStatus ->{
                if (deviceStatus == null || (!OnlineStatusEnum.DISCONNECTED.getCode().equalsIgnoreCase(deviceStatus.getOnlineStatus()))) {
                    //在线需要更新成非在线
                    UpdateDeviceStatusReq updateDeviceStatusParam = UpdateDeviceStatusReq.builder()
                            .deviceId(deviceStatus.getDeviceId()).tenantId(tenantId).onlineStatus(OnlineStatusEnum.DISCONNECTED.getCode())
                            .lastLoginTime(new Date())
                            .build();
                    deviceStatusReqList.add(updateDeviceStatusParam);
                }
            });
        }
        processDisconnect(deviceStatusReqList);
//        processOnlineStatusRecord(onlineStatusRecordReqList);
        processOfflineMessage(message.getTenantId(), message.getDeviceId());
        Date end = new Date();
        long consumerTime  = end.getTime() - start.getTime();
        log.info("DeviceDisconnectMessageProcess:end {}",consumerTime);
    }


    private void processOfflineMessage(Long tenantId, String deviceId) {
        try {
            GetDeviceInfoRespVo device = deviceCoreService.getDeviceInfoByDeviceId(deviceId);
            GetDeviceStatusInfoRespVo deviceStatus = deviceCoreService.getDeviceStatusByDeviceId(tenantId, deviceId);
            if (deviceStatus != null && OnlineStatusEnum.DISCONNECTED.getCode().equalsIgnoreCase(deviceStatus.getOnlineStatus())) {
                return;
            }

            List<ListUserDeviceInfoRespVo> userDeviceInfoRespVoList = deviceCoreService.listUserDevices(tenantId, deviceId);
            if (CollectionUtils.isEmpty(userDeviceInfoRespVoList)) {
                log.debug("devStatusNotif not get user by tenantId:{},deviceId:{}", tenantId, deviceId);
                return;
            }
            ListUserDeviceInfoRespVo userDevice = userDeviceInfoRespVoList.get(0);
            FetchUserResp user = userApi.getUser(userDevice.getUserId());
            if (user == null) {
                log.debug("devStatusNotif not get user by userId:{}", userDevice.getUserId());
                return;
            }
            pushMessageService.pushMessage2App(device, null, AppPushMessageKey.DEVICE_IS_OFFLINE, user.getUuid());
//            pushMessageService.pushMessage2App(device, "home", AppPushMessageKey.DEVICE_IS_OFFLINE, user.getUuid());
        } catch (Exception e) {
            log.error("devStatusNotif-processOfflineMessage({}, {},error:{})", tenantId, deviceId, e.getMessage());
        }
    }

    private void buildOnlineStatusRecord(List<OnlineStatusRecordReq> targetList, DeviceDisconnectMessage message, String deviceId) {
        OnlineStatusRecordReq onlineStatusRecord = new OnlineStatusRecordReq();
        onlineStatusRecord.setId(deviceId);
        onlineStatusRecord.setType(OnlineTypeEnum.DEVICE.getCode());
        onlineStatusRecord.setStatus(message.getStatus());
        onlineStatusRecord.setRecordTime(new Date());
        onlineStatusRecord.setTenantId(message.getTenantId());
        targetList.add(onlineStatusRecord);
    }
    private void processOnlineStatusRecord(List<OnlineStatusRecordReq> onlineStatusRecordList){

        if (!CollectionUtils.isEmpty(onlineStatusRecordList)) {
            recordService.saveBatchOnlineStatusRecord(onlineStatusRecordList);
        }
    }

    private void buildDisconnect(List<UpdateDeviceStatusReq> targetList, DeviceDisconnectMessage message, String deviceId) {
        Long tenantId = message.getTenantId();
        Long lastLoginTime = message.getLastLoginTime();
        GetDeviceStatusInfoRespVo deviceStatus = deviceCoreService.getDeviceStatusByDeviceId(tenantId, deviceId);
        if (deviceStatus == null || (!OnlineStatusEnum.DISCONNECTED.getCode().equalsIgnoreCase(deviceStatus.getOnlineStatus()))) {
            //在线需要更新成非在线
            UpdateDeviceStatusReq updateDeviceStatusParam = UpdateDeviceStatusReq.builder()
                    .deviceId(deviceId).tenantId(tenantId).onlineStatus(OnlineStatusEnum.DISCONNECTED.getCode())
                    .lastLoginTime(new Date(lastLoginTime))
                    .build();
            targetList.add(updateDeviceStatusParam);
        }

    }
    private void processDisconnect(List<UpdateDeviceStatusReq> targetList) {
        if (!CollectionUtils.isEmpty(targetList)) {
            deviceStatusCoreApi.saveOrUpdateBatch(targetList);
        }
    }
}
