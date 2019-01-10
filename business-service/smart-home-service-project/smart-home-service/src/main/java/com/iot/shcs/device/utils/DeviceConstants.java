package com.iot.shcs.device.utils;

import com.iot.shcs.helper.Constants;

import java.text.SimpleDateFormat;

/**
 * @Author: xfz @Descrpiton: @Date: 9:43 2018/6/13 @Modify by:
 */
public interface DeviceConstants {

    int QOS = 1;

    String TOPIC_CLIENT_PREFIX = "iot/v1/c/";

    SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    String DISCONNECTED = "0";

    String CONNECTED = "1";

    String DEFAULT_SPACE = "0";

    int ENABLE = 1;

    int DISABLE = 0;

    static String buildTopic(String sendTopicId, String topicMethod) {
        return TOPIC_CLIENT_PREFIX + sendTopicId + "/device/" + topicMethod;
    }

    public static String buildUserTopic(String sendTopicId, String topicMethod) {
        return Constants.TOPIC_CLIENT_PREFIX + sendTopicId + "/user/" + topicMethod;
    }
}
