package com.iot.robot.rabbit.consumer.scene;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.iot.common.beans.CommonResponse;
import com.iot.common.mq.process.AbsMessageProcess;
import com.iot.control.scene.api.SceneApi;
import com.iot.control.scene.vo.req.SceneReq;
import com.iot.control.scene.vo.rsp.SceneResp;
import com.iot.robot.service.CommonService;
import com.iot.robot.transform.AbstractTransfor;
import com.iot.robot.utils.alexa.ChangeStateUtil;
import com.iot.robot.vo.alexa.AlexaAddOrUpdateReport;
import com.iot.robot.vo.alexa.AlexaEndpoint;
import com.iot.shcs.voicebox.queue.bean.SceneMessage;
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

/**
 * @Descrpiton: 执行情景响应 处理器
 * @Author: yuChangXing
 * @Date: 2018/10/13 14:06
 * @Modify by:
 */

@Slf4j
@Component
public class AddOrUpdateSceneProcess extends AbsMessageProcess<SceneMessage> {
    @Autowired
    private UserApi userApi;
    @Autowired
    private SmartTokenApi smartTokenApi;
    @Autowired
    private SceneApi sceneApi;
    @Autowired
    private CommonService commonService;
    @Resource(name = "alexaTransfor")
    private AbstractTransfor alexaTransfor;

    @Override
    public void processMessage(SceneMessage message) {
        log.debug("***** AddOrUpdateSceneProcess, message={}", JSON.toJSONString(message));

        try {
            Long sceneId = message.getSceneId();
            Long userId = message.getUserId();

            if (sceneId == null || userId == null) {
                return;
            }

            List<SmartTokenResp> smartTokenList = smartTokenApi.findSmartTokenListByUserId(userId);
            if (CollectionUtils.isEmpty(smartTokenList)) {
                log.debug("***** AddOrUpdateSceneProcess, smartTokenList is empty.");
                return;
            }

            for (SmartTokenResp smartToken : smartTokenList) {
                if (smartToken == null) {
                    log.debug("***** AddOrUpdateSceneProcess, current smartToken is null");
                    continue;
                }

                Integer smartType = smartToken.getSmart();
                if (smartType == null) {
                    log.debug("***** AddOrUpdateSceneProcess, current smartType is null, smartToken={}", JSON.toJSONString(smartToken));
                    continue;
                }

                try {
                    if (smartType == SmartHomeConstants.ALEXA) {
                        // 处理 alexa sceneId 新增、更新 通知
                        handleAddOrUpdateSceneAlexa(userId, sceneId);
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

    private void handleAddOrUpdateSceneAlexa(Long userId, Long sceneId) {
        SmartTokenResp alexaToken = smartTokenApi.getAlexaSmartTokenByUserId(userId);
        if (alexaToken != null) {
            log.debug("***** handleAddOrUpdateSceneAlexa, scene addOrUpdate notify start.");

            FetchUserResp userResp = userApi.getUser(userId);

            SceneReq sceneReq = new SceneReq();
            sceneReq.setId(sceneId);
            sceneReq.setTenantId(userResp.getTenantId());
            SceneResp sceneResp = sceneApi.sceneById(sceneReq);

            AlexaEndpoint alexaEndpoint = (AlexaEndpoint) alexaTransfor.sceneHandle(sceneResp);

            AlexaAddOrUpdateReport addOrUpdateReport = new AlexaAddOrUpdateReport();
            addOrUpdateReport.setToken(alexaToken.getAccessToken());
            addOrUpdateReport.addEndpoint(alexaEndpoint);

            String jsonResult = JSONObject.toJSONString(addOrUpdateReport);

            log.debug("***** handleAddOrUpdateSceneAlexa, addOrUpdateReport.json={}", jsonResult);

            CommonResponse commonResponse = ChangeStateUtil.send(alexaToken.getAccessToken(), jsonResult);
            commonService.dealAlexaReportResult(userId, commonResponse);

            log.debug("***** handleAddOrUpdateSceneAlexa, scene addOrUpdate notify end.");
        }
    }
}
