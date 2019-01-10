package com.iot.smarthome.rabbit.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.common.mq.process.AbsMessageProcess;
import com.iot.device.api.DataPointApi;
import com.iot.device.api.DeviceTypeCoreApi;
import com.iot.device.api.ProductCoreApi;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import com.iot.saas.SaaSContextHolder;
import com.iot.shcs.device.api.DeviceCoreServiceApi;
import com.iot.shcs.device.queue.bean.DeviceAddOrUpdateMessage;
import com.iot.smarthome.api.DeviceClassifyApi;
import com.iot.smarthome.api.ThirdPartyInfoApi;
import com.iot.smarthome.constant.EventNotifyConst;
import com.iot.smarthome.service.CommonService;
import com.iot.smarthome.util.HttpUtils;
import com.iot.smarthome.util.SignatureUtil;
import com.iot.smarthome.vo.DeviceInfo;
import com.iot.smarthome.vo.EventDeviceAddOrUpdateResp;
import com.iot.smarthome.vo.SignHashMap;
import com.iot.smarthome.vo.resp.DeviceClassifyResp;
import com.iot.smarthome.vo.resp.ThirdPartyInfoResp;
import com.iot.user.api.SmartTokenApi;
import com.iot.user.api.UserApi;
import com.iot.user.vo.FetchUserResp;
import com.iot.user.vo.SmartTokenResp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    private CommonService commonService;
    @Autowired
    private SmartTokenApi smartTokenApi;
    @Autowired
    private DeviceCoreServiceApi deviceCoreServiceApi;
    @Autowired
    private DeviceClassifyApi deviceClassifyApi;

    @Override
    public void processMessage(DeviceAddOrUpdateMessage message) {
        log.info("***** DeviceAddOrUpdateProcess, message={}", JSON.toJSONString(message));

        try {
            String deviceId = message.getDeviceId();
            Long userId = message.getUserId();
            boolean newAdd = message.isNewAdd();

            List<SmartTokenResp> smartTokenList = smartTokenApi.findThirdPartyInfoIdNotNull(userId);
            if (CollectionUtils.isEmpty(smartTokenList)) {
                log.debug("***** DeviceAddOrUpdateProcess, smartTokenList is empty.");
                return;
            }

            GetDeviceInfoRespVo deviceResp = deviceCoreServiceApi.getDeviceInfoByDeviceId(deviceId);
            DeviceClassifyResp deviceClassifyResp = deviceClassifyApi.getDeviceClassifyByProductId(deviceResp.getProductId());
            if (deviceClassifyResp == null) {
                log.info("DeviceAddOrUpdateProcess, deviceId={} DeviceClassifyProductXref is null, cancel notify thirdParty", deviceId);
                return ;
            }

            // 封装基础数据
            DeviceInfo devInfo = new DeviceInfo();
            devInfo.setDeviceId(deviceResp.getUuid());
            devInfo.setName(deviceResp.getName());
            devInfo.setDeviceVersion(deviceResp.getVersion());
            devInfo.setType(deviceClassifyResp.getTypeCode());

            List<DeviceInfo> deviceInfoList = Lists.newArrayList();
            deviceInfoList.add(devInfo);

            EventDeviceAddOrUpdateResp eventDeviceAddOrUpdateResp = new EventDeviceAddOrUpdateResp();
            eventDeviceAddOrUpdateResp.setDeviceList(deviceInfoList);

            Map<String, Object> resultMap = eventDeviceAddOrUpdateResp.buildMsg();
            String jsonContent = JSON.toJSONString(resultMap);

            for (SmartTokenResp smartToken : smartTokenList) {
                log.debug("***** DeviceAddOrUpdateProcess, smartToken={}", JSON.toJSONString(smartToken));
                if (smartToken == null) {
                    log.info("***** DeviceAddOrUpdateProcess, current smartToken is null");
                    continue;
                }
                commonService.notifyThirdParty(smartToken.getThirdPartyInfoId(), jsonContent);
            }

        } catch (Exception e) {
            log.error("***** DeviceAddOrUpdateProcess error.");
            e.printStackTrace();
        }
    }


}
