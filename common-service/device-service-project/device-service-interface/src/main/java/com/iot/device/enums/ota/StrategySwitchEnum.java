package com.iot.device.enums.ota;

/** 
 * 
 * 项目名称：IOT云平台
 * 模块名称：设备
 * 功能描述：策略开关枚举类
 * 创建人： nongchongwei
 * 创建时间：2018/7/25 10:04
 * 修改人： nongchongwei
 * 修改时间：2018/7/25 10:04
 * 修改描述：
 */
public enum StrategySwitchEnum {

    UNUSE(0, "不使用"),
    USE (1, "使用"),

    ;

    private Integer value;

    private String desc;

    StrategySwitchEnum(Integer value, String desc) {
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
        for (StrategySwitchEnum item: StrategySwitchEnum.values()) {
            if (value == item.getValue()){
                return true;
            }
        }
        return false;
    }
}
