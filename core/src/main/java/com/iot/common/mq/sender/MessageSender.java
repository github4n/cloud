package com.iot.common.mq.sender;

import com.iot.common.mq.utils.DetailRes;
import com.iot.common.mq.utils.MessageWithTime;

public interface MessageSender {

    DetailRes send(Object message);

    DetailRes send(MessageWithTime messageWithTime);

    void close();
}