package com.iot.user.enums;

/**
 *
 * 项目名称：IOT云平台
 * 模块名称：用户
 * 功能描述：用户状态枚举
 * 创建人： maochengyuan
 * 创建时间：2018/7/14 10:28
 * 修改人： maochengyuan
 * 修改时间：2018/7/14 10:28
 * 修改描述：
 */
public enum LockStatusEnum {
	//	0:未锁定 1:锁定
	NOLOCKED(0, "未锁定"),

	LOCKED(1, "锁定");

	LockStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

	/** 类型编码 */
    private Integer code;

	/** 描述 */
    private String desc;

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }}
