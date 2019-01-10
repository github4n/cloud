package com.iot.shcs.user.contants;

import com.iot.shcs.helper.Constants;

public class UserContants {

    public static String buildTopic(String sendTopicId, String topicMethod) {
        return Constants.TOPIC_CLIENT_PREFIX + sendTopicId + "/user/" + topicMethod;
    }
}
