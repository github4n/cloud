package com.iot.common.enums;

/**
 * @author lucky
 * @EnumName PermissionEnum
 * @Description 0130 permission
 * @date 2019/1/3 15:38
 * @Version 1.0
 */
public enum PermissionEnum implements IPermission {

    HOME_LIST("01300001", "HOME 查看列表权限"),
    HOME_ADD("01300002", "HOME 添加权限"),
    HOME_UPDATE("01300003", "HOME 修改权限"),
    HOME_DELETE("01300004", "HOME 删除权限"),

    SCENE_LIST("01300101", "SCENE 查看列表权限"),
    SCENE_ADD("01300102", "SCENE 添加权限"),
    SCENE_UPDATE("01300103", "SCENE 修改权限"),
    SCENE_DELETE("01300104", "SCENE 删除权限"),

    SECURITY_LIST("01300201", "SECURITY 查看列表权限"),
    SECURITY_ADD("01300202", "SECURITY 添加权限"),
    SECURITY_UPDATE("01300303", "SECURITY 修改权限"),
    SECURITY_DELETE("01300304", "SECURITY 删除权限"),

    AUTO_LIST("01300301", "AUTO 查看列表权限"),
    AUTO_ADD("01300302", "AUTO 添加权限"),
    AUTO_UPDATE("01300303", "AUTO 修改权限"),
    AUTO_DELETE("01300304", "AUTO 删除权限"),
    ;
    private String code;

    private String remark;

    PermissionEnum(String code, String remark) {
        this.code = code;
        this.remark = remark;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getRemark() {
        return remark;
    }
}
