package com.iot.common.mq.process;

import com.iot.common.mq.utils.DetailRes;

/**
 * Created by littlersmall on 16/5/11.
 */
public interface MessageProcess<T> {
    DetailRes process(T message);
}