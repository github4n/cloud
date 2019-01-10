package com.iot.device.enums.ota;

/** 
 * 
 * 项目名称：IOT云平台
 * 模块名称：设备
 * 功能描述：分位枚举类
 * 创建人： nongchongwei
 * 创建时间：2018/7/25 10:04
 * 修改人： nongchongwei
 * 修改时间：2018/7/25 10:04
 * 修改描述：
 */
public enum TaskTypeEnum {

    DIRECTFORCE("D", "强制升级直连设备"),

    SUBFORCE("S", "强制升级子设备"),

    PUSH("P", "推送至用户");

    private String value;

    private String desc;

    TaskTypeEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * 描述：判断是否属于此枚举
     * @author nongchongwei
     * @created 2018/7/25 10:00
     * @param value
     * @return boolean
     */
    public static boolean checkIsValid(String value){
        for (TaskTypeEnum item: TaskTypeEnum.values()) {
            if (value .equals(item.getValue()) ){
                return true;
            }
        }
        return false;
    }
}
