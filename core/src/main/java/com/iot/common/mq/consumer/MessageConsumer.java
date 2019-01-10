package com.iot.common.mq.consumer;

import com.iot.common.mq.utils.DetailRes;

/**
 * Created by littlersmall on 16/5/12.
 */
public interface MessageConsumer {
    DetailRes consume();

    void close();

}