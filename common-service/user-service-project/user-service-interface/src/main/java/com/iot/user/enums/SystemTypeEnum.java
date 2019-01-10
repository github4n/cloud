package com.iot.user.enums;

public enum SystemTypeEnum {
	
	/** 超级管理员 */
    USER_BOSS("1", "管理员系统"),

	/** 普通用户 */
    USER_PORTAL("2", "2B系统"),

    /** 普通用户 */
    USER_2C("3", "2C系统"),
    
    /** 普通用户 */
    USER_2B("4", "2B系统"),
    ;

    private SystemTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

	/** 类型编码 */
    private String code;

	/** 描述 */
    private String desc;

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}
