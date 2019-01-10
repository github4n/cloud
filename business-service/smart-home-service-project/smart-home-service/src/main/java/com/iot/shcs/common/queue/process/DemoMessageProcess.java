package com.iot.shcs.common.queue.process;

import com.alibaba.fastjson.JSON;
import com.iot.common.mq.process.AbsMessageProcess;
import com.iot.shcs.common.queue.bean.DemoMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 9:34 2018/8/16
 * @Modify by:
 */
@Slf4j
public class DemoMessageProcess extends AbsMessageProcess<DemoMessage> {

    public void processMessage(DemoMessage message) {
        log.info("receive skill message:{}", JSON.toJSONString(message));

    }
}
