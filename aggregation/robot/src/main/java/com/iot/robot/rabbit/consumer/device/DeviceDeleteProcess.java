package com.iot.robot.rabbit.consumer.device;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.iot.common.beans.CommonResponse;
import com.iot.common.helper.ApplicationContextHelper;
import com.iot.common.mq.process.AbsMessageProcess;
import com.iot.robot.service.CommonService;
import com.iot.robot.utils.alexa.ChangeStateUtil;
import com.iot.robot.vo.alexa.AlexaDeleteReport;
import com.iot.shcs.device.queue.bean.DeviceDeleteMessage;
import com.iot.user.api.SmartTokenApi;
import com.iot.user.api.UserApi;
import com.iot.user.constant.SmartHomeConstants;
import com.iot.user.vo.SmartTokenResp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

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
    private UserApi userApi;
    @Autowired
    private SmartTokenApi smartTokenApi;
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

            List<SmartTokenResp> smartTokenList = smartTokenApi.findSmartTokenListByUserId(userId);
            if (CollectionUtils.isEmpty(smartTokenList)) {
                log.debug("***** DeviceDeleteProcess, smartTokenList is empty.");
                return;
            }

            for (SmartTokenResp smartToken : smartTokenList) {
                if (smartToken == null) {
                    log.debug("***** DeviceDeleteProcess, current smartToken is null");
                    continue;
                }

                Integer smartType = smartToken.getSmart();
                if (smartType == null) {
                    log.debug("***** DeviceDeleteProcess, current smartType is null, smartToken={}", JSON.toJSONString(smartToken));
                    continue;
                }

                try {
                    if (commonService.deviceFunctionIsEmpty(deviceId)) {
                        log.debug("***** DeviceDeleteProcess, deviceId={} 的功能点 is empty, end notify third party", deviceId);
                        return;
                    }

                    if (smartType == SmartHomeConstants.ALEXA) {
                        // 处理 alexa 删除通知
                        handleDeviceDeleteAlexa(userId, deviceId, smartToken);
                    } else if (smartType == SmartHomeConstants.GOOGLE_HOME) {
                        // 处理 googleHome 删除通知

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleDeviceDeleteAlexa(Long userId, String deviceId, SmartTokenResp smartToken) {
        log.debug("***** handleAlexa, alexa delete device notify start.");

        AlexaDeleteReport deleteReport = new AlexaDeleteReport();
        deleteReport.setToken(smartToken.getAccessToken());
        deleteReport.addEndpoint(deviceId);

        String jsonResult = JSONObject.toJSONString(deleteReport);

        log.debug("***** handleAlexa, deleteReport.json={}", jsonResult);

        CommonResponse commonResponse = ChangeStateUtil.send(smartToken.getAccessToken(), jsonResult);
        commonService.dealAlexaReportResult(userId, commonResponse);

        log.debug("***** handleAlexa delete device notify end.");
    }
}
