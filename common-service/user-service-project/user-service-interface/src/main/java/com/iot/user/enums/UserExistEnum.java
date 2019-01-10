package com.iot.user.enums;

public enum UserExistEnum {
	/** 用户存在 */
	EXIST(0, "EXIST"),

	/** 用户不存在 */
	NOT_EXIST(1, "NOT_EXIST"),
	;

    private UserExistEnum(int code, String desc) {
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
