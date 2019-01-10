package com.iot.widget.rabbit.consumer.scene;

import com.alibaba.fastjson.JSON;
import com.iot.common.mq.process.AbsMessageProcess;
import com.iot.shcs.voicebox.queue.bean.SceneMessage;
import com.iot.shcs.widget.api.UserWidgetApi;
import com.iot.shcs.widget.constant.WidgetType;
import com.iot.shcs.widget.vo.req.UserWidgetReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    private UserWidgetApi userWidgetApi;

    @Override
    public void processMessage(SceneMessage message) {
        log.debug("***** DeleteSceneProcess, message={}", JSON.toJSONString(message));

        try {
            Long sceneId = message.getSceneId();
            Long userId = message.getUserId();

            if (sceneId == null || userId == null) {
                log.error("***** DeleteSceneProcess error, because sceneId or userId is null.");
                return;
            }

            UserWidgetReq userWidgetReq = new UserWidgetReq();
            userWidgetReq.setUserId(userId);
            userWidgetReq.setType(WidgetType.SCENE.getType());
            userWidgetReq.setValue(String.valueOf(sceneId));
            userWidgetApi.deleteUserWidget(userWidgetReq);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("***** DeleteSceneProcess error.");
        }
    }
}
