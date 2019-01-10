package com.iot.user.enums;

public enum AdminStatusEnum {
	/** 超级管理员 */
    SUPER(1, "超级管理员"),

	/** 普通用户 */
    NORMAL(2, "普通用户"),

    ;

    private AdminStatusEnum(int code, String desc) {
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
