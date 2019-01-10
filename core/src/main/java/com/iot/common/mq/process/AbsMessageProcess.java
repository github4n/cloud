package com.iot.common.mq.process;

import com.alibaba.fastjson.JSON;
import com.iot.common.constant.SystemConstants;
import com.iot.common.mq.bean.BaseMessageEntity;
import com.iot.common.mq.utils.DetailRes;
import com.iot.common.util.StringUtil;
import com.iot.saas.SaaSContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 11:50 2018/9/3
 * @Modify by:
 */
@Slf4j
public abstract class AbsMessageProcess<T extends BaseMessageEntity> implements MessageProcess<T> {

    public DetailRes process(T message) {
        try {
            log.debug("receive process message content:{}", JSON.toJSONString(message));
            if (StringUtil.isNotEmpty(message.getLogRequestId())) {
                SaaSContextHolder.setLogRequestId(message.getLogRequestId());
                MDC.put(SystemConstants.LOG_REQUEST_ID, message.getLogRequestId());
            }
            processMessage(message);
            return new DetailRes(true, "");
        } catch (Exception e) {
            return new DetailRes(true, "process message:{" + JSON.toJSONString(message) + "} business error");
        } finally{
            SaaSContextHolder.removeCurrentContextMap();
        }
    }

    public abstract void processMessage(T message);


}
