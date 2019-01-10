package com.iot.robot.rabbit.consumer.scene;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.iot.common.beans.CommonResponse;
import com.iot.common.mq.process.AbsMessageProcess;
import com.iot.robot.service.CommonService;
import com.iot.robot.utils.alexa.ChangeStateUtil;
import com.iot.robot.vo.alexa.AlexaDeleteReport;
import com.iot.shcs.voicebox.queue.bean.SceneMessage;
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
 * @Descrpiton: 执行情景响应 处理器
 * @Author: yuChangXing
 * @Date: 2018/10/13 14:06
 * @Modify by:
 */

@Slf4j
@Component
public class DeleteSceneProcess extends AbsMessageProcess<SceneMessage> {
    @Autowired
    private UserApi userApi;
    @Autowired
    private SmartTokenApi smartTokenApi;
    @Autowired
    private CommonService commonService;

    @Override
    public void processMessage(SceneMessage message) {
        log.debug("***** DeleteSceneProcess, message={}", JSON.toJSONString(message));

        try {
            Long sceneId = message.getSceneId();
            Long userId = message.getUserId();

            if (sceneId == null || userId == null) {
                return;
            }

            List<SmartTokenResp> smartTokenList = smartTokenApi.findSmartTokenListByUserId(userId);
            if (CollectionUtils.isEmpty(smartTokenList)) {
                log.debug("***** DeleteSceneProcess, smartTokenList is empty.");
                return;
            }

            for (SmartTokenResp smartToken : smartTokenList) {
                if (smartToken == null) {
                    log.debug("***** DeleteSceneProcess, current smartToken is null");
                    continue;
                }

                Integer smartType = smartToken.getSmart();
                if (smartType == null) {
                    log.debug("***** DeleteSceneProcess, current smartType is null, smartToken={}", JSON.toJSONString(smartToken));
                    continue;
                }

                try {
                    if (smartType == SmartHomeConstants.ALEXA) {
                        // 处理 alexa 删除通知
                        handleDeleteSceneAlexa(userId, sceneId);
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

    private void handleDeleteSceneAlexa(Long userId, Long sceneId) {
        SmartTokenResp alexaToken = smartTokenApi.getAlexaSmartTokenByUserId(userId);
        if (alexaToken != null) {
            log.debug("***** handleDeleteSceneAlexa, alexa delete scene notify start.");

            AlexaDeleteReport deleteReport = new AlexaDeleteReport();
            deleteReport.setToken(alexaToken.getAccessToken());
            deleteReport.addEndpoint(String.valueOf(sceneId));

            String jsonResult = JSONObject.toJSONString(deleteReport);

            log.debug("***** handleDeleteSceneAlexa, deleteReport.json={}", jsonResult);

            CommonResponse commonResponse = ChangeStateUtil.send(alexaToken.getAccessToken(), jsonResult);
            commonService.dealAlexaReportResult(userId, commonResponse);

            log.debug("***** handleDeleteSceneAlexa, delete scene notify end.");
        }
    }
}
