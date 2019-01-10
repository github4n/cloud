package com.iot.device.enums.ota;

/** 
 * 
 * 项目名称：IOT云平台
 * 模块名称：设备
 * 功能描述：分位枚举类
 * 创建人： maochengyuan 
 * 创建时间：2018/7/25 10:04
 * 修改人： maochengyuan
 * 修改时间：2018/7/25 10:04
 * 修改描述：
 */
public enum FwTypeEnum {

    ALL("0", "所有的模块在一个分位里面"),

    WIFI("1", "wifi 模块的分位"),

    ZIGBEE("2", "zigbee模块的分位"),

    ZWAVE("3", "z-wave模块的分位"),

    BLE("4", "ble模块的分位");

    private String value;

    private String desc;

    FwTypeEnum(String value, String desc) {
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
     * @author maochengyuan
     * @created 2018/7/25 10:00
     * @param value
     * @return boolean
     */
    public static boolean checkIsValid(String value){
        for (FwTypeEnum item: FwTypeEnum.values()) {
            if (item.getValue().equals(value)){
                return true;
            }
        }
        return false;
    }
}
