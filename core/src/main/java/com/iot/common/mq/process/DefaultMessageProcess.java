package com.iot.common.mq.process;

import com.iot.common.mq.bean.BaseMessageEntity;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 13:53 2018/9/3
 * @Modify by:
 */
@Slf4j
public class DefaultMessageProcess extends AbsMessageProcess {


    public void processMessage(BaseMessageEntity message) {
        log.debug("process default message.");
    }
}
