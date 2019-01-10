package com.iot.robot.rabbit.consumer.device;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.api.client.util.Maps;
import com.iot.common.beans.CommonResponse;
import com.iot.common.mq.process.AbsMessageProcess;
import com.iot.device.api.DataPointApi;
import com.iot.device.api.DeviceTypeCoreApi;
import com.iot.device.api.ProductCoreApi;
import com.iot.device.vo.rsp.ServiceModulePropertyResp;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import com.iot.device.vo.rsp.device.GetDeviceTypeInfoRespVo;
import com.iot.device.vo.rsp.device.GetProductInfoRespVo;
import com.iot.robot.common.constant.AlexaDeviceDefaultVal;
import com.iot.robot.service.CommonService;
import com.iot.robot.transform.AbstractTransfor;
import com.iot.robot.utils.alexa.ChangeStateUtil;
import com.iot.robot.vo.DeviceInfo;
import com.iot.robot.vo.SmartVoiceBoxNotifyVo;
import com.iot.robot.vo.alexa.AlexaAddOrUpdateReport;
import com.iot.robot.vo.alexa.AlexaEndpoint;
import com.iot.saas.SaaSContextHolder;
import com.iot.shcs.device.api.DeviceCoreServiceApi;
import com.iot.shcs.device.queue.bean.DeviceAddOrUpdateMessage;
import com.iot.user.api.SmartTokenApi;
import com.iot.user.api.UserApi;
import com.iot.user.constant.SmartHomeConstants;
import com.iot.user.vo.FetchUserResp;
import com.iot.user.vo.SmartTokenResp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Descrpiton: 设备新增、更新 处理器
 * @Author: yuChangXing
 * @Date: 2018/10/13 14:06
 * @Modify by:
 */

@Slf4j
@Component
public class DeviceAddOrUpdateProcess extends AbsMessageProcess<DeviceAddOrUpdateMessage> {
    @Autowired
    private UserApi userApi;
    @Autowired
    private SmartTokenApi smartTokenApi;
    @Autowired
    private DeviceCoreServiceApi deviceCoreServiceApi;
    @Autowired
    private DataPointApi dataPointApi;
    @Autowired
    private CommonService commonService;
    @Autowired
    private DeviceTypeCoreApi deviceTypeCoreApi;
    @Autowired
    private ProductCoreApi productCoreApi;
    @Autowired
    private DeviceCommonProcess deviceProcess;
    @Resource(name="alexaTransfor")
    private AbstractTransfor alexaTransfor;

    @Override
    public void processMessage(DeviceAddOrUpdateMessage message) {
        log.debug("***** DeviceAddOrUpdateProcess, message={}", JSON.toJSONString(message));

        try {
            String deviceId = message.getDeviceId();
            Long userId = message.getUserId();
            boolean newAdd = message.isNewAdd();

            List<SmartTokenResp> smartTokenList = smartTokenApi.findSmartTokenListByUserId(userId);
            if (CollectionUtils.isEmpty(smartTokenList)) {
                log.debug("***** DeviceAddOrUpdateProcess, smartTokenList is empty.");
                return;
            }

            for (SmartTokenResp smartToken : smartTokenList) {
                if (smartToken == null) {
                    log.debug("***** DeviceAddOrUpdateProcess, current smartToken is null");
                    continue;
                }

                Integer smartType = smartToken.getSmart();
                if (smartType == null) {
                    log.debug("***** DeviceAddOrUpdateProcess, current smartType is null, smartToken={}", JSON.toJSONString(smartToken));
                    continue;
                }

                try {
                    //List<DeviceFunResp> deviceFunRespList = dataPointApi.findDataPointListByDeviceId(deviceId);
                    List<ServiceModulePropertyResp> serviceModulePropertyList = commonService.findPropertyListByDeviceId(deviceId);
                    if (CollectionUtils.isEmpty(serviceModulePropertyList)) {
                        log.debug("***** DeviceAddOrUpdateProcess, deviceId={} 的功能点 is empty, end notify third party", deviceId);
                        return;
                    }

                    FetchUserResp fetchUserResp = userApi.getUser(userId);
                    Long tenantId = fetchUserResp.getTenantId();
                    SaaSContextHolder.setCurrentTenantId(tenantId);
                    log.info("***** DeviceAddOrUpdateProcess, tenantId:{}", tenantId);

                    if (smartType == SmartHomeConstants.ALEXA) {
                        // 处理 alexa 设备 新增、更新 通知
                        handleAddOrUpdateAlexa(userId, deviceId, serviceModulePropertyList, newAdd, smartToken);
                    } else if (smartType == SmartHomeConstants.GOOGLE_HOME) {
                        // 处理 googleHome 设备 新增、更新 通知

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleAddOrUpdateAlexa(Long userId, String deviceId, List<ServiceModulePropertyResp> serviceModulePropertyList,
                                        boolean newAdd, SmartTokenResp smartToken) {
        log.debug("***** handleAddOrUpdateAlexa, alexa addOrUpdate device notify start, newAdd={}", newAdd);

        GetDeviceInfoRespVo deviceResp = deviceCoreServiceApi.getDeviceInfoByDeviceId(deviceId);
        GetProductInfoRespVo productResp = productCoreApi.getByProductId(deviceResp.getProductId());
        GetDeviceTypeInfoRespVo deviceTypeResp = deviceTypeCoreApi.get(productResp.getDeviceTypeId());

        DeviceInfo device = new DeviceInfo();
        device.setServiceModulePropertyList(serviceModulePropertyList);
        device.setDeviceId(deviceId);
        device.setDeviceName(deviceResp.getName());
        device.setProductId(deviceResp.getProductId());
        device.setTenantId(deviceResp.getTenantId());
        device.setIsDirectDevice(deviceResp.getIsDirectDevice());
        device.setDeviceType(deviceTypeResp.getType());
        device.setDeviceTypeId(deviceTypeResp.getId());

        AlexaEndpoint alexaEndpoint = (AlexaEndpoint)alexaTransfor.deviceHandle(device);

        AlexaAddOrUpdateReport addOrUpdateReport = new AlexaAddOrUpdateReport();
        addOrUpdateReport.setToken(smartToken.getAccessToken());
        addOrUpdateReport.addEndpoint(alexaEndpoint);

        String jsonResult = JSONObject.toJSONString(addOrUpdateReport);

        log.debug("***** handleAddOrUpdateAlexa, addOrUpdateReport.json={}", jsonResult);

        CommonResponse commonResponse = ChangeStateUtil.send(smartToken.getAccessToken(), jsonResult);
        commonService.dealAlexaReportResult(userId, commonResponse);

        log.debug("***** handleAddOrUpdateAlexa addOrUpdate device notify end.");

        if (newAdd) {
            // 新增加设备
            if (jsonResult.contains(AlexaDeviceDefaultVal.POWER_STATE.getSupportName())) {
                Map<String, Object> attrMap = Maps.newHashMap();
                attrMap.put(AlexaDeviceDefaultVal.POWER_STATE.getKey(), AlexaDeviceDefaultVal.POWER_STATE.getVal());

                log.debug("***** handleAddOrUpdateAlexa, addOrUpdate device notify end. notify device state to default value, attrMap={}", attrMap);

                SmartVoiceBoxNotifyVo notifyVo = SmartVoiceBoxNotifyVo.getInstance()
                        .setTenantId(deviceResp.getTenantId())
                        .setAttrMap(attrMap)
                        .setOnlineStatus(null)
                        .setDeviceId(deviceId)
                        .setUserId(userId)
                        .setSmartType(SmartHomeConstants.ALEXA);
                deviceProcess.processDeviceStatusChange(notifyVo);
            }
        }
    }
}
