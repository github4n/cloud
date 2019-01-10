package com.iot.building.device.util;

import com.iot.building.helper.Constants;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 9:43 2018/6/13
 * @Modify by:
 */
public class DeviceContants {
    public static final String DISCONNECTED = "0";

    public static final String CONNECTED = "1";

    public static final String DEFAULT_SPACE = "0";

    public static String buildTopic(String sendTopicId, String topicMethod) {
        return Constants.TOPIC_CLIENT_PREFIX + sendTopicId + "/device/" + topicMethod;
    }
}
