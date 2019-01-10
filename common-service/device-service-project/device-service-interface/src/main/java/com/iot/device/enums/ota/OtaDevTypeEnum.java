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
public enum OtaDevTypeEnum {


    DIRECT(1, "直连设备"),

    SUB(0, "子设备");

    private Integer value;

    private String desc;

    OtaDevTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
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
    public static boolean checkIsValid(int value){
        for (OtaDevTypeEnum item: OtaDevTypeEnum.values()) {
            if (value == item.getValue()){
                return true;
            }
        }
        return false;
    }
}
