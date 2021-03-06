package com.iot.device.enums.ota;

/** 
 * 
 * 项目名称：IOT云平台
 * 模块名称：设备
 * 功能描述：升级包类型枚举类
 * 创建人： maochengyuan 
 * 创建时间：2018/7/25 10:04
 * 修改人： maochengyuan
 * 修改时间：2018/7/25 10:04
 * 修改描述：
 */
public enum OtaTypeEnum {

    WholePackage("WholePackage", "整包升级"),

    Difference("Difference", "差分升级");

    private String value;

    private String desc;

    OtaTypeEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    /**
     * 描述：判断是否属于此枚举
     * @author maochengyuan
     * @created 2018/7/25 10:00
     * @param value
     * @return boolean
     */
    public static boolean checkIsValid(String value){
        for (OtaTypeEnum item: OtaTypeEnum.values()) {
            if (item.getValue().equals(value)){
                return true;
            }
        }
        return false;
    }
}
