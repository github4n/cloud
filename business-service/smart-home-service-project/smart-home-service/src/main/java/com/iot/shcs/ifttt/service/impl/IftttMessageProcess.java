package com.iot.shcs.ifttt.service.impl;

import com.alibaba.fastjson.JSON;
import com.iot.common.mq.process.AbsMessageProcess;
import com.iot.ifttt.vo.ActionMessage;
import com.iot.shcs.device.service.impl.DeviceMQTTService;
import com.iot.shcs.scene.service.SceneService;
import com.iot.shcs.scene.service.impl.SceneMQTTService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 描述：联动消息执行逻辑
 * 创建人： LaiGuiMing
 * 创建时间： 2018/9/30 14:21
 */
@Slf4j
@Component
public class IftttMessageProcess extends AbsMessageProcess<ActionMessage> {

    @Autowired
    private SceneService sceneService;
    @Autowired
    private DeviceMQTTService deviceMQTTService;

    public void processMessage(ActionMessage message) {
        //只接受2C消息
        String route = message.getRoute();
        if (!"2C".equals(route)) {
            return;
        }

        log.info("receive skill message:{}", JSON.toJSONString(message));
        //执行 设备/情景
        Map<String, Object> map = JSON.parseObject(message.getMessage(), Map.class);
        String thenType = String.valueOf(map.get("thenType"));
        String id = String.valueOf(map.get("id"));
        if (thenType.equals("dev")) {
            Map<String, Object> attr = (Map<String, Object>) map.get("attr");
            //属性值排序
            HashMap<String, Object> attrMap = new LinkedHashMap();
            if (attr != null) {
                if (attr.containsKey("OnOff")) {
                    //有OnOff,重新排序
                    attrMap.put("OnOff", attr.get("OnOff"));
                    attr.remove("OnOff");
                }
                for (Map.Entry<String, Object> entry : attr.entrySet()) {
                    attrMap.put(entry.getKey(), entry.getValue());
                }
            }
            //执行控制设备
            deviceMQTTService.setDevAttr(id, attrMap);
        } else if (thenType.equals("scene")) {
            //执行scene
            sceneService.excScene(Long.parseLong(id), null, message.getTenantId());
        }
    }
}
