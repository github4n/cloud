package com.iot.common.mq.process;

import com.iot.common.constant.SystemConstants;
import com.iot.common.mq.bean.BaseMessageEntity;
import com.iot.common.mq.utils.DetailRes;
import com.iot.saas.SaaSContextHolder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.util.concurrent.Callable;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 9:59 2018/12/4
 * @Modify by:
 */
@AllArgsConstructor
@Slf4j
public class TaskProcess<T> implements Callable<DetailRes> {

    private T data;

    private MessageProcess<T> messageProcess;

    public DetailRes call() {
        try{
            if (data != null) {
                if( data.getClass().getSuperclass() == BaseMessageEntity.class) {

                    BaseMessageEntity baseMsg=(BaseMessageEntity) data;
                    SaaSContextHolder.setLogRequestId(baseMsg.getLogRequestId());
                    MDC.put(SystemConstants.LOG_REQUEST_ID, baseMsg.getLogRequestId());
                }
            }
            return messageProcess.process(data);
        }finally{
            SaaSContextHolder.removeCurrentContextMap();
        }
    }
}
