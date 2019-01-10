package com.iot.smarthome.rabbit.consumer;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.iot.common.mq.process.AbsMessageProcess;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import com.iot.shcs.device.api.DeviceCoreServiceApi;
import com.iot.shcs.device.queue.bean.DeviceDeleteMessage;
import com.iot.smarthome.api.DeviceClassifyApi;
import com.iot.smarthome.service.CommonService;
import com.iot.smarthome.vo.EventDeviceDeleteResp;
import com.iot.smarthome.vo.resp.DeviceClassifyResp;
import com.iot.user.api.SmartTokenApi;
import com.iot.user.api.UserApi;
import com.iot.user.vo.SmartTokenResp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Descrpiton: 设备删除 处理器
 * @Author: yuChangXing
 * @Date: 2018/10/13 14:06
 * @Modify by:
 */

@Slf4j
@Component
public class DeviceDeleteProcess extends AbsMessageProcess<DeviceDeleteMessage> {
    @Autowired
    private SmartTokenApi smartTokenApi;
    @Autowired
    private DeviceCoreServiceApi deviceCoreServiceApi;
    @Autowired
    private DeviceClassifyApi deviceClassifyApi;
    @Autowired
    private CommonService commonService;

    @Override
    public void processMessage(DeviceDeleteMessage message) {
        log.debug("***** DeviceDeleteProcess, message={}", JSON.toJSONString(message));

        try {
            String deviceId = message.getDeviceId();
            Long userId = message.getUserId();

            if (deviceId == null || userId == null) {
                return;
            }

            List<SmartTokenResp> smartTokenList = smartTokenApi.findThirdPartyInfoIdNotNull(userId);
            if (CollectionUtils.isEmpty(smartTokenList)) {
                log.debug("***** DeviceDeleteProcess, smartTokenList is empty.");
                return;
            }

            GetDeviceInfoRespVo deviceResp = deviceCoreServiceApi.getDeviceInfoByDeviceId(deviceId);
            DeviceClassifyResp deviceClassifyResp = deviceClassifyApi.getDeviceClassifyByProductId(deviceResp.getProductId());
            if (deviceClassifyResp == null) {
                log.info("DeviceDeleteProcess, deviceId={} DeviceClassifyProductXref is null, cancel notify thirdParty", deviceId);
                return ;
            }

            // 封装基础数据
            List<String> deviceIds = Lists.newArrayList();
            deviceIds.add(deviceId);

            EventDeviceDeleteResp deleteResp = new EventDeviceDeleteResp();
            deleteResp.setDeviceIds(deviceIds);

            Map<String, Object> resultMap = deleteResp.buildMsg();
            String jsonContent = JSON.toJSONString(resultMap);

            for (SmartTokenResp smartToken : smartTokenList) {
                log.debug("***** DeviceDeleteProcess, smartToken={}", JSON.toJSONString(smartToken));
                if (smartToken == null) {
                    log.info("***** DeviceDeleteProcess, current smartToken is null");
                    continue;
                }

                commonService.notifyThirdParty(smartToken.getThirdPartyInfoId(), jsonContent);
            }
        } catch (Exception e) {
            log.error("***** DeviceDeleteProcess, error.");
            e.printStackTrace();
        }
    }
}
