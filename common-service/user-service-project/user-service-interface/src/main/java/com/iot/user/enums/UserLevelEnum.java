package com.iot.user.enums;

public enum UserLevelEnum {
	/** BOSS */
	BOSS(1, "BOSS"),

	/** 企业级用户 */
	BUSINESS(2, "企业级用户"),

	/** 终端用户 */
	CONSUMER(3, "终端用户"),
	;

    private UserLevelEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

	/** 类型编码 */
    private int code;

	/** 描述 */
    private String desc;

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}
