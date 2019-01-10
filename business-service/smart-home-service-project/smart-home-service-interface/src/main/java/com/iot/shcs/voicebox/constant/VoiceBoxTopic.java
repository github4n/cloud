package com.iot.shcs.voicebox.constant;

/**
 * @Descrpiton:
 * @Author: yuChangXing
 * @Date: 2018/10/8 14:15
 * @Modify by:
 */
public class VoiceBoxTopic {

    // 执行场景请求
    public static final String SCENE_TOPIC_EXC_SCENE_REQ = "iot/v1/s/%s/scene/excSceneReq";

    // 布置安防请求
    public static final String SECURITY_TOPIC_SET_ARM_MODE_REQ = "iot/v1/s/%s/security/setArmModeReq";

    // 获取安防状态请求
    public static final String SECURITY_TOPIC_GET_STATUS_REQ = "iot/v1/s/%s/security/getStatusReq";


    public static String getSceneTopicExcSceneReq(String userUuid) {
        return String.format(SCENE_TOPIC_EXC_SCENE_REQ, userUuid);
    }

    public static String getSecurityTopicSetArmModeReq(String userUuid) {
        return String.format(SECURITY_TOPIC_SET_ARM_MODE_REQ, userUuid);
    }

    public static String getSecurityTopicGetStatusReq(String userUuid) {
        return String.format(SECURITY_TOPIC_GET_STATUS_REQ, userUuid);
    }

    /*public static void main(String[] args) {
        System.out.printf(VoiceBoxTopic.getSceneTopicExcSceneReq("123456"));
    }*/
}
