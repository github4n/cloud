package com.iot.shcs.contants;

/**
 * User: yuChangXing
 * Date: 2018/6/13
 * Time: 16:35
 * Desc: 网关错误码对照表
 */
public enum GatewayErrorCodeEnum {
    // 无效的设备id
    INVALID_DEVICE_ID("invalid deviceId", -33301),
    // 无效的sceneId
    INVALID_SCENE_ID("invalid sceneID", -33710),
    // 无效的iftttId
    INVALID_IFTTT_ID("invalid iftttID", -33810),

    // 保存scene规则部分失败
    SAVE_SCENE_RULE_ERROR("save scene rule error", 207)

    ;


    private int codeValue;
    private String codeDesc;

    GatewayErrorCodeEnum(String codeDesc, int codeValue) {
        this.codeDesc = codeDesc;
        this.codeValue = codeValue;
    }

    public int getCodeValue() {
        return codeValue;
    }

    public void setCodeValue(int codeValue) {
        this.codeValue = codeValue;
    }

    public String getCodeDesc() {
        return codeDesc;
    }

    public void setCodeDesc(String codeDesc) {
        this.codeDesc = codeDesc;
    }
}
