package com.iot.widget.rabbit.consumer.device;

import com.alibaba.fastjson.JSON;
import com.iot.common.mq.process.AbsMessageProcess;
import com.iot.common.util.StringUtil;
import com.iot.shcs.device.queue.bean.DeviceDeleteMessage;
import com.iot.shcs.widget.api.UserWidgetApi;
import com.iot.shcs.widget.constant.WidgetType;
import com.iot.shcs.widget.vo.req.UserWidgetReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    private UserWidgetApi userWidgetApi;

    @Override
    public void processMessage(DeviceDeleteMessage message) {
        log.debug("***** DeviceDeleteProcess, message={}", JSON.toJSONString(message));

        try {
            String deviceId = message.getDeviceId();
            Long userId = message.getUserId();

            if (StringUtil.isBlank(deviceId) || userId == null) {
                log.error("***** DeviceDeleteProcess error, because deviceId or userId is null.");
                return;
            }

            UserWidgetReq userWidgetReq = new UserWidgetReq();
            userWidgetReq.setUserId(userId);
            userWidgetReq.setType(WidgetType.DEVICE.getType());
            userWidgetReq.setValue(String.valueOf(deviceId));
            userWidgetApi.deleteUserWidget(userWidgetReq);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("***** DeviceDeleteProcess error.");
        }
    }
}
