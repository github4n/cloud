package com.iot.shcs.security.constant;

/**
 * 项目名称: IOT云平台
 * 模块名称：
 * 功能描述： 布置安防模式,off:撤防,stay:在家布防,away:离家布防,紧急呼叫:panic
 * 创建人: yuChangXing
 * 创建时间: 2018/5/10 11:54
 * 修改人:
 * 修改时间：
 */
public enum ArmModeEnum {
    // 撤防
    off("off"),

    // 在家布防
    stay("stay"),

    // 离家布防
    away("away"),

    // 紧急呼叫
    panic("panic"),

    //安防活动日志
    Disarm("Disarm"),
    Stay("Stay"),
    Away("Away")
    ;


    private String armMode;

    public String getArmMode() {
        return armMode;
    }

    public void setArmMode(String armMode) {
        this.armMode = armMode;
    }

    ArmModeEnum(String armMode) {
        this.armMode = armMode;
    }

    public String toString() {
        return getArmMode();
    }
}
